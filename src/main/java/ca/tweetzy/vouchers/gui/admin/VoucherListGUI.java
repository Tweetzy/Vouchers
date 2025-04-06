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

package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.BaseVoucher;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import ca.tweetzy.vouchers.impl.StandardVoucher;
import ca.tweetzy.vouchers.model.input.UserInput;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherListGUI extends VoucherUpdatingPagedGUI<Voucher> {

	private Category selectedCategory = Vouchers.getCategoryManager().get("allvouchers");

	public VoucherListGUI(@NonNull final Player player) {
		super(new VouchersAdminGUI(player), player, TranslationManager.string(Translations.GUI_ADMIN_VOUCHER_LIST_TITLE), 6, 20, new ArrayList<>());

		setOnOpen(open -> startTask());
		applyClose();
		draw();
	}

	@Override
	protected void prePopulate() {
		if (this.selectedCategory.getId().equalsIgnoreCase("allvouchers") || this.selectedCategory == null)
			this.items = new ArrayList<>(Vouchers.getVoucherManager().getValues());
		else
			this.items = new ArrayList<>(Vouchers.getVoucherManager().getValues()).stream().filter(voucher -> voucher.getCategoryId().equalsIgnoreCase(this.selectedCategory.getId())).toList();
	}

	@Override
	protected void drawFixed() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 6, QuickItem
				.of(this.selectedCategory.getIcon())
				.name(this.selectedCategory.getName())
				.lore(
						"&8Used to change categories",
						"",
						"&e&lClick",
						"&7To cycle to the next category"
				)
				.make(), click -> {

			this.selectedCategory = Vouchers.getCategoryManager().getNextElement(this.selectedCategory);
			draw();
		});

		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:77DD77>&lCreate Voucher</GRADIENT:C1E1C1>")
				.lore(
						"&8Used to create a voucher",
						"&7You can also create a file in",
						"&7the voucher-files folder",
						"",
						"&e&lClick",
						"&7To create a new voucher"
				)
				.make(), click -> {
			cancelTask();

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Creation</GRADIENT:AEC6CF>", "&eEnter id for voucher in chat", result -> {
				StandardVoucher.empty(result).store(stored -> {
					if (stored != null) {
						Vouchers.getVoucherManager().add(result, stored);
						click.manager.showGUI(click.player, new VoucherListGUI(click.player));
					}

				});
			}, fail -> {
				if (Vouchers.getVoucherManager().doesVoucherWithIdExists(fail)) {
					Common.tell(click.player, TranslationManager.string(Translations.VOUCHER_EXISTS_ALREADY));
				}
			}, () -> click.manager.showGUI(click.player, VoucherListGUI.this), validate -> !validate.isEmpty(), transform -> ChatColor.stripColor(transform).replaceAll("\\s", ""));
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(Voucher voucher) {
		return QuickItem
				.of(voucher.getItem())
				.name(voucher.getDisplayName())
				.lore(voucher.getDescription())
				.lore(
						"",
						"&e&lLeft Click",
						"&7To open voucher overview",
						"",
						"&e&lRight Click",
						"&7To give yourself this voucher",
						"",
						"&e&lDrop Key",
						"&7To &cdelete &7this voucher, this can't be undone."

				)
				.glow(voucher.getSettings().useGlow())
				.make();
	}

	@Override
	protected void onClick(Voucher voucher, GuiClickEvent click) {
		final BaseVoucher baseVoucher = (BaseVoucher) voucher;

		if (click.clickType == ClickType.LEFT) {
			cancelTask();
			click.manager.showGUI(click.player, new VoucherOverviewGUI(click.player, voucher));
		}

		if (click.clickType == ClickType.RIGHT) {
			PlayerUtil.giveItem(click.player, baseVoucher.generatePhysicalVoucher(click.player));
		}

		if (click.clickType == ClickType.DROP) {
			// TODO DELETE
			baseVoucher.unStore(result -> draw());
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
