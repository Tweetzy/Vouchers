package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:12 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public class VVoucherSettings implements VoucherSettings, ConfigSerializable {

	private boolean glowing;
	private boolean askConfirm;
	private boolean removeOnUse;
	private boolean requireUsePermission;
	private String usePermission;

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
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"glowing", this.glowing,
				"ask confirm", this.askConfirm,
				"remove on use", this.removeOnUse,
				"require use permission", this.requireUsePermission,
				"use permission", this.usePermission
		);
	}

	public static VVoucherSettings deserialize(SerializedMap map) {
		return new VVoucherSettings(
			map.getBoolean("glowing"),
			map.getBoolean("ask confirm"),
			map.getBoolean("remove on use"),
			map.getBoolean("require use permission"),
			map.getString("use permission")
		);
	}
}
