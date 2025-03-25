package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.sync.Identifiable;

public interface Category extends Identifiable<String> {

	String getName();

	String getIcon();
}
