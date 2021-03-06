package ca.tweetzy.vouchers.guis;


import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.gui.Gui;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.items.TItemBuilder;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.voucher.Voucher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:04 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class GUIVoucherList extends Gui {

    private final List<Voucher> vouchers = new ArrayList<>();

    public GUIVoucherList() {
        setTitle(TextUtils.formatText(Settings.GUI_LIST_TITLE.getString()));
        setRows(6);
        setAcceptsItems(false);
        setUseLockedCells(false);
        vouchers.addAll(Vouchers.getInstance().getVoucherManager().getVouchers());
        draw();
    }

    private void draw() {
        reset();
        pages = (int) Math.max(1, Math.ceil(vouchers.size() / ((double) 45)));

        setItems(45, 53, XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());
        setButton(5, 3, new TItemBuilder(Objects.requireNonNull(XMaterial.matchXMaterial(Settings.GUI_BACK_BTN_ITEM.getString()).orElse(XMaterial.ARROW).parseMaterial())).setName(Settings.GUI_BACK_BTN_NAME.getString()).setLore(Settings.GUI_BACK_BTN_LORE.getStringList()).toItemStack(), e -> e.gui.prevPage());
        setButton(5, 4, new TItemBuilder(Objects.requireNonNull(XMaterial.matchXMaterial(Settings.GUI_CLOSE_BTN_ITEM.getString()).orElse(XMaterial.BARRIER).parseMaterial())).setName(Settings.GUI_CLOSE_BTN_NAME.getString()).setLore(Settings.GUI_CLOSE_BTN_LORE.getStringList()).toItemStack(), e -> e.player.closeInventory());
        setButton(5, 5, new TItemBuilder(Objects.requireNonNull(XMaterial.matchXMaterial(Settings.GUI_BACK_BTN_ITEM.getString()).orElse(XMaterial.ARROW).parseMaterial())).setName(Settings.GUI_NEXT_BTN_NAME.getString()).setLore(Settings.GUI_NEXT_BTN_LORE.getStringList()).toItemStack(), e -> e.gui.nextPage());
        setOnPage((event) -> draw());

        List<Voucher> data = Vouchers.getInstance().getVoucherManager().getVouchers().stream().sorted(Comparator.comparing(Voucher::getId)).skip((page - 1) * 45L).limit(45).collect(Collectors.toList());

        int slot = 0;
        for (Voucher voucher : data) {
            setItem(slot, voucher.getItem(1, false));
            slot++;
        }
    }
}
