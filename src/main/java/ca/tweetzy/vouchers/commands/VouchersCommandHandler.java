package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import revxrsal.commands.bukkit.BukkitCommandHandler;

/**
 * Date Created: February 27 2022
 * Time Created: 11:14 p.m.
 *
 * @author Kiran Hart
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VouchersCommandHandler {

	@Getter
	private static final VouchersCommandHandler instance = new VouchersCommandHandler();

	private final BukkitCommandHandler commandHandler = BukkitCommandHandler.create(Vouchers.getInstance());

	public void register() {
		commandHandler.setMessagePrefix(Common.colorize(Settings.PREFIX.getString() + " "));
		commandHandler.register(new VouchersCommand());
	}
}
