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

import ca.tweetzy.feather.comp.enums.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class BlockListeners implements Listener {

	@EventHandler
	public void onVoucherCraftAttempt(final PrepareItemCraftEvent event) {
		if (Arrays.stream(event.getInventory().getContents()).anyMatch(item -> Vouchers.getVoucherManager().isVoucher(item))) {
			event.getInventory().setResult(CompMaterial.AIR.parseItem());
		}
	}

	@EventHandler
	public void onVoucherPlaceAttempt(final BlockPlaceEvent event) {
		final ItemStack toBePlaced = event.getItemInHand();
		if (toBePlaced.getType() == CompMaterial.AIR.parseMaterial()) return;

		if (Vouchers.getVoucherManager().isVoucher(toBePlaced))
			event.setCancelled(true);
	}
}
