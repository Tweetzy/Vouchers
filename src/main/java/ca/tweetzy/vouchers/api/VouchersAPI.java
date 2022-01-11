package ca.tweetzy.vouchers.api;

import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.model.VoucherManager;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

	public void executeVoucher(@NonNull final Player player, @NonNull final Voucher voucher, @NonNull final ItemStack voucherItem) {
		voucherManager.executeVoucher(player, voucher, voucherItem);
	}

	public boolean isVoucher(@NonNull final ItemStack itemstack) {
		return voucherManager.isVoucher(itemstack);
	}

	public String getVoucherId(@NonNull final ItemStack itemstack) {
		return voucherManager.getVoucherId(itemstack);
	}

	public List<Voucher> getVouchers() {
		return voucherManager.getVouchers();
	}
}
