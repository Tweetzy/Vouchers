package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.api.voucher.VoucherReward;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:13 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public class VVoucherReward implements VoucherReward, ConfigSerializable {

	private final RewardType rewardType;
	private final ItemStack item;
	private final String command;

	@Override
	public @NonNull RewardType getRewardType() {
		return this.rewardType;
	}

	@Override
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"reward type", this.rewardType,
				"item", this.item,
				"command", this.command
		);
	}

	public static VVoucherReward deserialize(SerializedMap map) {
		return new VVoucherReward(
				map.get("reward type", RewardType.class),
				map.getItem("item"),
				map.getString("command")
		);
	}
}
