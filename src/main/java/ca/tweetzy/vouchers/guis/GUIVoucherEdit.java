package ca.tweetzy.vouchers.guis;

import ca.tweetzy.core.configuration.editor.ConfigEditorGui;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 06 2021
 * Time Created: 4:19 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUIVoucherEdit extends ConfigEditorGui {

    public GUIVoucherEdit(Player player, String voucherId, Gui parent) {
        super(player, Vouchers.getInstance(), parent, "data.yml", "&eEditing " + voucherId, Vouchers.getInstance().getLocale().getMessage("general.prefix").getMessage(), Vouchers.getInstance().getData(), Objects.requireNonNull(Vouchers.getInstance().getData().getConfigurationSection("vouchers." + voucherId)));

        // override the default save process, to include the voucher reload
        if (!(this.getParent() instanceof ConfigEditorGui)) {
            this.setOnClose(e -> {
                this.save(Vouchers.getInstance().getLocale().getMessage("general.prefix").getMessage());
                Vouchers.getInstance().getVoucherManager().loadVouchers(true, Settings.DATABASE_USE.getBoolean());
            });
        } else {
            this.setOnClose(e -> ((ConfigEditorGui) this.getParent()).edits |= this.edits);
        }
    }

    public GUIVoucherEdit(Player player, String voucherId) {
        this(player, voucherId, null);
    }
}
