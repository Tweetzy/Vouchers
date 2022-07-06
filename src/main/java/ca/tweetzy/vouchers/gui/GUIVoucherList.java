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
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.feather.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.ActiveVoucher;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.settings.Locale;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class GUIVoucherList extends PagedGUI<Voucher> {

	public GUIVoucherList() {
		super(new GUIVouchersAdmin(), "&bVouchers &8> &7Listing Vouchers", 6, Vouchers.getVoucherManager().getAll());
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Voucher voucher) {
		return QuickItem
				.of(voucher.getItem())
				.name(voucher.getName())
				.lore(voucher.getDescription())
				.lore(
						"",
						"&b&lLeft Click &8» &7To take voucher",
						"&a&lRight Click &8» &7To edit voucher",
						"&c&lPress 1 &8» &7To delete voucher"
				)
				.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL).name("&a&lNew Voucher").lore(
				"&b&lClick &8» &7To create new voucher"
		).make(), click -> new TitleInput(click.player, "&b&lVouchers", "&fEnter id for voucher into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, new GUIVoucherList());
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.toLowerCase());
				if (Vouchers.getVoucherManager().find(string) != null) {
					Common.tell(click.player, Locale.VOUCHER_EXISTS_ALREADY.getString());
					return false;
				}

				final Voucher voucher = new ActiveVoucher(string, "&e" + string, CompMaterial.PAPER.parseItem(), List.of("&7Sample Lore"), new VoucherSettings(), new ArrayList<>());

				Vouchers.getDataManager().createVoucher(voucher, (error, created) -> {
					if (error == null) {
						Vouchers.getVoucherManager().add(created);
						click.manager.showGUI(click.player, new GUIVoucherList());
					}
				});

				return true;
			}
		});
	}

	@Override
	protected void onClick(Voucher voucher, GuiClickEvent clickEvent) {
		if (clickEvent.clickType == ClickType.NUMBER_KEY)
			Vouchers.getDataManager().deleteVoucher(voucher.getId(), (error, deleted) -> {
				if (error == null) {
					Vouchers.getVoucherManager().remove(voucher.getId());
					clickEvent.manager.showGUI(clickEvent.player, new GUIVoucherList());
				}

			});

		if (clickEvent.clickType == ClickType.LEFT)
			clickEvent.manager.showGUI(clickEvent.player, new GUIVoucherEdit(voucher));
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
