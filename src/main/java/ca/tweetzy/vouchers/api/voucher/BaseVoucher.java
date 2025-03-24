package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.utils.QuickItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public abstract class BaseVoucher implements Voucher {

	@Setter
	private VoucherType voucherType;

	public abstract ItemStack generatePhysicalVoucher(Player player);
}
