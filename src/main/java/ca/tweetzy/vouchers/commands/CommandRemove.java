package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherAPI;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:03 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandRemove extends AbstractCommand {

    public CommandRemove() {
        super(CommandType.CONSOLE_OK, "remove");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1) return ReturnType.SYNTAX_ERROR;

        String voucherId = args[0].toLowerCase();

        if (Vouchers.getInstance().getVoucherManager().getVouchers().size() == 0) {
            Vouchers.getInstance().getLocale().getMessage("voucher.novouchers").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!VoucherAPI.getInstance().doesVoucherExists(voucherId)) {
            Vouchers.getInstance().getLocale().getMessage("voucher.invalid").processPlaceholder("voucher_id", voucherId).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        VoucherAPI.getInstance().removeVoucher(voucherId);
        Vouchers.getInstance().getVoucherManager().removeVoucher(Vouchers.getInstance().getVoucherManager().getVoucher(voucherId));
        Vouchers.getInstance().getVoucherManager().loadVouchers(true);
        Vouchers.getInstance().getLocale().getMessage("voucher.remove").processPlaceholder("voucher_id", args[0]).sendPrefixedMessage(sender);
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
        return "vouchers.cmd.remove";
    }

    @Override
    public String getSyntax() {
        return "remove <name>";
    }

    @Override
    public String getDescription() {
        return "Used to delete a voucher";
    }
}
