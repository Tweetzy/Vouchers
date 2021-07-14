package ca.tweetzy.vouchers.listener;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.nms.NBTEditor;
import ca.tweetzy.vouchers.Helpers;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.guis.GUIConfirm;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 06 2021
 * Time Created: 3:29 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack toBePlaced = e.getItemInHand();
        if (toBePlaced.getType() == XMaterial.AIR.parseMaterial()) return;
        if (NBTEditor.contains(toBePlaced, "tweetzy:voucher:id")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftAttempt(PrepareItemCraftEvent e) {
        if (Arrays.stream(e.getInventory().getContents()).anyMatch(item -> NBTEditor.contains(item, "tweetzy:voucher:id"))) {
            e.getInventory().setResult(XMaterial.AIR.parseItem());
        }
    }

    @EventHandler
    public void onPlayerClick(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        ItemStack heldItem = Helpers.getHeldItem(player);

        if (heldItem == null)
            return;
        if (heldItem.getType() == XMaterial.AIR.parseMaterial()) return;
        if (!NBTEditor.contains(heldItem, "tweetzy:voucher:id")) return;

        String voucherId = NBTEditor.getString(heldItem, "tweetzy:voucher:id");
        // if there is a voucher loaded into the list, use that one instead of the one stored in nbt data
        Voucher voucher = Vouchers.getInstance().getVoucherManager().isLoaded(voucherId) ? Vouchers.getInstance().getVoucherManager().getVoucher(voucherId) : (Voucher) Helpers.fromString(NBTEditor.getString(heldItem, "tweetzy:voucher:voucher"));

        handleRedeem(player, voucher);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = Helpers.getHeldItem(player);

        if (heldItem == null || e.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if (heldItem.getType() == XMaterial.AIR.parseMaterial()) return;
        if (!NBTEditor.contains(heldItem, "tweetzy:voucher:id")) return;

        String voucherId = NBTEditor.getString(heldItem, "tweetzy:voucher:id");
        // if there is a voucher loaded into the list, use that one instead of the one stored in nbt data
        Voucher voucher = Vouchers.getInstance().getVoucherManager().isLoaded(voucherId) ? Vouchers.getInstance().getVoucherManager().getVoucher(voucherId) : (Voucher) Helpers.fromString(NBTEditor.getString(heldItem, "tweetzy:voucher:voucher"));

        handleRedeem(player, voucher);
    }

    private void handleRedeem(Player player, Voucher voucher) {
        if (!voucher.getPermission().isEmpty() && !player.hasPermission(voucher.getPermission())) {
            Vouchers.getInstance().getLocale().getMessage("voucher.nopermission").sendPrefixedMessage(player);
            return;
        }

        if (Vouchers.getInstance().getVoucherManager().isPlayerInCoolDown(player.getUniqueId())) {
            if (Vouchers.getInstance().getVoucherManager().isPlayerInCoolDownForVoucher(player.getUniqueId(), voucher)) {
                long coolDownTime = Vouchers.getInstance().getVoucherManager().getCoolDownTime(player.getUniqueId(), voucher);
                if (System.currentTimeMillis() < coolDownTime) {
                    Vouchers.getInstance().getLocale().getMessage("general.cooldown").processPlaceholder("remaining_time", String.format("%,.2f", (coolDownTime - System.currentTimeMillis()) / 1000F)).sendPrefixedMessage(player);
                    return;
                }
            }
        }

        if (voucher.isAskConfirm()) {
            Vouchers.getInstance().getGuiManager().showGUI(player, new GUIConfirm(voucher));
        } else {
            Vouchers.getInstance().getVoucherManager().redeem(player, voucher);
            Vouchers.getInstance().getVoucherManager().addPlayerToCoolDown(player.getUniqueId(), voucher);
        }
    }
}
