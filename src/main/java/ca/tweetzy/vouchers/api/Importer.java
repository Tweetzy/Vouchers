package ca.tweetzy.vouchers.api;

import ca.tweetzy.vouchers.api.voucher.Voucher;

import java.util.List;
import java.util.function.Consumer;

public interface Importer {

	boolean process(Consumer<List<Voucher>> vouchers);
}
