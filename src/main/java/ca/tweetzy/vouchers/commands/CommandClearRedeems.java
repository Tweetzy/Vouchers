/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.model.FlagExtractor;
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
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 2) return ReturnType.INVALID_SYNTAX;

		final boolean clearingAllPlayers = args[0].equals("*");
		final Player target = Bukkit.getPlayerExact(args[0]);

		if (!clearingAllPlayers)
			if (target == null) {
				Common.tell(sender, TranslationManager.string(Translations.PLAYER_OFFLINE, "value", args[0]));
				return ReturnType.FAIL;
			}


		final String voucherId = FlagExtractor.grabWordsUntilFlag(args, 1, "-a");
		final Voucher voucherFound = Vouchers.getVoucherManager().find(voucherId);

		if (voucherFound == null) {
			Common.tell(sender, TranslationManager.string(Translations.VOUCHER_NOT_FOUND, "voucher_id", voucherId));
			return ReturnType.FAIL;
		}

		if (clearingAllPlayers) {
			Vouchers.getRedeemManager().deleteAllRedeems(voucherFound.getId());
		} else {
			Vouchers.getRedeemManager().deleteRedeems(target, voucherFound.getId());
		}

		Common.tell(sender, TranslationManager.string(Translations.REDEEM_HISTORY_CLEARED));
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		if (args.length == 1) {
			final List<String> options = new ArrayList<>(List.of("*"));

			options.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
			return options;
		}

		if (args.length == 2)
			return Vouchers.getVoucherManager().getAll().stream().map(Voucher::getId).collect(Collectors.toList());

		return null;
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
