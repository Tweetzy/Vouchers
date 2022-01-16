package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.annotation.AutoRegister;
import ca.tweetzy.tweety.command.SimpleCommandGroup;
import ca.tweetzy.vouchers.menu.MenuVoucherList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 28 2021
 * Time Created: 1:27 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VouchersCommand extends SimpleCommandGroup {

	@Getter
	private static final VouchersCommand instance = new VouchersCommand();

	@Override
	protected void zeroArgActions(CommandSender sender) {
		if (!(sender instanceof Player)) return;
		final Player player = (Player) sender;
		new MenuVoucherList().displayTo(player);
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CommandCreate());
		registerSubcommand(new CommandDelete());
		registerSubcommand(new CommandEdit());
		registerSubcommand(new CommandGive());
		registerSubcommand(new CommandImport());
		registerSubcommand(new CommandReload());
	}

	@Override
	protected boolean useZeroArgAction() {
		return true;
	}
}
