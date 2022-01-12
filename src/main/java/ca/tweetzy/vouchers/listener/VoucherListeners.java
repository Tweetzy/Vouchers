package ca.tweetzy.vouchers.listener;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 10 2022
 * Time Created: 3:50 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class VoucherListeners implements Listener {

	@EventHandler
	public void onRedeem(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getItem() == null) return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

		final ItemStack possibleVoucher = event.getItem();
		if (!Vouchers.getVoucherManager().isVoucher(possibleVoucher)) return;

		final String voucherId = Vouchers.getVoucherManager().getVoucherId(possibleVoucher);
		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);

		if (voucher == null) return;

		voucher.execute(player, possibleVoucher);
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e) {
		final ItemStack toBePlaced = e.getItemInHand();
		if (toBePlaced.getType() == CompMaterial.AIR.toMaterial()) return;
		if (Vouchers.getVoucherManager().isVoucher(toBePlaced)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCraftAttempt(final PrepareItemCraftEvent e) {
		if (Arrays.stream(e.getInventory().getContents()).anyMatch(item -> Vouchers.getVoucherManager().isVoucher(item))) {
			e.getInventory().setResult(CompMaterial.AIR.toItem());
		}
	}

}
