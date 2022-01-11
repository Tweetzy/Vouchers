package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.TabUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Localization;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 4:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandDelete extends AbstractVoucherCommand {

	public CommandDelete() {
		super("delete|remove");
	}

	@Override
	protected void onCommand() {
		final String voucherId = args[0];

		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);
		if (voucher == null)
			returnTell(Localization.Error.VOUCHER_DOES_NOT_EXIST.replace("{voucher_id}", voucherId));

		Vouchers.getVoucherManager().deleteVoucher(voucher.getId());
		tell(Localization.Success.VOUCHER_DELETED.replace("{voucher_id}", voucherId));
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return TabUtil.complete(args[0], Vouchers.getVoucherManager().getVouchers().stream().map(Voucher::getId).collect(Collectors.toList()));
		return NO_COMPLETE;
	}
}
