package ca.tweetzy.vouchers.api;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class VoucherImporter implements Importer {

	protected String pluginName;

	protected boolean foundVouchers() {
		return false;
	}
}
