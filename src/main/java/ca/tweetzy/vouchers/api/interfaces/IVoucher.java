package ca.tweetzy.vouchers.api.interfaces;

import lombok.NonNull;

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
	 * Get the settings of the voucher
	 *
	 * @return the voucher settings
	 */
	IVoucherSettings getSettings();
}
