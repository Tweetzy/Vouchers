package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.menu.MenuVoucherEdit;
import ca.tweetzy.vouchers.settings.Localization;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 4:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandCreate extends AbstractVoucherCommand {

	public CommandCreate() {
		super("create|new");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		new TitleInput(player, Localization.VoucherCreation.TITLE, Localization.VoucherCreation.SUBTITLE) {
			@Override
			public boolean onResult(String val) {
				if (Vouchers.getVoucherManager().findVoucher(val) != null) {
					tell(Localization.Error.VOUCHER_ALREADY_EXISTS.replace("{voucher_id}", val));
					return false;
				}

				Voucher voucher = new Voucher(val, CompMaterial.PAPER, val, new ArrayList<>(Collections.singletonList("&7sample lore")), new VoucherSettings(val), new ArrayList<>(Collections.singletonList(new VoucherReward())));
				Vouchers.getVoucherManager().addVoucher(voucher);
				new MenuVoucherEdit(voucher).displayTo(player);

				tell(Localization.Success.VOUCHER_CREATED.replace("{voucher_id}", val));
				return true;
			}
		};
	}
}
