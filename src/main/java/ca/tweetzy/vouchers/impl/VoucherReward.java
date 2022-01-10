package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.api.voucher.IVoucherReward;
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
public class VoucherReward implements IVoucherReward, ConfigSerializable {

	private RewardType rewardType;
	private ItemStack item;
	private String command;
	private double chance;

	public VoucherReward() {
		this(RewardType.COMMAND, null, "eco give {player} 5000", 100);
	}

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
	public double getChance() {
		return this.chance;
	}

	@Override
	public void setRewardType(@NonNull RewardType type) {
		this.rewardType = type;
	}

	@Override
	public void setItem(@NonNull ItemStack item) {
		this.item = item;
	}

	@Override
	public void setCommand(@NonNull String command) {
		this.command = command;
	}

	@Override
	public void setChance(double chance) {
		this.chance = chance;
	}

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"reward type", this.rewardType,
				"item", this.item,
				"command", this.command,
				"chance", this.chance
		);
	}

	public static VoucherReward deserialize(SerializedMap map) {
		return new VoucherReward(
				map.get("reward type", RewardType.class),
				map.getItem("item"),
				map.getString("command"),
				map.getDouble("chance")
		);
	}
}
