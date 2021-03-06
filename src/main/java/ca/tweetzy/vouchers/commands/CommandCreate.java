package ca.tweetzy.vouchers.commands;

import ca.tweetzy.core.commands.AbstractCommand;
import ca.tweetzy.core.compatibility.XSound;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VoucherAPI;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 6:29 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CommandCreate extends AbstractCommand {

    public CommandCreate() {
        super(CommandType.CONSOLE_OK, "create");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1) return ReturnType.SYNTAX_ERROR;

        if (VoucherAPI.getInstance().doesVoucherExists(args[0])) {
            Vouchers.getInstance().getLocale().getMessage("voucher.exists").processPlaceholder("voucher_id", args[0]).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Voucher voucher = Voucher.builder()
                .id(args[0])
                .material(Settings.DEFAULT_MATERIAL.getMaterial())
                .displayName(Settings.DEFAULT_DISPLAYNAME.getString().replace("%voucher_id%", args[0]))
                .permission(Settings.DEFAULT_PERMISSION.getString().replace("%voucher_id%", args[0]))
                .lore(Settings.DEFAULT_LORE.getStringList())
                .glowing(Settings.DEFAULT_GLOW.getBoolean())
                .askConfirm(Settings.DEFAULT_ASK_TO_CONFIRM.getBoolean())
                .unbreakable(Settings.DEFAULT_UNBREAKABLE.getBoolean())
                .hideAttributes(Settings.DEFAULT_HIDE_ATTRIBUTES.getBoolean())
                .removeOnUse(Settings.DEFAULT_REMOVE_ON_USE.getBoolean())
                .sendTitle(Settings.DEFAULT_SEND_TITLE.getBoolean())
                .sendActionbar(Settings.DEFAULT_SEND_ACTIONBAR.getBoolean())
                .commands(Settings.DEFAULT_COMMANDS.getStringList())
                .broadcastMessages(Settings.DEFAULT_BROADCAST_MESSAGES.getStringList())
                .playerMessages(Settings.DEFAULT_PLAYER_MESSAGES.getStringList())
                .actionbarMessage(Settings.DEFAULT_ACTIONBAR.getString())
                .title(Settings.DEFAULT_TITLE.getString())
                .subTitle(Settings.DEFAULT_SUBTITLE.getString())
                .titleFadeIn(Settings.DEFAULT_TITLE_FADE_IN.getInt())
                .titleStay(Settings.DEFAULT_TITLE_STAY.getInt())
                .titleFadeOut(Settings.DEFAULT_TITLE_FADE_OUT.getInt())
                .redeemSound(XSound.matchXSound(Settings.DEFAULT_REDEEM_SOUND.getString()).get().parseSound())
                .build();

        VoucherAPI.getInstance().createVoucher(voucher);
        Vouchers.getInstance().getVoucherManager().addVoucher(voucher);
        Vouchers.getInstance().getLocale().getMessage("voucher.create").processPlaceholder("voucher_id", args[0]).sendPrefixedMessage(sender);
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
