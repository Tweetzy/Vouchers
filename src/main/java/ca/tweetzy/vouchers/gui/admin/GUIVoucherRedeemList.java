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

package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.redeem.Redeem;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class GUIVoucherRedeemList extends VouchersPagedGUI<Redeem> {

	public GUIVoucherRedeemList(@NonNull final Player player) {
		super(new GUIVouchersAdmin(player), player, "&bVouchers &8> &7Listing Redeems", 6, new ArrayList<>(Vouchers.getRedeemManager().getValues()));
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
