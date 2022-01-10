package ca.tweetzy.vouchers.model;

import ca.tweetzy.vouchers.impl.Voucher;
import lombok.Getter;
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

	@Getter
	private VoucherHolder voucherHolder;

	public Voucher findVoucher(@NonNull final String id) {
		return this.voucherHolder.getVouchers().getSource().stream().filter(voucher -> voucher.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public void addVoucher(@NonNull final Voucher voucher) {
		this.voucherHolder.getVouchers().addIfNotExist(voucher);
		this.voucherHolder.save();
	}

	public void deleteVoucher(@NonNull final String id) {
		final Voucher voucher = this.findVoucher(id);
		if (voucher == null) return;
		this.voucherHolder.getVouchers().removeWeak(voucher);
	}

	public List<Voucher> getVouchers() {
		return Collections.unmodifiableList(this.voucherHolder.getVouchers().getSource());
	}

	public void load() {
		this.voucherHolder = new VoucherHolder();
	}
}
