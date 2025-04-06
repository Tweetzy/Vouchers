/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
			for (int i = 0 ; i < quantity; i++)
				PlayerUtil.giveItem(target, baseVoucher.generatePhysicalVoucher(target));
		}else {
			for (int i = 0 ; i < quantity; i++)
				PlayerUtil.giveItem(target, baseVoucher.generatePhysicalVoucher(target));
		}

		return ReturnType.SUCCESS;
	}



	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		if (args.length == 1)
			return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

		if (args.length == 2)
			return List.of("1", "2", "3", "4", "5");

		if (args.length == 3)
			return Vouchers.getVoucherManager().getValues().stream().map(Voucher::getId).toList();

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
