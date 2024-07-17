package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.vouchers.api.manager.KeyValueManager;
import ca.tweetzy.vouchers.api.voucher.Category;

public final class VoucherCategoryManager extends KeyValueManager<String, Category> {

	public VoucherCategoryManager() {
		super("Voucher Category");
	}

	@Override
	public void load() {

	}
}
