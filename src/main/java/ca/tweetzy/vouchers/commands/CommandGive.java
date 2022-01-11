package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.TabUtil;
import ca.tweetzy.tweety.remain.Remain;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Localization;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 10 2022
 * Time Created: 3:43 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandGive extends AbstractVoucherCommand {

	public CommandGive() {
		super("give");
		setMinArguments(2);
	}

	@Override
	protected void onCommand() {

		final String voucherId = args[0];

		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);
		if (voucher == null)
			returnTell(Localization.Error.VOUCHER_DOES_NOT_EXIST.replace("{voucher_id}", voucherId));

		final Player player = findPlayer(args[1]);
		final int amtToGive = args.length == 3 && NumberUtils.isNumber(args[2]) ? Integer.parseInt(args[2]) : 1;

		for (int i = 0; i < amtToGive; i++)
			PlayerUtil.addItems(player.getInventory(), voucher.build());

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return TabUtil.complete(args[0], Vouchers.getVoucherManager().getVouchers().stream().map(Voucher::getId).collect(Collectors.toList()));
		if (args.length == 2)
			return TabUtil.complete(args[1], Remain.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
		return NO_COMPLETE;
	}
}
