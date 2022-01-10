package ca.tweetzy.vouchers.api.voucher;

import lombok.NonNull;

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

	/**
	 * When the voucher is redeemed, should it be broadcast
	 * to every player online?
	 *
	 * @return true if it should broadcast
	 */
	boolean broadcastRedeem();

	/**
	 * Sets whether the voucher should be broadcast when redeemed
	 *
	 * @param broadcastRedeem is the new value
	 */
	void setBroadcastRedeem(final boolean broadcastRedeem);

	/**
	 * Get the broadcast msg
	 *
	 * @return the broadcast msg
	 */
	@NonNull String getBroadcastMessage();

	/**
	 * Set the new broadcast message
	 */
	void setBroadcastMessage(@NonNull final String msg);

	/**
	 * Should a title be sent on redeem
	 *
	 * @return true if a title should be sent
	 */
	boolean sendTitle();

	/**
	 * Set if a title should be sent
	 */
	void setSendTitle(final boolean send);

	/**
	 * Should a subtitle be sent on redeem
	 *
	 * @return true if a subtitle should be sent
	 */
	boolean sendSubtitle();

	/**
	 * Set if a subtitle should be sent
	 */
	void setSendSubtitle(final boolean send);

	/**
	 * Should an action bar be sent on redeem
	 *
	 * @return true if an action bar should be sent
	 */
	boolean sendActionBar();

	/**
	 * Set if an action bar should be sent
	 */
	void setSendActionBar(final boolean send);

	/**
	 * The title that will be sent if enabled
	 *
	 * @return the title
	 */
	@NonNull String getTitle();

	/**
	 * Set the new title
	 *
	 * @param title is the new title
	 */
	void setTitle(@NonNull final String title);

	/**
	 * The subtitle that will be sent if enabled
	 *
	 * @return the subtitle
	 */
	@NonNull String getSubtitle();

	/**
	 * Set the new title
	 *
	 * @param subtitle is the new subtitle
	 */
	void setSubtitle(@NonNull final String subtitle);

	/**
	 * The actionbar that will be sent if enabled
	 *
	 * @return the actionbar
	 */
	@NonNull String getActionBar();

	/**
	 * Set the actionbar
	 *
	 * @param actionbar is the actionbar
	 */
	void setActionbar(@NonNull final String actionbar);

	/**
	 * Get the chat message for when they redeem
	 * the voucher
	 *
	 * @return redeem message (chat msg)
	 */
	@NonNull String getRedeemMessage();

	/**
	 * Set the voucher redeem message
	 *
	 * @param msg is the new message
	 */
	void setRedeemMessage(@NonNull final String msg);
}
