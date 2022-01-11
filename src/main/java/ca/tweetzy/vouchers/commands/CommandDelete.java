package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.settings.Localization;
import org.bukkit.entity.Player;

import java.util.Collections;

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
}
