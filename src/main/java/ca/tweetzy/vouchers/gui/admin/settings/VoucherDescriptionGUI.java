/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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

package ca.tweetzy.vouchers.gui.admin.settings;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import ca.tweetzy.vouchers.model.input.UserInput;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VoucherDescriptionGUI extends VoucherUpdatingPagedGUI<String> {

	private Voucher voucher;
	private String lastClickedDescription;

	public VoucherDescriptionGUI(@NonNull final Player player, @NonNull final Voucher voucher) {
		super(new VoucherSettingsGUI(player, voucher), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Edit Description", 6, 20, new ArrayList<>());
		this.voucher = voucher;

		setOnOpen(open -> startTask());
		applyClose();
		draw();
	}

	@Override
	protected void prePopulate() {
		this.voucher = Vouchers.getVoucherManager().get(this.voucher.getId());
		this.items = this.voucher.getDescription();
	}

	@Override
	protected void drawFixed() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:77DD77>&lNew Line</GRADIENT:C1E1C1>")
				.lore(
						"&8Used to add a new line",
						"",
						"&e&lClick",
						"&7To insert new description line"
				)
				.make(), click -> {

			click.gui.close();

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Edit</GRADIENT:AEC6CF>", "&eEnter description in chat", result -> {
				voucher.getDescription().add(result);
				saveVoucher();
				click.manager.showGUI(click.player, new VoucherDescriptionGUI(click.player, voucher));
			}, null, () -> click.manager.showGUI(click.player, new VoucherDescriptionGUI(click.player, voucher)), validate -> !validate.isEmpty());
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(String description) {
		return QuickItem
				.of(CompMaterial.PAPER)
				.name(description)
				.lore(
						"&8Use the following options to adjust",
						"",
						"&e&lLeft Click",
						"&7To select/swap with another selected line.",
						"",
						"&e&lDrop Key",
						"&7To &cremove &7this line from the description"
				)
				.make();
	}

	@Override
	protected void onClick(String description, GuiClickEvent clickEvent) {
		if (clickEvent.clickType == ClickType.LEFT) {
			handleSwap(description);
			saveVoucher();
		} else if (clickEvent.clickType == ClickType.DROP) {
			// Handle drop click to remove description
			this.voucher.getDescription().remove(description);
			saveVoucher();
			draw();
		}
	}

	private void saveVoucher() {
		this.voucher.sync(result -> {
			if (result == SynchronizeResult.FAILURE)
				Common.tell(this.player, "&cSomething went wrong while saving the voucher.");
		});
	}

	private void handleSwap(String description) {
		if (lastClickedDescription != null) {
			// Swap logic here
			int index1 = items.indexOf(lastClickedDescription);
			int index2 = items.indexOf(description);
			if (index1 != -1 && index2 != -1) {
				String temp = items.get(index1);
				items.set(index1, items.get(index2));
				items.set(index2, temp);
				draw(); // Redraw the GUI
			}
			lastClickedDescription = null; // Reset
		} else {
			lastClickedDescription = description; // Store the first clicked description
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
