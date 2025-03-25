package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.Category;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public final class VoucherCategory implements Category {

	private final String id;
	private final String name;
	private final String icon;

	@Override
	public @NonNull String getId() {
		return this.id.toLowerCase();
	}

	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public String getIcon() {
		return this.icon;
	}
}
