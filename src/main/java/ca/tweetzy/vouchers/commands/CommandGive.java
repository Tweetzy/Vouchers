package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.utils.NumberUtils;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherAPI;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:04 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandGive extends AbstractCommand {

    public CommandGive() {
        super(CommandType.CONSOLE_OK, "give");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2) return ReturnType.FAILURE;

        String voucherId = args[0].toLowerCase();
        String target = args[1];
        int amountToGive = 1;

        if (Vouchers.getInstance().getVoucherManager().getVouchers().size() == 0) {
            Vouchers.getInstance().getLocale().getMessage("voucher.novouchers").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!VoucherAPI.getInstance().doesVoucherExists(voucherId)) {
            Vouchers.getInstance().getLocale().getMessage("voucher.invalid").processPlaceholder("voucher_id", voucherId).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (args.length == 3) {
            if (!NumberUtils.isInt(args[2])) {
                Vouchers.getInstance().getLocale().getMessage("voucher.notanumber").processPlaceholder("value", args[2]).sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            amountToGive = Integer.parseInt(args[2]);
        }

        giveVoucher(sender, target, voucherId, amountToGive);
        return ReturnType.SUCCESS;
    }

    private ReturnType giveVoucher(CommandSender sender, String target, String voucherId, int amt) {
        if (target.equalsIgnoreCase("all")) {
            Vouchers.getInstance().getLocale().getMessage("voucher.giveall").processPlaceholder("voucher_id", voucherId).processPlaceholder("amount", amt).sendPrefixedMessage(sender);
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                PlayerUtils.giveItem(player, Vouchers.getInstance().getVoucherManager().getVoucher(voucherId).getItem(amt, true));
                Vouchers.getInstance().getLocale().getMessage("voucher.received").processPlaceholder("voucher_id", voucherId).processPlaceholder("amount", amt).sendPrefixedMessage(player);
            });
        } else {
            Player targetedPlayer = PlayerUtils.findPlayer(target);
            if (targetedPlayer == null) {
                Vouchers.getInstance().getLocale().getMessage("general.playeroffline").processPlaceholder("player", target).sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }

            PlayerUtils.giveItem(targetedPlayer, Vouchers.getInstance().getVoucherManager().getVoucher(voucherId).getItem(amt, true));
            Vouchers.getInstance().getLocale().getMessage("voucher.received").processPlaceholder("voucher_id", voucherId).processPlaceholder("amount", amt).sendPrefixedMessage(targetedPlayer);
        }
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1)
            return Vouchers.getInstance().getVoucherManager().getVouchers().stream().map(Voucher::getId).collect(Collectors.toList());
        if (args.length == 2) {
            List<String> options = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            options.add(0, "all");
            return options;
        }

        if (args.length == 3) return Arrays.asList("1", "2", "3", "4", "5");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "vouchers.cmd.give";
    }

    @Override
    public String getSyntax() {
        return "give <voucher> <all|player> [#]";
    }

    @Override
    public String getDescription() {
        return "Used to give a voucher";
    }
}
