package ca.tweetzy.vouchers.managers;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.compatibility.XSound;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 9:11 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

    private final HashMap<String, Voucher> vouchers = new HashMap<>();

    public Voucher addVoucher(Voucher voucher) {
        return vouchers.put(voucher.getId(), voucher);
    }

    public Voucher removeVoucher(Voucher voucher) {
        return vouchers.remove(voucher);
    }

    public Voucher getVoucher(String id) {
        return vouchers.get(id);
    }

    public Collection<Voucher> getVouchers() {
        return vouchers.values();
    }

    public void loadVouchers(boolean isReload) {
        if (isReload) vouchers.clear();

        ConfigurationSection section = Vouchers.getInstance().getData().getConfigurationSection("vouchers");
        if (section == null || section.getKeys(false).size() == 0) return;

        long start = System.currentTimeMillis();

        section.getKeys(false).forEach(voucherId -> {
            Voucher voucher = Voucher.builder()
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
                    .cooldown(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".execution.cool down"))
                    .title(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.title"))
                    .subTitle(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.subtitle"))
                    .actionbarMessage(Vouchers.getInstance().getData().getString("vouchers." + voucherId + ".titles.actionbar"))
                    .titleFadeIn(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.fade in"))
                    .titleStay(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.stay"))
                    .titleFadeOut(Vouchers.getInstance().getData().getInt("vouchers." + voucherId + ".titles.fade out"))
                    .build();

            addVoucher(voucher);
        });

        Vouchers.getInstance().getLocale().newMessage(String.format("&a%s &2%d &avoucher(s) in &2%d &ams", isReload ? "Reloaded" : "Loaded", vouchers.keySet().size(), System.currentTimeMillis() - start)).sendPrefixedMessage(Bukkit.getConsoleSender());
    }
}
