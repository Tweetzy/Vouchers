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
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.model.manager.VoucherManager;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class CommandSyncFiles extends Command {

	public CommandSyncFiles() {
		super(AllowedExecutor.BOTH, "syncfiles");
	}

	@Override
	protected ReturnType execute(CommandContext context) {
		final AtomicReference<VoucherManager.SyncFromDiskResult> resultRef = new AtomicReference<>();

		Vouchers.newChain()
				.async(() -> resultRef.set(Vouchers.getVoucherManager().syncFromDisk()))
				.sync(() -> {
					final VoucherManager.SyncFromDiskResult r = resultRef.get();
					if (r == null) {
						tell(context.getSender(), "&cSync failed unexpectedly.");
						return;
					}
					tell(context.getSender(), "&aSynced voucher files: &f%d &aupdated, &f%d &aremoved, &f%d &afailed to load."
							.formatted(r.updated(), r.removed(), r.failed()));
					tell(context.getSender(), "&7(&f/vouchers reload &7only reloads settings and language; it does &cnot &7reload voucher JSON from disk.)");
				})
				.execute();

		return ReturnType.SUCCESS;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		return execute(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	protected List<String> tab(CommandContext context) {
		return null;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return tab(new CommandContext(sender, args, getSubCommands().get(0)));
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.syncfiles";
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
