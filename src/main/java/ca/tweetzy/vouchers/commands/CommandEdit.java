package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.configuration.editor.ConfigEditorGui;
import ca.tweetzy.core.configuration.editor.PluginConfigGui;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherAPI;
import ca.tweetzy.vouchers.guis.GUIVoucherEdit;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:03 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandEdit extends AbstractCommand {

    public CommandEdit() {
        super(CommandType.PLAYER_ONLY, "edit");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1) return ReturnType.SYNTAX_ERROR;

        Player player = (Player) sender;
        String voucherId = args[0].toLowerCase();

        if (Vouchers.getInstance().getVoucherManager().getVouchers().size() == 0) {
            Vouchers.getInstance().getLocale().getMessage("voucher.novouchers").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!VoucherAPI.getInstance().doesVoucherExists(voucherId)) {
            Vouchers.getInstance().getLocale().getMessage("voucher.invalid").processPlaceholder("voucher_id", voucherId).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Vouchers.getInstance().getGuiManager().showGUI(player, new GUIVoucherEdit(player, voucherId));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1)
            return Vouchers.getInstance().getVoucherManager().getVouchers().stream().map(Voucher::getId).collect(Collectors.toList());
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.edit";
    }

    @Override
    public String getSyntax() {
        return "edit <voucher>";
    }

    @Override
    public String getDescription() {
        return "Edit a voucher";
    }
}
