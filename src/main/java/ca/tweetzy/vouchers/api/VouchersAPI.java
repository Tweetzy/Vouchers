package ca.tweetzy.vouchers.api;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.model.VoucherManager;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 28 2021
 * Time Created: 1:29 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class VouchersAPI {

	private final VoucherManager voucherManager = Vouchers.getVoucherManager();

	public Voucher findVoucher(@NonNull final String id) {
		return voucherManager.findVoucher(id);
	}

	public void addVoucher(@NonNull final Voucher voucher) {
		voucherManager.addVoucher(voucher);
	}

	public void deleteVoucher(@NonNull final String id) {
		voucherManager.deleteVoucher(id);
	}

	public List<Voucher> getVouchers() {
		return voucherManager.getVouchers();
	}
}
