/*
 * Vouchers
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.listeners;

import ca.tweetzy.feather.comp.NBTEditor;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.GUIConfirm;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class VoucherListeners implements Listener {

	private final List<UUID> blockedFromDrop = new ArrayList<>();

	@EventHandler
	public void onVoucherRedeem(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getItem();

		if (item == null) return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			// not even a voucher
			if (!Vouchers.getVoucherManager().isVoucher(item)) return;

			final Voucher voucher = Vouchers.getVoucherManager().find(NBTEditor.getString(item, "Tweetzy:Vouchers"));
			final String voucherArgsRaw = NBTEditor.getString(item, "Tweetzy:VouchersArgs");

			final List<String> voucherArgs = voucherArgsRaw == null ? null : voucherArgsRaw.split(" ").length == 0 ? null : List.of(voucherArgsRaw.split(" "));

			// invalid / deleted voucher
			if (voucher == null) return;

			event.setUseItemInHand(Event.Result.DENY);

			if (!this.blockedFromDrop.contains(player.getUniqueId()))
				this.blockedFromDrop.add(player.getUniqueId());

			if (voucher.getOptions().isAskConfirm()) {
				Vouchers.getGuiManager().showGUI(player, new GUIConfirm(confirmed -> {
					if (confirmed) {
						if (voucherArgs == null)
							Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
						else Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false, voucherArgs);
					}

					player.closeInventory();
					this.blockedFromDrop.remove(player.getUniqueId());
				}, fail -> this.blockedFromDrop.remove(player.getUniqueId())));
			} else {
				if (voucherArgs == null)
					Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
				else Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false, voucherArgs);
			}
		}
	}

	@EventHandler
	public void onVoucherDropAttempt(final PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		if (!this.blockedFromDrop.contains(player.getUniqueId())) return;

		final ItemStack item = event.getItemDrop().getItemStack();
		if (item == null) return;

		if (Vouchers.getVoucherManager().isVoucher(item))
			event.setCancelled(true);
	}

	@EventHandler
	public void onHandSwapWithVoucher(final PlayerSwapHandItemsEvent event) {
		final ItemStack itemMain = event.getMainHandItem();
		final ItemStack itemOff = event.getOffHandItem();

		if ((itemMain != null && Vouchers.getVoucherManager().isVoucher(itemMain)) || (itemOff != null && Vouchers.getVoucherManager().isVoucher(itemOff))) {
			event.setCancelled(true);
		}
	}
}
