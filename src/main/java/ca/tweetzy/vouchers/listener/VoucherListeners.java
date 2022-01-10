package ca.tweetzy.vouchers.listener;

import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

		final ItemStack possibleVoucher = event.getItem();
		if (!CompMetadata.hasMetadata(possibleVoucher, "Tweetzy:Vouchers")) return;

		final String voucherId = CompMetadata.getMetadata(possibleVoucher, "Tweetzy:Vouchers");
		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);

		if (voucher == null) return;

		voucher.execute(player, possibleVoucher);
	}
}
