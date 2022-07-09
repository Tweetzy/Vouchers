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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class VoucherListeners implements Listener {

	@EventHandler
	public void onVoucherRedeem(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getItem();

		if (item == null) return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// not even a voucher
			if (!Vouchers.getVoucherManager().isVoucher(item)) return;

			final Voucher voucher = Vouchers.getVoucherManager().find(NBTEditor.getString(item, "Tweetzy:Vouchers"));

			// invalid / deleted voucher
			if (voucher == null) return;

			if (voucher.getOptions().isAskConfirm()) {
				Vouchers.getGuiManager().showGUI(player, new GUIConfirm(confirmed -> {
					if (confirmed) {
						Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
					}
					player.closeInventory();
				}));
			} else {
				Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
			}
		}
	}

}
