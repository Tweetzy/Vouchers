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
import ca.tweetzy.vouchers.model.Giver;
import ca.tweetzy.vouchers.settings.Translations;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class CommandGive extends Command {

	public CommandGive() {
		super(AllowedExecutor.BOTH, "give");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 3) return ReturnType.INVALID_SYNTAX;

		final boolean isGivingAll = args[0].equals("*");

		final Player target = Bukkit.getPlayerExact(args[0]);

		if (!isGivingAll)
			if (target == null) {
				Common.tell(sender, TranslationManager.string(Translations.PLAYER_OFFLINE, "value", args[0]));
				return ReturnType.FAIL;
			}

		int amount = 1;

		if (NumberUtils.isNumber(args[1]))
			amount = Integer.parseInt(args[1]);

		final String voucherId = FlagExtractor.grabWordsUntilFlag(args, 2, "-a");
		final Voucher voucherFound = Vouchers.getVoucherManager().find(voucherId);

		if (voucherFound == null) {
			Common.tell(sender, TranslationManager.string(Translations.VOUCHER_NOT_FOUND, "voucher_id", voucherId));
			return ReturnType.FAIL;
		}

		final List<String> optionalArgs = FlagExtractor.grabFlagArguments(args, "-a");

		if (isGivingAll)
			for (Player player : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < amount; i++)
					Giver.giveItem(player, optionalArgs.isEmpty() ? voucherFound.buildItem(player) : voucherFound.buildItem(player, optionalArgs), true);
			}
		else {
			for (int i = 0; i < amount; i++)
				Giver.giveItem(target, optionalArgs.isEmpty() ? voucherFound.buildItem(target) : voucherFound.buildItem(target, optionalArgs), true);
		}

		return ReturnType.SUCCESS;

	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		if (args.length == 1) {
			final List<String> options = new java.util.ArrayList<>(List.of("*"));

			options.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
			return options;
		}

		if (args.length == 2)
			return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

		if (args.length == 3)
			return Vouchers.getVoucherManager().getManagerContent().values().stream().map(Voucher::getId).collect(Collectors.toList());

		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.give";
	}

	@Override
	public String getSyntax() {
		return "<player/*> <#> <voucherId> [args]";
	}

	@Override
	public String getDescription() {
		return "Gives select users a voucher";
	}
}
