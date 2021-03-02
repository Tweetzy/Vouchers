package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 6:29 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandCreate extends AbstractCommand {

    public CommandCreate() {
        super(CommandType.PLAYER_ONLY, "create");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.create";
    }

    @Override
    public String getSyntax() {
        return "create <name>";
    }

    @Override
    public String getDescription() {
        return "Used to create a new voucher";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }
}
