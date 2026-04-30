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

package ca.tweetzy.vouchers.impl;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.BaseVoucher;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import ca.tweetzy.vouchers.api.voucher.VoucherType;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.impl.message.VoucherActionBarMessage;
import ca.tweetzy.vouchers.impl.message.VoucherBroadcastMessage;
import ca.tweetzy.vouchers.impl.message.VoucherChatMessage;
import ca.tweetzy.vouchers.impl.message.VoucherTitleMessage;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.model.VoucherHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StandardVoucher extends BaseVoucher {

	private final String id;
	private String item;
	private String name;
	private List<String> description;
	private final VoucherSettings settings;
	private final List<Message> messages;
	private final List<Reward> rewards;

	@Setter
	private String category = "allvouchers";

	public StandardVoucher(
			@NonNull final String id,
			@NonNull final String item,
			@NonNull final String name,
			@NonNull final List<String> description,
			@NonNull final VoucherSettings settings,
			@NonNull final List<Message> messages,
			@NonNull final List<Reward> rewards
	) {
		super(VoucherType.STANDARD, new String[]{});
		this.id = id;
		this.item = item;
		this.name = name;
		this.description = description;
		this.settings = settings;
		this.messages = messages;
		this.rewards = rewards;
	}

	public static StandardVoucher empty(@NonNull final String id) {
		final List<Message> msgs = new ArrayList<>();
		final List<Reward> rewardList = new ArrayList<>();

		msgs.add(new VoucherBroadcastMessage("&aDefault broadcast message"));
		msgs.add(new VoucherChatMessage("&aHi &e%player% &7this is a chat msg"));
		msgs.add(new VoucherActionBarMessage("&aThis is an actionbar msg"));
		msgs.add(new VoucherTitleMessage("&aDefault title msg", "&bDefault subtitle msg", 20, 20, 20));

		final List<Message> cakeRewardMessages = new ArrayList<>();
		cakeRewardMessages.add(new VoucherChatMessage("&bYou won some cake"));
		cakeRewardMessages.add(new VoucherTitleMessage("&bReward Won", "&e+1 Cake", 20, 20, 20));
		
		rewardList.add(new ItemReward(
				CompMaterial.CAKE.parseItem(),
				100,
				0,
				cakeRewardMessages));

		final List<String> cmdDesc = new ArrayList<>();
		cmdDesc.add("&7Default command description");
		
		final List<Message> cmdRewardMessages = new ArrayList<>();
		cmdRewardMessages.add(new VoucherChatMessage("&bYou won &a$100"));
		cmdRewardMessages.add(new VoucherBroadcastMessage("&e%player% &7has won &a$1000"));
		
		rewardList.add(new CommandReward(
				"eco give %player% 1000",
				100,
				0,
				"<GRADIENT:B3EBF2>&LVoucher Command Reward</GRADIENT:AEC6CF>",
				cmdDesc,
				cmdRewardMessages
		));

		return new StandardVoucher(
				id.toLowerCase(),
				"PAPER",
				"&e%s Voucher".formatted(id),
				new ArrayList<>(List.of("&7This is the default lore for new vouchers", "&7You can change this in the file or gui")),
				new VoucherOptions(
						true,
						true,
						"vouchers.use.%s".formatted(id.toLowerCase()),
						true,
						CompSound.ENTITY_BAT_TAKEOFF,
						true,
						true,
						-1,
						false,
						1,
						RewardMode.AUTOMATIC,
						1
				),
				msgs,
				rewardList
		);
	}

	@Override
	public @NonNull String getId() {
		return this.id.toLowerCase();
	}

	@Override
	public String getItem() {
		return this.item;
	}

	@Override
	public void setItem(@NotNull String item) {
		this.item = item;
	}

	@Override
	public @NonNull String getDisplayName() {
		return this.name;
	}

	@Override
	public @NonNull List<String> getDescription() {
		return this.description;
	}

	@Override
	public void setDisplayName(@NonNull String displayName) {
		this.name = displayName;
	}

	@Override
	public void setDescription(@NonNull List<String> description) {
		this.description = description;
	}

	@Override
	public VoucherSettings getSettings() {
		return this.settings;
	}

	@Override
	public List<Message> getMessages() {
		return this.messages;
	}

	@Override
	public List<Reward> getRewards() {
		return this.rewards;
	}

	@Override
	public ItemStack generatePhysicalVoucher(Player player) {
		return QuickItem
				.of(getItem())
				.name(VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, getDisplayName()), getArgs()))
				.lore(VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, getDescription()), getArgs()))
				.glow(getSettings().useGlow())
				.hideTags(true)
				.tag("Tweetzy:Vouchers", getId())
				.tag("Tweetzy:VouchersArgs", String.join(" ", getArgs()))
				.make();
	}

	@Override
	public void store(@NonNull Consumer<Voucher> stored) {
		Vouchers.getInstance().getServer().getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
			File directory = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files/");
			if (!directory.exists()) {
				if (!directory.mkdirs()) {
					Vouchers.getInstance().getLogger().severe("Failed to create voucher-files directory!");
					try {
						stored.accept(null);
					} catch (Exception ex) {
						Vouchers.getInstance().getLogger().severe("Error in store callback: " + ex.getMessage());
					}
					return;
				}
			}

			File file = new File(String.format("%s/voucher-files/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()));
			
			try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
				 FileChannel channel = raf.getChannel();
				 FileLock lock = channel.tryLock()) {
				
				if (lock == null) {
					// Could not acquire lock, retry after short delay
					Vouchers.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Vouchers.getInstance(), () -> store(stored), 5L);
					return;
				}
				
				// Lock acquired, write file using FileChannel
				try {
					// Prepare JSON content first before truncating
					Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
					String jsonContent = gson.toJson(getAsJSON());
					
					// Convert string to bytes
					byte[] jsonBytes = jsonContent.getBytes(StandardCharsets.UTF_8);
					ByteBuffer buffer = ByteBuffer.wrap(jsonBytes);
					
					// Truncate file to 0 length to overwrite (only after we have the content ready)
					channel.truncate(0);
					channel.position(0);
					
					// Write JSON directly to FileChannel (avoids FileWriter lock conflicts)
					while (buffer.hasRemaining()) {
						channel.write(buffer);
					}
					channel.force(true); // Force write to disk
					
					stored.accept(this);
				} catch (IOException e) {
					Vouchers.getInstance().getLogger().severe("Failed to store voucher " + getId() + ": " + e.getMessage());
					e.printStackTrace();
					// Still call stored callback with null to indicate failure
					try {
						stored.accept(null);
					} catch (Exception ex) {
						Vouchers.getInstance().getLogger().severe("Error in store callback: " + ex.getMessage());
					}
				} catch (Exception e) {
					Vouchers.getInstance().getLogger().severe("Unexpected error storing voucher " + getId() + ": " + e.getMessage());
					e.printStackTrace();
					try {
						stored.accept(null);
					} catch (Exception ex) {
						Vouchers.getInstance().getLogger().severe("Error in store callback: " + ex.getMessage());
					}
				}
			} catch (IOException e) {
				Vouchers.getInstance().getLogger().severe("Failed to acquire file lock for voucher " + getId() + ": " + e.getMessage());
				e.printStackTrace();
				// Call stored callback with null to indicate failure
				try {
					stored.accept(null);
				} catch (Exception ex) {
					Vouchers.getInstance().getLogger().severe("Error in store callback: " + ex.getMessage());
				}
			} catch (Exception e) {
				Vouchers.getInstance().getLogger().severe("Unexpected error in store method for voucher " + getId() + ": " + e.getMessage());
				e.printStackTrace();
				try {
					stored.accept(null);
				} catch (Exception ex) {
					Vouchers.getInstance().getLogger().severe("Error in store callback: " + ex.getMessage());
				}
			}
		});
	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.getInstance().getServer().getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
			File file = new File(String.format("%s/voucher-files/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()));

			if (!file.exists()) {
				if (syncResult != null) {
					syncResult.accept(SynchronizeResult.FAILURE);
				}
				return;
			}

			// Retry logic for file locking
			int maxRetries = 5;
			int retryDelay = 100; // milliseconds
			
			for (int attempt = 0; attempt < maxRetries; attempt++) {
				try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
					 FileChannel channel = raf.getChannel();
					 FileLock lock = channel.tryLock()) {
					
					if (lock == null) {
						// Could not acquire lock, wait and retry
						if (attempt < maxRetries - 1) {
							try {
								Thread.sleep(retryDelay);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								if (syncResult != null) {
									syncResult.accept(SynchronizeResult.FAILURE);
								}
								return;
							}
							continue;
						} else {
							// Last attempt failed
							Vouchers.getInstance().getLogger().warning("Failed to acquire file lock for voucher " + getId() + " after " + maxRetries + " attempts");
							if (syncResult != null) {
								syncResult.accept(SynchronizeResult.FAILURE);
							}
							return;
						}
					}
					
					// Lock acquired, write file using FileChannel
					try {
						// Prepare JSON content first before truncating
						Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
						String jsonContent = gson.toJson(getAsJSON());
						
						// Convert string to bytes
						byte[] jsonBytes = jsonContent.getBytes(StandardCharsets.UTF_8);
						ByteBuffer buffer = ByteBuffer.wrap(jsonBytes);
						
						// Truncate file to 0 length to overwrite (only after we have the content ready)
						channel.truncate(0);
						channel.position(0);
						
						// Write JSON directly to FileChannel (avoids FileWriter lock conflicts)
						while (buffer.hasRemaining()) {
							channel.write(buffer);
						}
						channel.force(true); // Force write to disk
						
						// Update manager with saved voucher instance on main thread
						Vouchers.getInstance().getServer().getScheduler().runTask(Vouchers.getInstance(), () -> {
							Vouchers.getVoucherManager().update(getId(), this);
						});
						
						if (syncResult != null) {
							syncResult.accept(SynchronizeResult.SUCCESS);
						}
					} catch (IOException e) {
						Vouchers.getInstance().getLogger().severe("Failed to sync voucher " + getId() + ": " + e.getMessage());
						e.printStackTrace();
						if (syncResult != null) {
							syncResult.accept(SynchronizeResult.FAILURE);
						}
					}
					
					// Success, exit retry loop
					return;
					
				} catch (IOException e) {
					if (attempt < maxRetries - 1) {
						try {
							Thread.sleep(retryDelay);
						} catch (InterruptedException ie) {
							Thread.currentThread().interrupt();
							if (syncResult != null) {
								syncResult.accept(SynchronizeResult.FAILURE);
							}
							return;
						}
						continue;
					} else {
						Vouchers.getInstance().getLogger().severe("Failed to acquire file lock for voucher " + getId() + ": " + e.getMessage());
						e.printStackTrace();
						if (syncResult != null) {
							syncResult.accept(SynchronizeResult.FAILURE);
						}
						return;
					}
				}
			}
		});
	}

	@Override
	public JsonObject getAsJSON() {
		final JsonObject voucherObject = new JsonObject();
		voucherObject.addProperty("item", this.item);
		// Appearance
		final JsonObject appearanceObject = new JsonObject();
		appearanceObject.addProperty("display_name", this.name);
		appearanceObject.addProperty("glow", this.settings.useGlow());
		// description
		final JsonArray descriptionArray = new JsonArray();
		this.description.forEach(descriptionArray::add);
		appearanceObject.add("description", descriptionArray);
		voucherObject.add("appearance", appearanceObject);

		// permission
		final JsonObject permissionsObject = new JsonObject();
		permissionsObject.addProperty("requires_permission", this.settings.usePermission());
		permissionsObject.addProperty("use_permission", this.settings.getPermission());
		voucherObject.add("permissions", permissionsObject);

		// sounds
		final JsonObject soundsObject = new JsonObject();
		soundsObject.addProperty("play_sound", this.settings.useSound());
		soundsObject.addProperty("use_sound", this.settings.getSound().name());
		voucherObject.add("sounds", soundsObject);

		final JsonObject usageObject = new JsonObject();
		usageObject.addProperty("remove_on_use", this.settings.isRemoveOnUse());
		usageObject.addProperty("ask_for_confirm", this.settings.isAskForConfirm());
		usageObject.addProperty("max_uses", this.settings.getMaximumUses());

		final JsonObject cooldownObject = new JsonObject();
		cooldownObject.addProperty("use_cooldown", this.settings.useCooldown());
		cooldownObject.addProperty("cooldown_time", TimeConverter.convertSecondsToHumanReadable(this.settings.getCooldown()));
		usageObject.add("cooldown", cooldownObject);
		voucherObject.add("usage", usageObject);

		// messages
		voucherObject.add("messages", convertMessagesToObject(this.messages));
		// rewards
		final JsonObject rewardOptionsObject = new JsonObject();
		rewardOptionsObject.addProperty("reward_mode", this.settings.getRewardMode().name());
		rewardOptionsObject.addProperty("maximum_rewards", this.settings.getMaximumRewards());

		final JsonArray rewardsObject = new JsonArray();
		this.rewards.forEach(reward -> {
			final JsonObject rewardObject = new JsonObject();
			rewardObject.addProperty("chance", reward.getChance());
			rewardObject.addProperty("delay", reward.getDelay());;

			rewardObject.add("messages", convertMessagesToObject(reward.getMessages()));

			if (reward instanceof CommandReward commandReward) {
				rewardObject.addProperty("type", RewardType.COMMAND.name());
				rewardObject.addProperty("command", commandReward.getCommand());
				rewardObject.addProperty("name", commandReward.getName());

				final JsonArray cmdDesc = new JsonArray();
				commandReward.getDescription().forEach(cmdDesc::add);
				rewardObject.add("description", cmdDesc);
			}

			if (reward instanceof ItemReward itemReward) {
				rewardObject.addProperty("type", RewardType.ITEM.name());
				rewardObject.addProperty("item", QuickItem.toString(itemReward.getItem()));
			}

			rewardsObject.add(rewardObject);
		});

		rewardOptionsObject.add("rewards", rewardsObject);
		voucherObject.add("reward_options", rewardOptionsObject);

		return voucherObject;
	}

	private JsonObject convertMessagesToObject(List<Message> messages) {
		final JsonObject messagesObject = new JsonObject();

		final JsonArray broadcastMessages = new JsonArray();
		final JsonArray chatMessages = new JsonArray();
		final JsonArray actionBarMessages = new JsonArray();
		final JsonArray titleMessages = new JsonArray();

		messages.stream().map(msg -> (BaseMessage) msg).forEach(message -> {
			if (message.getMessageType() == MessageType.BROADCAST)
				broadcastMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.CHAT)
				chatMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.ACTION_BAR)
				actionBarMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.TITLE) {
				final VoucherTitleMessage titleMessage = (VoucherTitleMessage) message;
				final JsonObject titleObject = new JsonObject();
				titleObject.addProperty("title", titleMessage.getPrimaryContent());
				titleObject.addProperty("subtitle", titleMessage.getSecondaryContent());
				titleObject.addProperty("fade_in", titleMessage.getFadeInTime());
				titleObject.addProperty("stay_duration", titleMessage.getStayTime());
				titleObject.addProperty("fade_out", titleMessage.getFadeOutTime());
				titleMessages.add(titleObject);
			}
		});

		messagesObject.add("broadcast", broadcastMessages);
		messagesObject.add("chat_messages", chatMessages);
		messagesObject.add("actionbar", actionBarMessages);
		messagesObject.add("titles", titleMessages);

		return messagesObject;
	}

	@Override
	public String getCategoryId() {
		return this.category;
	}

	@Override
	public long getTimeCreated() {
		return 0;
	}

	@Override
	public long getLastUpdated() {
		return 0;
	}
}
