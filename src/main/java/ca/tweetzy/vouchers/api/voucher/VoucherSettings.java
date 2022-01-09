package ca.tweetzy.vouchers.api.voucher;

import lombok.NonNull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 28 2021
 * Time Created: 1:31 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface VoucherSettings {

	/**
	 * Should the voucher have a glow effect
	 *
	 * @return true if a glow effect is needed
	 */
	boolean isGlowing();

	/**
	 * Set the glow status of the voucher
	 *
	 * @param val is the new value
	 */
	void setGlowing(final boolean val);

	/**
	 * Should the voucher ask for confirmation before redeem
	 *
	 * @return true if a confirmation is needed
	 */
	boolean askConfirm();

	/**
	 * Set the confirmation option
	 *
	 * @param val is the new value
	 */
	void setAskConfirm(final boolean val);

	/**
	 * Should the voucher be removed from the player's
	 * inventory when its used?
	 *
	 * @return true if it should be removed
	 */
	boolean removeOnUse();

	/**
	 * Set whether the voucher should be removed
	 *
	 * @param val is the new value
	 */
	void setRemoveOnUse(final boolean val);

	/**
	 * Should the player who is redeeming the voucher
	 * have it's permission to use it
	 *
	 * @return true if redeem perms are needed
	 */
	boolean requiresUsePermission();

	/**
	 * Set if a use permission is required
	 *
	 * @param val is the new value
	 */
	void setRequiresUsePermission(final boolean val);

	/**
	 * Get the permission required to activate the voucher
	 *
	 * @return the permission
	 */
	@NonNull String getPermission();

	/**
	 * Set the new use permission of the voucher
	 *
	 * @param permission is the new permission
	 */
	void setPermission(@NonNull final String permission);


}
