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

package ca.tweetzy.vouchers.impl.importer;

import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherImporter;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class VouchersImporter extends VoucherImporter {

	public VouchersImporter() {
		super("Vouchers");
	}

	@Override
	public boolean process(Consumer<List<Voucher>> vouchers) {
		final List<Voucher> foundVouchers = new ArrayList<>();

		File directory = new File(Vouchers.getInstance().getDataFolder() + "/vouchers/");
		if (!directory.exists()) return false;

		final File[] versionThreeVouchers = directory.listFiles();
		if (versionThreeVouchers == null) return false;

		// load files and convert
		for (File file : versionThreeVouchers) {
			final Voucher voucher = loadFoundVoucher(file);

			if (voucher != null)
				foundVouchers.add(voucher);
		}

		vouchers.accept(foundVouchers);
		return true;
	}

	public Voucher loadFoundVoucher(File file) {
		final String voucherId = file.getName().replace(".json", "").toLowerCase();
		final JsonObject object;
		try {
			object = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
		} catch (Exception e) {
			return null;
		}

		final String displayName = object.has("displayName") && !object.get("displayName").isJsonNull() ? object.get("displayName").getAsString() : "Un-named voucher";
		final ArrayList<String> description = new ArrayList<>();

		if (object.has("description")) {
			final JsonArray descArr = object.get("description").getAsJsonArray();
			descArr.forEach(element -> {
				if (element != null && !element.isJsonNull()) {
					description.add(element.getAsString());
				}
			});
		}

		final VoucherOptions options = new VoucherOptions();
		options.setUseGlow(object.has("glowing") && object.get("glowing").getAsBoolean());

		// permissions
		options.setUsePermission(object.has("requirePermission") && !object.get("requirePermission").isJsonNull() && object.get("requirePermission").getAsBoolean());
		options.setPermission(object.has("permission") && !object.get("permission").isJsonNull() ? object.get("permission").getAsString() : "vouchers.use.%s".formatted(voucherId));

		// sounds
		options.setUseSound(object.has("playSound") && !object.get("playSound").isJsonNull() && object.get("playSound").getAsBoolean());
		options.setSound(object.has("sound") && !object.get("sound").isJsonNull() ? CompSound.of(object.get("sound").getAsString()).orElse(CompSound.ENTITY_BAT_TAKEOFF) : CompSound.ENTITY_BAT_TAKEOFF);

		options.setRemoveOnUse(object.has("removeOnUse") && object.get("removeOnUse").getAsBoolean());
		options.setAskForConfirmation(object.has("askForConfirm") && object.get("askForConfirm").getAsBoolean());
		options.setMaximumUses(object.has("maxUses") ? object.get("maxUses").getAsInt() : -1);

		final int rawCooldownTime = object.get("cooldown").getAsInt();
		options.setUseCooldown(rawCooldownTime > 0);
		options.setCooldown(rawCooldownTime);

		final RewardMode rewardMode = object.has("rewardMode") ? Enum.valueOf(RewardMode.class, object.get("rewardMode").getAsString()) : RewardMode.AUTOMATIC;

		options.setMaximumRewards(1);
		options.setRewardMode(rewardMode);

		// rewards
		final List<Reward> rewardList = new ArrayList<>();

		// rewards list
		if (object.has("rewards")) {
			final JsonArray rewardObjects = object.get("rewards").getAsJsonArray();

			rewardObjects.forEach(rewardObjectElement -> {
				final JsonObject rewardObject = rewardObjectElement.getAsJsonObject();

				if (!rewardObject.has("type") || !rewardObject.has("chance")) return;

				final RewardType rewardType = Enum.valueOf(RewardType.class, rewardObject.get("type").getAsString());
				final double chance = rewardObject.get("chance").getAsDouble();
				final int delay = rewardObject.get("delay").getAsInt();

				if (rewardType == RewardType.COMMAND) {
					if (!rewardObject.has("command") || rewardObject.get("command").isJsonNull()) return;
					
					final String name = rewardObject.has("name") && !rewardObject.get("name").isJsonNull() ? rewardObject.get("name").getAsString() : "<GRADIENT:B3EBF2>&LVoucher Command Reward</GRADIENT:AEC6CF>";
					final List<String> cmdDesc = new ArrayList<>();
					if (rewardObject.has("description")) {
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
							new ArrayList<>()
					));
				} else {
					if (!rewardObject.has("item") || rewardObject.get("item").isJsonNull()) return;
					
					rewardList.add(new ItemReward(
							QuickItem.getItem(rewardObject.get("item").getAsString()),
							chance,
							delay,
							new ArrayList<>()
					));
				}
			});
		}

		return new StandardVoucher(
				voucherId,
				object.has("item") && !object.get("item").isJsonNull() ? object.get("item").getAsString() : "PAPER",
				displayName,
				description,
				options,
				extractMessages(object),
				rewardList
		);
	}

	public List<Message> extractMessages(@NonNull final JsonObject object) {
		final List<Message> messageList = new ArrayList<>();

		// broadcast messages
		if (object.has("broadcastMessages"))
			object.get("broadcastMessages").getAsJsonArray().forEach(element -> {
				if (element != null && !element.isJsonNull()) {
					final String line = element.getAsString();
					messageList.add(new VoucherBroadcastMessage(line));
				}
			});

		if (object.has("chatMessages"))
			object.get("chatMessages").getAsJsonArray().forEach(element -> {
				if (element != null && !element.isJsonNull()) {
					final String line = element.getAsString();
					messageList.add(new VoucherChatMessage(line));
				}
			});

		if (object.has("actionbarMessages"))
			object.get("actionbarMessages").getAsJsonArray().forEach(element -> {
				if (element != null && !element.isJsonNull()) {
					final String line = element.getAsString();
					messageList.add(new VoucherActionBarMessage(line));
				}
			});


		String title = "", subtitle = "";
		int fadeIn = 20, duration = 20, fadeOut = 20;

		if (object.has("titleMessage")) {
			final JsonObject titleObject = object.get("titleMessage").getAsJsonObject();
			if (titleObject.has("message") && !titleObject.get("message").isJsonNull()) {
				title = titleObject.get("message").getAsString();
				fadeIn = titleObject.has("fadeIn") && !titleObject.get("fadeIn").isJsonNull() ? titleObject.get("fadeIn").getAsInt() : 20;
				duration = titleObject.has("stay") && !titleObject.get("stay").isJsonNull() ? titleObject.get("stay").getAsInt() : 20;
				fadeOut = titleObject.has("fadeOut") && !titleObject.get("fadeOut").isJsonNull() ? titleObject.get("fadeOut").getAsInt() : 20;
			}
		}

		if (object.has("subtitleMessage")) {
			final JsonObject titleObject = object.get("subtitleMessage").getAsJsonObject();
			if (titleObject.has("message") && !titleObject.get("message").isJsonNull()) {
				subtitle = titleObject.get("message").getAsString();
			}
		}

		if (!title.isBlank() || !subtitle.isBlank())
			messageList.add(new VoucherTitleMessage(
					title, subtitle, fadeIn, duration, fadeOut
			));

		return messageList;
	}

	@Override
	protected boolean foundVouchers() {
		File directory = new File(Vouchers.getInstance().getDataFolder() + "/vouchers/");
		if (!directory.exists()) return false;

		final File[] versionThreeVouchers = directory.listFiles();
		return versionThreeVouchers != null && versionThreeVouchers.length != 0;
	}
}
