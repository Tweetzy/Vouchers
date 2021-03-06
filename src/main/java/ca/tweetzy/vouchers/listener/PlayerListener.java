package ca.tweetzy.vouchers.listener;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.utils.PlayerUtils;
import ca.tweetzy.core.utils.nms.NBTEditor;
import ca.tweetzy.vouchers.Helpers;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 06 2021
 * Time Created: 3:29 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = Helpers.getHeldItem(player);

        if (heldItem == null || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        if (heldItem.getType() == XMaterial.AIR.parseMaterial()) return;
        if (!NBTEditor.contains(heldItem, "tweetzy:voucher:id")) return;

        String voucherId = NBTEditor.getString(heldItem, "tweetzy:voucher:id");
        // if there is a voucher loaded into the list, use that one instead of the one stored in nbt data
        Voucher voucher = Vouchers.getInstance().getVoucherManager().isLoaded(voucherId) ? Vouchers.getInstance().getVoucherManager().getVoucher(voucherId) : (Voucher) Helpers.fromString(NBTEditor.getString(heldItem, "tweetzy:voucher:voucher"));
        Vouchers.getInstance().getCooldownManager().add(player.getUniqueId(), voucher);
        Vouchers.getInstance().getVoucherManager().redeem(player, voucher);
    }
}
