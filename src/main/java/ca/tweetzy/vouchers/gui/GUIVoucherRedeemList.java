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

package ca.tweetzy.vouchers.gui;

import ca.tweetzy.feather.comp.enums.CompMaterial;
import ca.tweetzy.feather.gui.events.GuiClickEvent;
import ca.tweetzy.feather.gui.helper.InventoryBorder;
import ca.tweetzy.feather.gui.template.PagedGUI;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Redeem;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class GUIVoucherRedeemList extends PagedGUI<Redeem> {

	public GUIVoucherRedeemList() {
		super(new GUIVouchersAdmin(), "&bVouchers &8> &7Listing Redeems", 6, Vouchers.getRedeemManager().getAll());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Redeem redeem) {
		final Voucher voucher = Vouchers.getVoucherManager().find(redeem.getVoucherId());
		final SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("MMMM dd, yyyy - hh:mm:ss aa");

		return QuickItem
				.of(voucher != null ? voucher.getItem() : CompMaterial.PAPER.parseItem())
				.name(redeem.getVoucherId() + " &8> &7Redeemed")
				.lore(
						"",
						"&7Redeemer&f: &b" + Bukkit.getOfflinePlayer(redeem.getUser()).getName(),
						"&7Time&f: &b" + format.format(new Date(redeem.getTime()))
				)
				.make();
	}

	@Override
	protected void onClick(Redeem object, GuiClickEvent clickEvent) {

	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
