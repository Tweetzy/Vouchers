package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.guis.GUIVoucherList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:03 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandList extends AbstractCommand {

    public CommandList() {
        super(CommandType.CONSOLE_OK, "list");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (Vouchers.getInstance().getVoucherManager().getVouchers().size() == 0) {
            Vouchers.getInstance().getLocale().getMessage("voucher.novouchers").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!(sender instanceof Player)) {
            Vouchers.getInstance().getVoucherManager().getVouchers().forEach(voucher -> {
                sender.sendMessage(TextUtils.formatText(String.format("&e%s", voucher.getId())));
            });
        } else {
            Player player = (Player) sender;
            Vouchers.getInstance().getGuiManager().showGUI(player, new GUIVoucherList());
        }
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.create";
    }

    @Override
    public String getSyntax() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List all the available vouchers";
    }
}
