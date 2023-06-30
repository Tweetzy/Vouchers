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
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class CommandReload extends Command {

	public CommandReload() {
		super(AllowedExecutor.BOTH, "reload");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		Settings.init();
		Translations.init();
		Common.setPrefix(Settings.PREFIX.getStringOr("<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8»"));
		tell(sender, "&aSuccessfully reloaded all configuration files.");

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.import";
	}

	@Override
	public String getSyntax() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}
}
