/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.VouchersAPI;
import ca.tweetzy.vouchers.model.manager.CategoryManager;
import ca.tweetzy.vouchers.model.manager.CooldownManager;
import ca.tweetzy.vouchers.model.manager.RedeemManager;
import ca.tweetzy.vouchers.model.manager.VoucherManager;
import lombok.NonNull;

public final class StandardVouchersAPI implements VouchersAPI {

	private final VoucherManager voucherManager;
	private final RedeemManager redeemManager;
	private final CooldownManager cooldownManager;
	private final CategoryManager categoryManager;

	public StandardVouchersAPI(
			@NonNull VoucherManager voucherManager,
			@NonNull RedeemManager redeemManager,
			@NonNull CooldownManager cooldownManager,
			@NonNull CategoryManager categoryManager) {
		this.voucherManager = voucherManager;
		this.redeemManager = redeemManager;
		this.cooldownManager = cooldownManager;
		this.categoryManager = categoryManager;
	}

	@Override
	public VoucherManager getVoucherManager() {
		return this.voucherManager;
	}

	@Override
	public RedeemManager getRedeemManager() {
		return this.redeemManager;
	}

	@Override
	public CooldownManager getCooldownManager() {
		return this.cooldownManager;
	}

	@Override
	public CategoryManager getCategoryManager() {
		return this.categoryManager;
	}
}
