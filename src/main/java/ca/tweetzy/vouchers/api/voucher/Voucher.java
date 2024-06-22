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

import ca.tweetzy.flight.config.tweetzy.TweetzyYamlConfig;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.Synchronize;
import ca.tweetzy.vouchers.commands.CommandReload;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface Voucher extends Synchronize {

	String getId();

	String getName();

	ItemStack getItem();

	List<String> getDescription();

	VoucherOptions getOptions();

	RewardMode getRewardMode();

	List<Reward> getRewards();

	void setName(String name);

	void setItem(ItemStack item);

	void setRewardMode(RewardMode rewardMode);

	void setDescription(List<String> description);

	String getRewardJson();

	ItemStack buildItem(Player player);

	ItemStack buildItem(Player player, List<String> params);

	void addReward(Reward reward);

	void removeReward(Reward reward);

	default List<String> getFilteredDescription() {
		List<String> desc = new ArrayList<>(getDescription());

		for (int i = 0; i < desc.size(); i++) {
			if (desc.get(i).contains("-Blank-"))
				desc.set(i, "");
		}

		return desc;
	}

	default void exportVoucher() {
		TweetzyYamlConfig config = new TweetzyYamlConfig(new File(Vouchers.getInstance().getDataFolder() + "/vouchers", getId().toLowerCase() + ".yml"), Vouchers.getInstance().getLogger());

		config.createEntry("item", getItem().getType().name());
		config.createEntry("display name", getName());
		config.createEntry("description", getDescription());
		config.createEntry("max uses", getOptions().getMaxUses());
		config.createEntry("cooldown", getOptions().getCooldown());
		config.createEntry("ask for confirmation", getOptions().isAskConfirm());
		config.createEntry("remove on use", getOptions().isRemoveOnUse());
		config.createEntry("glowing", getOptions().isGlowing());

		config.createEntry("permission.required", getOptions().isRequiresPermission());
		config.createEntry("permission.permission", getOptions().getPermission());

		config.createEntry("sound.play sound", getOptions().isPlayingSound());
		config.createEntry("sound.sound", getOptions().getSound().name());

		List<String> broadcasts = getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.BROADCAST).map(Message::getMessage).toList();
		List<String> chats = getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.CHAT).map(Message::getMessage).toList();
		List<String> actionbar = getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.ACTION_BAR).map(Message::getMessage).toList();

		config.createEntry("messages.broadcast", broadcasts.isEmpty() ? new ArrayList<>() : broadcasts);
		config.createEntry("messages.chat", chats.isEmpty() ? new ArrayList<>() : chats);
		config.createEntry("messages.action bar", actionbar.isEmpty() ? new ArrayList<>() : actionbar);

		getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.TITLE).toList().forEach(MSG -> {
			config.createEntry("messages.title.message", MSG.getMessage());
			config.createEntry("messages.title.fade in", MSG.getFadeInDuration());
			config.createEntry("messages.title.stay", MSG.getStayDuration());
			config.createEntry("messages.title.fade out", MSG.getFadeOutDuration());
		});

		getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.SUBTITLE).toList().forEach(MSG -> {
			config.createEntry("messages.subtitle.message", MSG.getMessage());
			config.createEntry("messages.subtitle.fade in", MSG.getFadeInDuration());
			config.createEntry("messages.subtitle.stay", MSG.getStayDuration());
			config.createEntry("messages.subtitle.fade out", MSG.getFadeOutDuration());
		});

		config.createEntry("rewards",getRewards());
		config.init();
	}
}