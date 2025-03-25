package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public abstract class BaseVoucher implements Voucher {

	@Setter
	private VoucherType voucherType;

	@Setter
	private String[] args;

	public abstract ItemStack generatePhysicalVoucher(Player player);

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.newChain().async(() -> {
			File voucherFile = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files/%s.json".formatted(getId().toLowerCase()));
			boolean success = voucherFile.delete();

			if (success)
				Vouchers.getVoucherManager().remove(getId());

			if (syncResult != null)
				syncResult.accept(success ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		}).execute();
	}


}
