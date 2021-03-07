package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.configuration.editor.PluginConfigGui;
import ca.tweetzy.vouchers.Vouchers;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 06 2021
 * Time Created: 11:31 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandSettings extends AbstractCommand {

    public CommandSettings() {
        super(CommandType.PLAYER_ONLY, "settings");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;
        Vouchers.getInstance().getGuiManager().showGUI(player, new PluginConfigGui(Vouchers.getInstance(), Vouchers.getInstance().getLocale().getMessage("general.prefix").getMessage()));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.settings";
    }

    @Override
    public String getSyntax() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return "Open the settings GUI";
    }
}
