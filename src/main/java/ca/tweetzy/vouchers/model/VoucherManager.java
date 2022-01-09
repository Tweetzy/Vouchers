package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

	private final StrictList<Voucher> vouchers = new StrictList<>();

	public Voucher findVoucher(@NonNull final String id) {
		return this.vouchers.getSource().stream().filter(voucher -> voucher.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public void deleteVoucher(@NonNull final String id) {
		final Voucher voucher = this.findVoucher(id);
		if (voucher == null) return;
		this.vouchers.removeWeak(voucher);
	}

	public List<Voucher> getVouchers() {
		return Collections.unmodifiableList(this.vouchers.getSource());
	}
}
