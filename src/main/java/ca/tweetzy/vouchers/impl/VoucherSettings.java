package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.tweety.remain.CompSound;
import ca.tweetzy.vouchers.api.RewardMode;
import ca.tweetzy.vouchers.api.voucher.IVoucherSettings;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:12 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public class VoucherSettings implements IVoucherSettings, ConfigSerializable {

	private RewardMode rewardMode;
	private CompSound sound;
	private boolean glowing;
	private boolean askConfirm;
	private boolean removeOnUse;
	private boolean requireUsePermission;
	private boolean broadcastRedeem;
	private boolean sendTitle;
	private boolean sendSubtitle;
	private boolean sendActionBar;
	private String title;
	private String subTitle;
	private String actionbar;
	private String redeemMessage;
	private String broadcastMessage;
	private String usePermission;

	public VoucherSettings(@NonNull final String id) {
		this(
				RewardMode.AUTOMATIC,
				CompSound.ORB_PICKUP,
				false,
				false,
				true,
				false,
				false,
				true,
				true,
				true,
				"&eVoucher Redeemed",
				"&7Redeemed the " + id + " voucher",
				"&aRedeemed the " + id + " voucher",
				"&aYou redeemed the voucher: &2" + id,
				"&2" + id + "&a voucher was redeemed",
				"vouchers.use." + id
		);
	}

	@Override
	public boolean isGlowing() {
		return this.glowing;
	}

	@Override
	public void setGlowing(boolean val) {
		this.glowing = val;
	}

	@Override
	public boolean askConfirm() {
		return this.askConfirm;
	}

	@Override
	public void setAskConfirm(boolean val) {
		this.askConfirm = val;
	}

	@Override
	public boolean removeOnUse() {
		return this.removeOnUse;
	}

	@Override
	public void setRemoveOnUse(boolean val) {
		this.removeOnUse = val;
	}

	@Override
	public boolean requiresUsePermission() {
		return this.requireUsePermission;
	}

	@Override
	public void setRequiresUsePermission(boolean val) {
		this.requireUsePermission = val;
	}

	@Override
	public @NonNull String getPermission() {
		return this.usePermission;
	}

	@Override
	public void setPermission(@NonNull String permission) {
		this.usePermission = permission;
	}

	@Override
	public boolean broadcastRedeem() {
		return this.broadcastRedeem;
	}

	@Override
	public void setBroadcastRedeem(boolean broadcastRedeem) {
		this.broadcastRedeem = broadcastRedeem;
	}

	@Override
	public @NotNull String getBroadcastMessage() {
		return this.broadcastMessage;
	}

	@Override
	public void setBroadcastMessage(@NonNull String msg) {
		this.broadcastMessage = msg;
	}

	@Override
	public boolean sendTitle() {
		return this.sendTitle;
	}

	@Override
	public void setSendTitle(boolean send) {
		this.sendActionBar = send;
	}

	@Override
	public boolean sendSubtitle() {
		return this.sendSubtitle;
	}

	@Override
	public void setSendSubtitle(boolean send) {
		this.sendSubtitle = send;
	}

	@Override
	public boolean sendActionBar() {
		return this.sendActionBar;
	}

	@Override
	public void setSendActionBar(boolean send) {
		this.sendActionBar = send;
	}

	@Override
	public @NonNull String getTitle() {
		return this.title;
	}

	@Override
	public void setTitle(@NonNull String title) {
		this.title = title;
	}

	@Override
	public @NonNull String getSubtitle() {
		return this.subTitle;
	}

	@Override
	public void setSubtitle(@NonNull String subtitle) {
		this.subTitle = subtitle;
	}

	@Override
	public @NonNull String getActionBar() {
		return this.actionbar;
	}

	@Override
	public void setActionbar(@NonNull String actionbar) {
		this.actionbar = actionbar;
	}

	@Override
	public @NonNull String getRedeemMessage() {
		return this.redeemMessage;
	}

	@Override
	public void setRedeemMessage(@NonNull String msg) {
		this.redeemMessage = msg;
	}

	@Override
	public @NonNull RewardMode getRewardMode() {
		return this.rewardMode;
	}

	@Override
	public void setRewardMode(@NonNull RewardMode mode) {
		this.rewardMode = mode;
	}

	@Override
	public @NonNull CompSound getSound() {
		return this.sound;
	}

	@Override
	public void setSound(@NonNull CompSound sound) {
		this.sound = sound;
	}

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"reward mode", this.rewardMode,
				"sound", this.sound,
				"glowing", this.glowing,
				"ask confirm", this.askConfirm,
				"remove on use", this.removeOnUse,
				"require use permission", this.requireUsePermission,
				"use permission", this.usePermission,
				"broadcast redeem", this.broadcastRedeem,
				"send title", this.sendTitle,
				"title", this.title,
				"send subtitle", this.sendSubtitle,
				"subtitle", this.subTitle,
				"send actionbar", this.sendActionBar,
				"actionbar", this.actionbar,
				"redeem message", this.redeemMessage,
				"broadcast message", this.broadcastMessage
		);
	}

	public static VoucherSettings deserialize(SerializedMap map) {
		return new VoucherSettings(
				map.get("reward mode", RewardMode.class),
				map.get("sound", CompSound.class),
				map.getBoolean("glowing"),
				map.getBoolean("ask confirm"),
				map.getBoolean("remove on use"),
				map.getBoolean("require use permission"),
				map.getBoolean("broadcast redeem"),
				map.getBoolean("send title"),
				map.getBoolean("send subtitle"),
				map.getBoolean("send actionbar"),
				map.getString("title"),
				map.getString("subtitle"),
				map.getString("actionbar"),
				map.getString("redeem message"),
				map.getString("broadcast message"),
				map.getString("use permission")
		);
	}
}
