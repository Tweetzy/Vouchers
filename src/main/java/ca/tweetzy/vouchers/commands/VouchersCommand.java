package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.vouchers.Vouchers;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 21 2021
 * Time Created: 3:20 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class VouchersCommand extends AbstractCommand {

	public VouchersCommand() {
		super(CommandType.CONSOLE_OK, "vouchers");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		sender.sendMessage(TextUtils.formatText("&e&lVouchers"));
		Vouchers.getInstance().getCommandManager().getAllCommands().forEach(command -> {
			if (command.getSyntax() != null && !command.getSyntax().equalsIgnoreCase("/vouchers")) {
				if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
					sender.sendMessage(TextUtils.formatText(String.format("&8- &e/vouchers %s &7- %s", command.getSyntax(), command.getDescription())));
				}
			}

		});
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.cmd";
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
