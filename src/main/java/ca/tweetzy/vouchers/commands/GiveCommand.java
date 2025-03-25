package ca.tweetzy.vouchers.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.BaseVoucher;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class GiveCommand extends Command {

	public GiveCommand() {
		super(AllowedExecutor.BOTH, "give");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		// player <#> <voucher>
		if (args.length < 3) return ReturnType.INVALID_SYNTAX;

		final Player target = Bukkit.getPlayerExact(args[0]);

		if (target == null) {
			tell(sender, TranslationManager.string(Translations.PLAYER_NOT_FOUND, "value", args[0]));
			return ReturnType.FAIL;
		}

		final int quantity = MathUtil.isInt(args[1]) ? Integer.parseInt(args[1]) : 1;

		final Voucher voucher = Vouchers.getVoucherManager().get(args[2].toLowerCase());
		if (voucher == null) {
			tell(sender, TranslationManager.string(Translations.VOUCHER_NOT_FOUND, "voucher_id", args[2]));
			return ReturnType.FAIL;
		}

		final String[] voucherArgs = args.length > 3 ? Arrays.copyOfRange(args, 3, args.length) : null;

		final BaseVoucher baseVoucher = (BaseVoucher) voucher;

		if (voucherArgs != null){
			baseVoucher.setArgs(voucherArgs);
			PlayerUtil.giveItem(target, baseVoucher.generatePhysicalVoucher(target));
		}else {
		}

		return ReturnType.SUCCESS;
	}



	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return List.of();
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.give";
	}

	@Override
	public String getSyntax() {
		return "give <player> <#> <voucher> [args]";
	}

	@Override
	public String getDescription() {
		return "Used to give a player a voucher";
	}
}
