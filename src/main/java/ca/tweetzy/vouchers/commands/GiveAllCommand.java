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
import ca.tweetzy.flight.command.CommandContext;
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

import java.util.List;

public final class GiveAllCommand extends Command {

	public GiveAllCommand() {
		super(AllowedExecutor.BOTH, "giveall");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		// player <#> <voucher>
		if (context.getArgCount() < 2) return ReturnType.INVALID_SYNTAX;

		final int quantity = MathUtil.isInt(context.getArg(0)) ? Integer.parseInt(context.getArg(0)) : 0;

		final Voucher voucher = Vouchers.getVoucherManager().get(context.getArg(1).toLowerCase());
		if (voucher == null) {
			tell(context.getSender(), TranslationManager.string(Translations.VOUCHER_NOT_FOUND, "voucher_id", context.getArg(1)));
			return ReturnType.FAIL;
		}

		final String[] voucherArgs = context.getArgCount() > 2 ? context.getArgs(2) : null;

		final BaseVoucher baseVoucher = (BaseVoucher) voucher;
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

			if (voucherArgs != null){
				baseVoucher.setArgs(voucherArgs);
				for (int i = 0 ; i < quantity; i++)
					PlayerUtil.giveItem(onlinePlayer, baseVoucher.generatePhysicalVoucher(onlinePlayer));
			} else {
				for (int i = 0 ; i < quantity; i++)
					PlayerUtil.giveItem(onlinePlayer, baseVoucher.generatePhysicalVoucher(onlinePlayer));
			}
		}

		return ReturnType.SUCCESS;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		return execute(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	protected List<String> tab(CommandContext context) {
		if (context.getArgCount() == 1)
			return List.of("1", "2", "3", "4", "5");

		if (context.getArgCount() == 2)
			return Vouchers.getVoucherManager().getValues().stream().map(Voucher::getId).toList();

		return List.of();
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return tab(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.giveall";
	}

	@Override
	public String getSyntax() {
		return "giveall <#> <voucher> [args]";
	}

	@Override
	public String getDescription() {
		return "Used to give all players a voucher";
	}
}
