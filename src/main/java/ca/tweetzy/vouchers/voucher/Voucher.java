package ca.tweetzy.vouchers.voucher;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.TextUtils;
import ca.tweetzy.core.utils.nms.NBTEditor;
import ca.tweetzy.vouchers.Helpers;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 7:03 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */

@Getter
@Setter
@Builder
public class Voucher implements Serializable {

    private final static long serialVersionUID = 1L;

    private String id;
    private String displayName;
    private List<String> lore;
    private String permission;

    private boolean glowing;
    private boolean askConfirm;
    private boolean unbreakable;
    private boolean hideAttributes;
    private boolean removeOnUse;
    private boolean sendTitle;
    private boolean sendActionbar;

    private List<String> commands;
    private List<String> broadcastMessages;
    private List<String> playerMessages;

    private String actionbarMessage;
    private String title;
    private String subTitle;
    private int titleFadeIn;
    private int titleStay;
    private int titleFadeOut;

    private Sound redeemSound;
    private int cooldown;

    private XMaterial material;

    public ItemStack getItem(int amount, boolean itemToGive) {
        ItemStack item = this.material.parseItem();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) meta = Bukkit.getItemFactory().getItemMeta(this.material.parseMaterial());
        if (!this.displayName.isEmpty()) meta.setDisplayName(TextUtils.formatText(this.displayName));
        if (this.lore != null) meta.setLore(this.lore.stream().map(TextUtils::formatText).collect(Collectors.toList()));

        if (this.glowing) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (this.hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        }

        item.setItemMeta(meta);
        item = NBTEditor.set(item, id, "tweetzy:voucher:id");
        if (itemToGive) {
            item = NBTEditor.set(item, Helpers.toString(this), "tweetzy:voucher:voucher");
        }

        if (this.unbreakable) {
            item = NBTEditor.set(item, (byte) 1, "Unbreakable");
        }
        return item;
    }

}
