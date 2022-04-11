package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.impl.VoucherReward;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 28 2021
 * Time Created: 1:29 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface IVoucher {

	/**
	 * Get the id of the voucher
	 *
	 * @return the voucher id
	 */
	@NonNull String getId();

	/**
	 * Get the physical that will be used
	 * to represent the voucher
	 *
	 * @return the {@link CompMaterial}
	 */
	@NonNull CompMaterial getIcon();

	/**
	 * Set the voucher item
	 *
	 * @param icon is the new {@link CompMaterial}
	 */
	void setIcon(@NonNull final CompMaterial icon);

	/**
	 * Get the display name of the voucher
	 *
	 * @return the voucher display name
	 */
	@NonNull String getDisplayName();

	/**
	 * Set the voucher display name
	 *
	 * @param name is the new name
	 */
	void setDisplayName(@NonNull final String name);

	/**
	 * Get the description of the voucher
	 *
	 * @return the voucher description
	 */
	@NonNull ArrayList<String> getDescription();

	/**
	 * Get the settings of the voucher
	 *
	 * @return the voucher settings
	 */
	IVoucherSettings getSettings();

	/**
	 * Get all the voucher rewards
	 *
	 * @return the voucher rewards
	 */
	@NonNull ArrayList<VoucherReward> getRewards();
}
