package ca.tweetzy.vouchers.managers;

import ca.tweetzy.core.compatibility.CompatibleHand;
import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.compatibility.XSound;
import ca.tweetzy.core.configuration.editor.ConfigEditorGui;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.nms.ActionBar;
import ca.tweetzy.core.utils.nms.Title;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.events.VoucherRedeemEvent;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 9:11 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

    private final HashMap<String, Voucher> vouchers = new HashMap<>();

    public void addVoucher(Voucher voucher) {
        vouchers.put(voucher.getId(), voucher);
    }

    public void removeVoucher(Voucher voucher) {
        vouchers.remove(voucher);
    }

    public Voucher getVoucher(String id) {
        return vouchers.get(id);
    }

    public Collection<Voucher> getVouchers() {
        return vouchers.values();
    }

    public boolean isLoaded(String id) {
        return vouchers.containsKey(id);
    }

    public void loadVouchers(boolean isReload, boolean useDatabase) {
        if (isReload) {
            vouchers.clear();
            Vouchers.getInstance().getData().load();
        }

        if (useDatabase) {
            Vouchers.getInstance().getDataManager().getVouchers(callback -> callback.forEach(this::addVoucher));
        } else {
            ConfigurationSection section = Vouchers.getInstance().getData().getConfigurationSection("vouchers");
            if (section == null || section.getKeys(false).size() == 0) return;

            long start = System.currentTimeMillis();

            section.getKeys(false).forEach(voucherId -> {
                addVoucher(buildVoucher(voucherId));
            });

            Vouchers.getInstance().getLocale().newMessage(String.format("&a%s &2%d &avoucher(s) in &2%d &ams", isReload ? "Reloaded" : "Loaded", vouchers.keySet().size(), System.currentTimeMillis() - start)).sendPrefixedMessage(Bukkit.getConsoleSender());
        }
    }

    public Voucher buildVoucher(String voucherId) {
        return Voucher.builder()
                .id(voucherId)
                .displayName(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".display name"))
                .permission(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".permission"))
                .material(XMaterial.matchXMaterial(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".material")).get())
                .lore(Vouchers.getInstance().getData().getStringList("vouchers." + voucherId + ".lore"))
                .glowing(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.glowing"))
                .askConfirm(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.ask to confirm"))
                .unbreakable(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.unbreakable"))
                .hideAttributes(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.hide attributes"))
                .removeOnUse(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.remove on use"))
                .sendTitle(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.send title"))
                .sendActionbar(Vouchers.getInstance().getData().getBoolean("vouchers." + voucherId + ".options.send actionbar"))
                .commands(Vouchers.getInstance().getData().getStringList("vouchers." + voucherId + ".execution.commands"))
                .broadcastMessages(Vouchers.getInstance().getData().getStringList("vouchers." + voucherId + ".execution.broadcast messages"))
                .playerMessages(Vouchers.getInstance().getData().getStringList("vouchers." + voucherId + ".execution.player messages"))
                .redeemSound(XSound.matchXSound(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".execution.redeem sound")).get().parseSound())
                .title(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.title"))
                .subTitle(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.subtitle"))
                .actionbarMessage(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.actionbar"))
                .titleFadeIn(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.fade in"))
                .titleStay(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.stay"))
                .titleFadeOut(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.fade out"))
                .build();
    }

    public void redeem(Player player, Voucher voucher) {
        VoucherRedeemEvent voucherRedeemEvent = new VoucherRedeemEvent(player, voucher);
        Bukkit.getServer().getPluginManager().callEvent(voucherRedeemEvent);
        if (voucherRedeemEvent.isCancelled()) return;

        if (voucher.isSendTitle()) {
            Title.send(player, Title.TitleType.TITLE, voucher.getTitle().replace("%voucher_title%", voucher.getDisplayName()).replace("%voucher_id%", voucher.getId()), voucher.getTitleFadeIn() * 20, voucher.getTitleStay() * 20, voucher.getTitleFadeOut() * 20);
            Title.send(player, Title.TitleType.SUBTITLE, voucher.getSubTitle().replace("%voucher_title%", voucher.getDisplayName()).replace("%voucher_id%", voucher.getId()), voucher.getTitleFadeIn() * 20, voucher.getTitleStay() * 20, voucher.getTitleFadeOut() * 20);
        }

        if (voucher.getRedeemSound() != null) {
            player.playSound(player.getLocation(), voucher.getRedeemSound(), 1.0F, 1.0F);
        }

        if (voucher.isSendActionbar()) {
            ActionBar.send(player, voucher.getActionbarMessage().replace("%voucher_title%", voucher.getDisplayName()).replace("%voucher_id%", voucher.getId()));
        }

        if (voucher.getCommands().size() != 0) {
            voucher.getCommands().stream().map(cmd -> cmd.replace("%player%", player.getName())).collect(Collectors.toList()).forEach(cmd -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd));
        }

        if (voucher.getBroadcastMessages().size() != 0) {
            voucher.getBroadcastMessages().stream().map(cmd -> TextUtils.formatText(cmd.replace("%voucher_title%", voucher.getDisplayName()).replace("%voucher_id%", voucher.getId()).replace("%player%", player.getName()))).collect(Collectors.toList()).forEach(Bukkit::broadcastMessage);
        }

        if (voucher.getPlayerMessages().size() != 0) {
            voucher.getPlayerMessages().stream().map(cmd -> TextUtils.formatText(cmd.replace("%voucher_title%", voucher.getDisplayName()).replace("%voucher_id%", voucher.getId()))).collect(Collectors.toList()).forEach(player::sendMessage);
        }

        if (voucher.isRemoveOnUse()) {
            PlayerUtils.takeActiveItem(player, CompatibleHand.MAIN_HAND, 1);
        }
    }
}
