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

import ca.tweetzy.feather.command.AllowedExecutor;
import ca.tweetzy.feather.command.Command;
import ca.tweetzy.feather.command.ReturnType;
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.settings.Locale;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class CommandGive extends Command {

	public CommandGive() {
		super(AllowedExecutor.BOTH, "give");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length == 0) {
			return ReturnType.SUCCESS;
		}

		final boolean isGivingAll = args[0].equals("*");

		final Player target = Bukkit.getPlayerExact(args[0]);

		if (!isGivingAll)
			if (target == null) {
				Common.tell(sender, Locale.PLAYER_OFFLINE.getString());
				return ReturnType.FAIL;
			}

		int amount = 1;

		if (args.length > 1) {
			if (NumberUtils.isNumber(args[1]))
				amount = Integer.parseInt(args[1]);
		}

		// check for flags
		final String voucher = CommandFlag.get(String.class, "voucher", null, args);
		if (voucher == null)
			return ReturnType.FAIL;

		final Voucher voucherFound = Vouchers.getVoucherManager().find(voucher);
		if (voucherFound == null) return ReturnType.FAIL;

		if (isGivingAll)
			for (Player player : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < amount; i++)
					player.getInventory().addItem(voucherFound.buildItem());
			}
		else {
			for (int i = 0; i < amount; i++)
				target.getInventory().addItem(voucherFound.buildItem());
		}

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.give";
	}

	@Override
	public String getSyntax() {
		return "<player/*> [#] -voucher <voucherId>";
	}

	@Override
	public String getDescription() {
		return "Gives select users a voucher";
	}
}
