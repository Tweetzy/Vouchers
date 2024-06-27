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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.abstraction.VouchersPagedGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public final class GUIListEditor extends VouchersPagedGUI<String> {

	private final Voucher voucher;

	private int selectedIndex = -1;

	public GUIListEditor(@NonNull final Voucher voucher) {
		super(new GUIVoucherEdit(voucher), "&bVouchers &8> &7" + voucher.getId() + " &8> &7Lore", 6, voucher.getDescription());
		this.voucher = voucher;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(String line) {
		return QuickItem
				.of(CompMaterial.PAPER)
				.name((line == null || line.trim().isBlank()) ? "&7-Blank Line-" : line)
				.lore(
						"&b&lLeft Click &8» &7To swap with another line",
						"&c&lPress 1 &8» &7To delete line"
				)
				.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL).name("&a&lNew Line").lore(
				"&b&lLeft Click &8» &7To add new line",
				"&a&lRight Click &8» &7To add blank line"
		).make(), click -> {
			if (click.clickType == ClickType.LEFT) {
				new TitleInput(Vouchers.getInstance(), click.player, "&b&lVoucher Edit", "&fEnter new line for lore") {

					@Override
					public void onExit(Player player) {
						click.manager.showGUI(click.player, GUIListEditor.this);
					}

					@Override
					public boolean onResult(String string) {
						GUIListEditor.this.voucher.getDescription().add(string);
						GUIListEditor.this.voucher.sync(true);
						click.manager.showGUI(click.player, new GUIListEditor(GUIListEditor.this.voucher));
						return true;
					}
				};
			}

			if (click.clickType == ClickType.RIGHT) {
				long totalBlanks = this.voucher.getDescription().stream().filter(line -> line.isEmpty() || line.isBlank() || line.contains("-Blank-")).count();

				this.voucher.getDescription().add("-Blank-" + (totalBlanks + 1));
				this.voucher.sync(true);
				click.manager.showGUI(click.player, new GUIListEditor(this.voucher));
			}
		});
	}

	@Override
	protected void onClick(String string, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT) {
			final int clickedIndex = this.voucher.getDescription().indexOf(string);

			if (this.selectedIndex == -1)
				this.selectedIndex = clickedIndex;
			else {
				if (this.selectedIndex == clickedIndex) return;
				Collections.swap(this.voucher.getDescription(), this.selectedIndex, clickedIndex);
				this.voucher.sync(true);

				click.manager.showGUI(click.player, new GUIListEditor(GUIListEditor.this.voucher));

			}
		}

		if (click.clickType == ClickType.NUMBER_KEY) {
			this.voucher.getDescription().remove(string);

			this.voucher.sync(true);
			click.manager.showGUI(click.player, new GUIListEditor(GUIListEditor.this.voucher));
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
