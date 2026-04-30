/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.nbtapi.iface.ReadableItemNBT;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.manager.KeyValueManager;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.impl.StandardVoucher;
import ca.tweetzy.vouchers.impl.VoucherOptions;
import ca.tweetzy.vouchers.impl.message.VoucherActionBarMessage;
import ca.tweetzy.vouchers.impl.message.VoucherBroadcastMessage;
import ca.tweetzy.vouchers.impl.message.VoucherChatMessage;
import ca.tweetzy.vouchers.impl.message.VoucherTitleMessage;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class VoucherManager extends KeyValueManager<String, Voucher> {

	public record SyncFromDiskResult(int updated, int failed, int removed) {
	}

	public VoucherManager() {
		super("Voucher");
	}

	public boolean doesVoucherWithIdExists(@NonNull final String voucherId) {
		return this.getManagerContent().containsKey(ChatColor.stripColor(voucherId).toLowerCase());
	}

	public boolean isVoucher(final ItemStack itemStack) {
		if (itemStack == null || itemStack.getType() == CompMaterial.AIR.get() || itemStack.getAmount() == 0) return false;
		return NBT.get(itemStack, nbt -> (boolean) nbt.hasTag("Tweetzy:Vouchers"));
	}

	/**
	 * Locates a voucher item in the player's inventory matching id and raw args (as stored in NBT {@code Tweetzy:VouchersArgs}).
	 */
	@Nullable
	public ItemStack findMatchingVoucherStack(@NonNull final Player player, @NonNull final String voucherId, @Nullable final String voucherArgsRaw) {
		final String wantId = ChatColor.stripColor(voucherId).toLowerCase();
		final String wantArgs = voucherArgsRaw == null ? "" : voucherArgsRaw;
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			final ItemStack item = player.getInventory().getItem(i);
			if (item == null || !isVoucher(item)) continue;
			final String id = NBT.get(item, (ReadableItemNBT nbt) -> nbt.getString("Tweetzy:Vouchers"));
			if (id == null || !ChatColor.stripColor(id).toLowerCase().equals(wantId)) continue;
			final String args = NBT.get(item, (ReadableItemNBT nbt) -> {
				final String s = nbt.getString("Tweetzy:VouchersArgs");
				return s == null ? "" : s;
			});
			if (args.equals(wantArgs)) return item;
		}
		return null;
	}

	@Override
	public void load() {
		// load existing voucher files
		Bukkit.getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {

			File vouchersDirectory = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files");
			File[] files = vouchersDirectory.listFiles();

			if (files == null) return;

			for (File file : files) {
				final String voucherId = file.getName().replace(".json", "").toLowerCase();
				final Voucher voucher = loadVoucherFromFile(file);

				if (voucher != null)
					add(voucherId, voucher);
			}

		});
	}

	/**
	 * Reloads all voucher definitions from {@code voucher-files/*.json}. Vouchers with no file on disk are removed.
	 * Call from an async context; mutates the manager map only (safe with concurrent readers).
	 */
	public SyncFromDiskResult syncFromDisk() {
		final File dir = new File(Vouchers.getInstance().getDataFolder(), "voucher-files");
		if (!dir.isDirectory()) {
			return new SyncFromDiskResult(0, 0, 0);
		}

		final File[] files = dir.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".json"));
		if (files == null) {
			return new SyncFromDiskResult(0, 0, 0);
		}

		final Set<String> idsOnDisk = new HashSet<>();
		int updated = 0;
		int failed = 0;

		for (final File file : files) {
			final String id = file.getName().replace(".json", "").toLowerCase();
			idsOnDisk.add(id);
			final Voucher voucher = loadVoucherFromFile(file);
			if (voucher == null) {
				failed++;
			} else {
				this.update(id, voucher);
				updated++;
			}
		}

		int removed = 0;
		for (final String id : new HashSet<>(this.managerContent.keySet())) {
			if (!idsOnDisk.contains(id)) {
				this.remove(id);
				removed++;
			}
		}

		return new SyncFromDiskResult(updated, failed, removed);
	}

	public Voucher loadVoucherFromFile(File file) {
		final String voucherId = file.getName().replace(".json", "").toLowerCase();
		JsonObject object = null;
		
		// Retry logic for file locking
		int maxRetries = 10;
		int retryDelay = 100; // milliseconds
		
		for (int attempt = 0; attempt < maxRetries; attempt++) {
			try (RandomAccessFile raf = new RandomAccessFile(file, "r");
				 FileChannel channel = raf.getChannel();
				 FileLock lock = channel.tryLock(0, Long.MAX_VALUE, true)) { // Shared read lock
				
				if (lock == null) {
					// Could not acquire lock, wait and retry
					if (attempt < maxRetries - 1) {
						try {
							Thread.sleep(retryDelay);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							Vouchers.getInstance().getLogger().warning("Interrupted while waiting for file lock: " + file.getName());
							return null;
						}
						continue;
					} else {
						Vouchers.getInstance().getLogger().warning("Failed to acquire read lock for voucher file " + file.getName() + " after " + maxRetries + " attempts");
						return null;
					}
				}
				
				// Lock acquired, read file
				try (FileReader reader = new FileReader(file)) {
					// Check if file is empty
					if (file.length() == 0) {
						Vouchers.getInstance().getLogger().warning("Voucher file is empty: " + file.getName());
						return null;
					}
					
					com.google.gson.JsonElement jsonElement = JsonParser.parseReader(reader);
					if (jsonElement == null || !jsonElement.isJsonObject()) {
						Vouchers.getInstance().getLogger().severe("Voucher file does not contain a valid JSON object: " + file.getName());
						return null;
					}
					
					object = jsonElement.getAsJsonObject();
					break; // Success, exit retry loop
				}
				
			} catch (FileNotFoundException e) {
				Vouchers.getInstance().getLogger().severe("Failed to load voucher file: " + file.getName() + " - File not found");
				return null;
			} catch (IOException e) {
				if (attempt < maxRetries - 1) {
					try {
						Thread.sleep(retryDelay);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						Vouchers.getInstance().getLogger().warning("Interrupted while waiting for file lock: " + file.getName());
						return null;
					}
					continue;
				} else {
					Vouchers.getInstance().getLogger().severe("Failed to load voucher file: " + file.getName() + " - " + e.getMessage());
					e.printStackTrace();
					return null;
				}
			} catch (IllegalStateException e) {
				// File might be empty or contain invalid JSON (possibly being written to)
				if (attempt < maxRetries - 1) {
					try {
						Thread.sleep(retryDelay);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						Vouchers.getInstance().getLogger().warning("Interrupted while waiting for file lock: " + file.getName());
						return null;
					}
					continue;
				} else {
					Vouchers.getInstance().getLogger().severe("Failed to load voucher file: " + file.getName() + " - Not a valid JSON object (file may be empty or corrupted)");
					return null;
				}
			} catch (Exception e) {
				Vouchers.getInstance().getLogger().severe("Failed to load voucher file: " + file.getName() + " - " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		
		if (object == null) {
			return null;
		}

		if (!object.has("appearance") || object.get("appearance").isJsonNull()) {
			return null;
		}

		final JsonObject appearanceObject = object.get("appearance").getAsJsonObject();
		final String displayName = appearanceObject.has("display_name") && !appearanceObject.get("display_name").isJsonNull() ? appearanceObject.get("display_name").getAsString() : "Un-named voucher";

		final ArrayList<String> description = new ArrayList<>();

		if (appearanceObject.has("description") && !appearanceObject.get("description").isJsonNull()) {
			final JsonArray descArr = appearanceObject.get("description").getAsJsonArray();
			descArr.forEach(element -> {
				if (element != null && !element.isJsonNull()) {
					description.add(element.getAsString());
				}
			});
		}

		final VoucherOptions options = new VoucherOptions();
		options.setUseGlow(appearanceObject.has("glow") && !appearanceObject.get("glow").isJsonNull() && appearanceObject.get("glow").getAsBoolean());

		// permissions
		if (object.has("permissions") && !object.get("permissions").isJsonNull()) {
			final JsonObject permissionObject = object.get("permissions").getAsJsonObject();
			options.setUsePermission(permissionObject.has("requires_permission") && !permissionObject.get("requires_permission").isJsonNull() && permissionObject.get("requires_permission").getAsBoolean());
			options.setPermission(permissionObject.has("use_permission") && !permissionObject.get("use_permission").isJsonNull() ? permissionObject.get("use_permission").getAsString() : "vouchers.use.%s".formatted(voucherId));
		}

		// sounds
		if (object.has("sounds") && !object.get("sounds").isJsonNull()) {
			final JsonObject soundsObject = object.get("sounds").getAsJsonObject();
			options.setUseSound(soundsObject.has("play_sound") && !soundsObject.get("play_sound").isJsonNull() && soundsObject.get("play_sound").getAsBoolean());
			options.setSound(soundsObject.has("use_sound") && !soundsObject.get("use_sound").isJsonNull() ? CompSound.of(soundsObject.get("use_sound").getAsString()).orElse(CompSound.ENTITY_BAT_TAKEOFF) : CompSound.ENTITY_BAT_TAKEOFF);
		}

		// usage
		if (object.has("usage") && !object.get("usage").isJsonNull()) {
			final JsonObject usageObject = object.get("usage").getAsJsonObject();
			options.setRemoveOnUse(usageObject.has("remove_on_use") && !usageObject.get("remove_on_use").isJsonNull() && usageObject.get("remove_on_use").getAsBoolean());
			options.setAskForConfirmation(usageObject.has("ask_for_confirm") && !usageObject.get("ask_for_confirm").isJsonNull() && usageObject.get("ask_for_confirm").getAsBoolean());
			options.setMaximumUses(usageObject.has("max_uses") && !usageObject.get("max_uses").isJsonNull() ? usageObject.get("max_uses").getAsInt() : -1);

			if (usageObject.has("cooldown") && !usageObject.get("cooldown").isJsonNull()) {
				final JsonObject cooldownObject = usageObject.get("cooldown").getAsJsonObject();
				options.setUseCooldown(cooldownObject.has("use_cooldown") && !cooldownObject.get("use_cooldown").isJsonNull() && cooldownObject.get("use_cooldown").getAsBoolean());

				if (cooldownObject.has("cooldown_time") && !cooldownObject.get("cooldown_time").isJsonNull()) {
					final String rawCooldownTime = cooldownObject.get("cooldown_time").getAsString();
					final long milliseconds = TimeConverter.convertHumanReadableTime(rawCooldownTime);

					options.setCooldown(TimeUnit.MILLISECONDS.toSeconds(milliseconds));
				}
			}

		}

		// rewards
		final List<Reward> rewardList = new ArrayList<>();

		if (object.has("reward_options") && !object.get("reward_options").isJsonNull()) {
			final JsonObject rewardOptionsObject = object.get("reward_options").getAsJsonObject();

			final int maximumRewards = rewardOptionsObject.has("maximum_rewards") && !rewardOptionsObject.get("maximum_rewards").isJsonNull() ? rewardOptionsObject.get("maximum_rewards").getAsInt() : 1;
			options.setMaximumRewards(maximumRewards);

			// rewards list
			if (rewardOptionsObject.has("rewards") && !rewardOptionsObject.get("rewards").isJsonNull()) {
				final JsonArray rewardObjects = rewardOptionsObject.get("rewards").getAsJsonArray();
				final RewardMode rewardMode = rewardOptionsObject.has("reward_mode") && !rewardOptionsObject.get("reward_mode").isJsonNull() ? Enum.valueOf(RewardMode.class, rewardOptionsObject.get("reward_mode").getAsString()) : RewardMode.AUTOMATIC;
				options.setRewardMode(rewardMode);

				rewardObjects.forEach(rewardObjectElement -> {
					final JsonObject rewardObject = rewardObjectElement.getAsJsonObject();
					// TODO figure out the type of reward it is
					if (!rewardObject.has("type") || !rewardObject.has("chance") || !rewardObject.has("delay")) return;
					
					if (rewardObject.get("type").isJsonNull() || rewardObject.get("chance").isJsonNull() || rewardObject.get("delay").isJsonNull()) return;

					final RewardType rewardType;
					try {
						rewardType = Enum.valueOf(RewardType.class, rewardObject.get("type").getAsString());
					} catch (IllegalArgumentException e) {
						Vouchers.getInstance().getLogger().warning("Invalid reward type in voucher " + voucherId + ": " + rewardObject.get("type").getAsString());
						return;
					}
					final double chance = rewardObject.get("chance").getAsDouble();
					final int delay = rewardObject.get("delay").getAsInt();
					final List<Message> rewardMessages = extractMessages(rewardObject);

					if (rewardType == RewardType.COMMAND) {
						if (!rewardObject.has("command") || rewardObject.get("command").isJsonNull()) return;
						
						final String name = rewardObject.has("name") && !rewardObject.get("name").isJsonNull() ? rewardObject.get("name").getAsString() : "<GRADIENT:B3EBF2>&LVoucher Command Reward</GRADIENT:AEC6CF>";
						final List<String> cmdDesc = new ArrayList<>();
					if (rewardObject.has("description") && !rewardObject.get("description").isJsonNull()) {
						final JsonArray descArr = rewardObject.get("description").getAsJsonArray();
							descArr.forEach(element -> {
								if (element != null && !element.isJsonNull()) {
									cmdDesc.add(element.getAsString());
								}
							});
						} else {
							cmdDesc.add("&7Default command description");
						}

						rewardList.add(new CommandReward(
								rewardObject.get("command").getAsString(),
								chance,
								delay,
								name,
								cmdDesc,
								rewardMessages
						));
					} else {
						if (!rewardObject.has("item") || rewardObject.get("item").isJsonNull()) return;
						
						rewardList.add(new ItemReward(
								QuickItem.getItem(rewardObject.get("item").getAsString()),
								chance,
								delay,
								rewardMessages
						));
					}
				});
			}
		}

		StandardVoucher standardVoucher = new StandardVoucher(
				voucherId,
				object.has("item") && !object.get("item").isJsonNull() ? object.get("item").getAsString() : "PAPER",
				displayName,
				description,
				options,
				extractMessages(object),
				rewardList
		);

		if (object.has("category") && !object.get("category").isJsonNull()) {
			standardVoucher.setCategory(object.get("category").getAsString());
		}

		return standardVoucher;
	}

	public List<Message> extractMessages(@NonNull final JsonObject object) {
		final List<Message> messageList = new ArrayList<>();

		if (object.has("messages") && !object.get("messages").isJsonNull()) {
			final JsonObject messagesObject = object.get("messages").getAsJsonObject();

			// broadcast messages
			if (messagesObject.has("broadcast") && !messagesObject.get("broadcast").isJsonNull())
				messagesObject.get("broadcast").getAsJsonArray().forEach(element -> {
					if (element != null && !element.isJsonNull()) {
						final String line = element.getAsString();
						messageList.add(new VoucherBroadcastMessage(line));
					}
				});

			if (messagesObject.has("chat_messages") && !messagesObject.get("chat_messages").isJsonNull())
				messagesObject.get("chat_messages").getAsJsonArray().forEach(element -> {
					if (element != null && !element.isJsonNull()) {
						final String line = element.getAsString();
						messageList.add(new VoucherChatMessage(line));
					}
				});

			if (messagesObject.has("actionbar") && !messagesObject.get("actionbar").isJsonNull())
				messagesObject.get("actionbar").getAsJsonArray().forEach(element -> {
					if (element != null && !element.isJsonNull()) {
						final String line = element.getAsString();
						messageList.add(new VoucherActionBarMessage(line));
					}
				});

			if (messagesObject.has("titles") && !messagesObject.get("titles").isJsonNull())
				messagesObject.get("titles").getAsJsonArray().forEach(element -> {
					if (element != null && !element.isJsonNull() && element.isJsonObject()) {
						final JsonObject titleObject = element.getAsJsonObject();
						final String title = titleObject.has("title") && !titleObject.get("title").isJsonNull() ? titleObject.get("title").getAsString() : "";
						final String subtitle = titleObject.has("subtitle") && !titleObject.get("subtitle").isJsonNull() ? titleObject.get("subtitle").getAsString() : "";
						final int fadeIn = titleObject.has("fade_in") && !titleObject.get("fade_in").isJsonNull() ? titleObject.get("fade_in").getAsInt() : 20;
						final int stayDuration = titleObject.has("stay_duration") && !titleObject.get("stay_duration").isJsonNull() ? titleObject.get("stay_duration").getAsInt() : 20;
						final int fadeOut = titleObject.has("fade_out") && !titleObject.get("fade_out").isJsonNull() ? titleObject.get("fade_out").getAsInt() : 20;
						messageList.add(new VoucherTitleMessage(title, subtitle, fadeIn, stayDuration, fadeOut));
					}
				});
		}

		return messageList;
	}
}
