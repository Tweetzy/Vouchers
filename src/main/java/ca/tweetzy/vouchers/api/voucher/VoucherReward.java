package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.RewardType;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:02 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface VoucherReward {

	/**
	 * Get the type of reward it is
	 *
	 * @return the {@link RewardType}
	 */
	@NonNull RewardType getRewardType();

	/**
	 * Get the item that will be given to the
	 * player when they redeem the reward
	 *
	 * @return the {@link ItemStack}
	 */
	ItemStack getItem();

	/**
	 * Get the command that will be executed
	 * when the player redeems the reward
	 *
	 * @return command to be executed
	 */
	String getCommand();
}