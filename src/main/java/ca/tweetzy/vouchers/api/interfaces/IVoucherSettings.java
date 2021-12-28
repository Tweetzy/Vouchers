package ca.tweetzy.vouchers.api.interfaces;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 28 2021
 * Time Created: 1:31 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public interface IVoucherSettings {

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

	boolean askConfirm();

	void setAskConfirm(final boolean val);

	boolean hideAttributes();

	void setHideAttributes(final boolean val);

	boolean removeOnUse();

	void setRemoveOnUse(final boolean val);

}
