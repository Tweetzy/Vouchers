package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.configuration.editor.ConfigEditorGui;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 08 2021
 * Time Created: 7:13 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandReload extends AbstractCommand {

    public CommandReload() {
        super(CommandType.CONSOLE_OK, "reload");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length == 0) {
            sender.sendMessage(TextUtils.formatText("&4&lReloading may cause problems, if editing files directly (not using in game editor), please reload/restart the server!"));
            return ReturnType.SYNTAX_ERROR;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            sender.sendMessage(TextUtils.formatText("&4&lReloading may cause problems, if editing files directly (not using in game editor), please reload/restart the server!"));
            Vouchers.getInstance().reloadConfig();
            Vouchers.getInstance().getLocale().getMessage("voucher.reload").sendPrefixedMessage(sender);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.reload";
    }

    @Override
    public String getSyntax() {
        return "reload [confirm]";
    }

    @Override
    public String getDescription() {
        return "Used to reload vouchers";
    }
}
