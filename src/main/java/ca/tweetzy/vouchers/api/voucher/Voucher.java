/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.Identifiable;
import ca.tweetzy.vouchers.api.sync.Jsonable;
import ca.tweetzy.vouchers.api.sync.Storeable;
import ca.tweetzy.vouchers.api.sync.Synchronize;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public interface Voucher extends Identifiable<String>, Storeable<Voucher>, Jsonable, Synchronize {

	String getName();

	ItemStack getItem();

	List<String> getDescription();

	VoucherOptions getOptions();

	RewardMode getRewardMode();

	List<Reward> getRewards();

	void setName(String name);

	void setItem(ItemStack item);

	void setRewards(List<Reward> rewards);

	void setRewardMode(RewardMode rewardMode);

	void setDescription(List<String> description);

	ItemStack buildItem(Player player);

	ItemStack buildItem(Player player, List<String> params);

	void addReward(Reward reward);

	void removeReward(Reward reward);

	void setVoucherHand(EquipmentSlot hand);

	EquipmentSlot getVoucherHand();

	default List<String> getFilteredDescription() {
		List<String> desc = new ArrayList<>(getDescription());

		for (int i = 0; i < desc.size(); i++) {
			if (desc.get(i).contains("-Blank-"))
				desc.set(i, "");
		}

		return desc;
	}

	default void exportVoucher() {
		Vouchers.getInstance().getServer().getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
			File directory = new File(Vouchers.getInstance().getDataFolder() + "/vouchers/");
			if (!directory.exists()) {
				directory.mkdir();
			}

			try (Writer writer = new FileWriter(String.format("%s/vouchers/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()))) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();


				JsonObject object = new JsonObject();
				object.addProperty("item", getItem().getType().name());
				object.addProperty("displayName", Common.colorize(getName()));

				final JsonArray desc = new JsonArray();
				getDescription().forEach(line -> desc.add(Common.colorize(line)));
				object.add("description", desc);

				object.addProperty("rewardMode", getRewardMode().name());
				object.addProperty("maxUses", getOptions().getMaxUses());
				object.addProperty("cooldown", getOptions().getCooldown());
				object.addProperty("removeOnUse", getOptions().isRemoveOnUse());
				object.addProperty("askForConfirm", getOptions().isAskConfirm());
				object.addProperty("glowing", getOptions().isGlowing());
				object.addProperty("requirePermission", getOptions().isRequiresPermission());
				object.addProperty("permission", getOptions().getPermission());
				object.addProperty("playSound", getOptions().isPlayingSound());
				object.addProperty("sound", getOptions().getSound().name());


				final JsonArray broadcasts = new JsonArray();
				getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.BROADCAST).map(Message::getMessage).map(Common::colorize).forEach(broadcasts::add);
				object.add("broadcastMessages", broadcasts);

				final JsonArray chat = new JsonArray();
				getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.CHAT).map(Message::getMessage).map(Common::colorize).forEach(chat::add);
				object.add("chatMessages", chat);

				final JsonArray actionbar = new JsonArray();
				getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.ACTION_BAR).map(Message::getMessage).map(Common::colorize).forEach(actionbar::add);
				object.add("actionbarMessages", actionbar);

				final JsonObject titleObject = new JsonObject();
				getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.TITLE).toList().forEach(MSG -> {
					titleObject.addProperty("message", Common.colorize(MSG.getMessage()));
					titleObject.addProperty("fadeIn", MSG.getFadeInDuration());
					titleObject.addProperty("stay", MSG.getStayDuration());
					titleObject.addProperty("fadeOut", MSG.getFadeOutDuration());
				});

				final JsonObject subtitleObject = new JsonObject();
				getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.SUBTITLE).toList().forEach(MSG -> {
					subtitleObject.addProperty("message", Common.colorize(MSG.getMessage()));
					subtitleObject.addProperty("fadeIn", MSG.getFadeInDuration());
					subtitleObject.addProperty("stay", MSG.getStayDuration());
					subtitleObject.addProperty("fadeOut", MSG.getFadeOutDuration());
				});

				object.add("titleMessage", titleObject);
				object.add("subtitleMessage", subtitleObject);

				final JsonArray rewards = new JsonArray();

				getRewards().forEach(reward -> {
					final JsonObject rewardObject = new JsonObject();
					rewardObject.addProperty("type", reward.getType().name());
					rewardObject.addProperty("chance", reward.getChance());
					rewardObject.addProperty("delay", reward.getDelay());

					if (reward instanceof final CommandReward commandReward) {
						rewardObject.addProperty("command", commandReward.getCommand());
						rewardObject.addProperty("claimMessage", Common.colorize(commandReward.getClaimMessage()));
					}

					if (reward instanceof final ItemReward itemReward) {
						rewardObject.addProperty("item", NBT.itemStackToNBT(itemReward.getItem()).toString());
					}

					rewards.add(rewardObject);
				});

				object.add("rewards", rewards);

				gson.toJson(object, writer);

			} catch (IOException e) {
			}
		});
	}
}