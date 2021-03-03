package ca.tweetzy.vouchers.guis;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.items.TItemBuilder;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:04 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUIConfirm extends Gui {

    public GUIConfirm(Player player, Voucher voucher) {
        setTitle(TextUtils.formatText(Settings.GUI_CONFIRM_TITLE.getString()));
        setRows(1);
        setAcceptsItems(false);
        setAllowClose(false);
        setAllowDrops(false);

        for (int i = 0; i <= 3; i++) {
            setButton(i, new TItemBuilder(Objects.requireNonNull(XMaterial.matchXMaterial(Settings.GUI_CONFIRM_YES_MATERIAL.getString()).get().parseMaterial())).setName(Settings.GUI_CONFIRM_YES_NAME.getString()).setLore(Settings.GUI_CONFIRM_YES_LORE.getStringList().stream().map(TextUtils::formatText).collect(Collectors.toList())).toItemStack(), (e) -> {

            });
        }

        setItem(4, voucher.getItem(1));

        for (int i = 5; i <= 8; i++) {
            setButton(i, new TItemBuilder(Objects.requireNonNull(XMaterial.matchXMaterial(Settings.GUI_CONFIRM_NO_MATERIAL.getString()).get().parseMaterial())).setName(Settings.GUI_CONFIRM_NO_NAME.getString()).setLore(Settings.GUI_CONFIRM_NO_LORE.getStringList().stream().map(TextUtils::formatText).collect(Collectors.toList())).toItemStack(), (e) -> {

            });
        }
    }
}
