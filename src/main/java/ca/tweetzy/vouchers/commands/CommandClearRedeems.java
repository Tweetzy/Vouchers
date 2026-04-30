/*
 * Vouchers
 * Copyright 2022-2025 Kiran Hart
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
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandClearRedeems extends Command {

	public CommandClearRedeems() {
		super(AllowedExecutor.BOTH, "clearredeems");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		final String[] args = context.getArgs().toArray(new String[0]);
		if (context.getArgCount() < 2) return ReturnType.INVALID_SYNTAX;

		final boolean clearingAllPlayers = context.getArg(0).equals("*");
		final Player target = Bukkit.getPlayerExact(context.getArg(0));

		if (!clearingAllPlayers)
			if (target == null) {
				Common.tell(context.getSender(), TranslationManager.string(Translations.PLAYER_OFFLINE, "value", context.getArg(0)));
				return ReturnType.FAIL;
			}


		final String voucherId = VoucherHelper.grabWordsUntilFlag(args, 1, "-a").toLowerCase();
		final Voucher voucherFound = Vouchers.getVoucherManager().get(voucherId);

		if (voucherFound == null) {
			Common.tell(context.getSender(), TranslationManager.string(Translations.VOUCHER_NOT_FOUND, "voucher_id", voucherId));
			return ReturnType.FAIL;
		}

		if (clearingAllPlayers) {
			Vouchers.getRedeemManager().deleteAllRedeems(voucherFound.getId());
		} else {
			Vouchers.getRedeemManager().deleteRedeems(target, voucherFound.getId());
		}

		Common.tell(context.getSender(), TranslationManager.string(Translations.REDEEM_HISTORY_CLEARED));
		return ReturnType.SUCCESS;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		return execute(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	protected List<String> tab(CommandContext context) {
		if (context.getArgCount() == 1) {
			final List<String> options = new ArrayList<>(List.of("*"));

			options.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
			return options;
		}

		if (context.getArgCount() == 2)
			return Vouchers.getVoucherManager().getManagerContent().values().stream().map(Voucher::getId).collect(Collectors.toList());

		return null;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return tab(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.clearredeems";
	}

	@Override
	public String getSyntax() {
		return "vouchers clearredeems <player/*> <voucherId>";
	}

	@Override
	public String getDescription() {
		return "Clears redeem history for player(s)";
	}
}