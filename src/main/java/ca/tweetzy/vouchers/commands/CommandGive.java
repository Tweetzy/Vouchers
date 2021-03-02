package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:04 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandGive extends AbstractCommand {

    public CommandGive() {
        super(CommandType.CONSOLE_OK, "edit");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        return null;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
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
