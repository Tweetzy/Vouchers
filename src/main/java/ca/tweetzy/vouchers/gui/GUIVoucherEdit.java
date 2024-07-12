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
import ca.tweetzy.flight.gui.template.MaterialPickerGUI;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.abstraction.VouchersBaseGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public final class GUIVoucherEdit extends VouchersBaseGUI {

	private final Voucher voucher;

	public GUIVoucherEdit(@NonNull final Voucher voucher) {
		super(new GUIVoucherList(), "&bVouchers &8> &7Edit &8> &7" + voucher.getId(), 6);
		this.voucher = voucher;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void draw() {

		setButton(1, 1, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("&b&lVoucher Name")
				.lore(
						"",
						"&7Current&f: " + this.voucher.getName(),
						"",
						"&b&lClick &8» &7To change display name"
				)
				.make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&lVoucher Edit", "&fEnter new name into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIVoucherEdit.this);
			}

			@Override
			public boolean onResult(String string) {
				if (string.length() < 1) return false;
				GUIVoucherEdit.this.voucher.setName(string);
				GUIVoucherEdit.this.voucher.sync(true);

				click.manager.showGUI(click.player, new GUIVoucherEdit(GUIVoucherEdit.this.voucher));
				return true;
			}
		});

		setButton(1, 4, QuickItem
				.of(this.voucher.getItem())
				.name("&b&lVoucher Item")
				.lore(
						"",
						"&7Current&f: " + ChatUtil.capitalizeFully(this.voucher.getItem().getType()),
						"",
						"&b&lLeft Click &8» &7To change with picker",
						"&b&lRight Click &8» &7To select held item"
				)
				.make(), click -> {

			if (click.clickType == ClickType.LEFT)
				click.manager.showGUI(click.player, new MaterialPickerGUI(this, "&bVouchers &8> &7Select Material", null, (event, selected) -> {
					this.voucher.setItem(selected);
					this.voucher.sync(true);
					click.manager.showGUI(click.player, new GUIVoucherEdit(this.voucher));
				}));

			if (click.clickType == ClickType.RIGHT) {
				final ItemStack cursor = click.cursor;
				if (cursor == null || cursor.getType() == CompMaterial.AIR.parseMaterial()) return;

				this.voucher.setItem(cursor.clone());
				this.voucher.sync(true);
				click.manager.showGUI(click.player, new GUIVoucherEdit(this.voucher));
			}
		});

		setButton(1, 7, QuickItem
				.of(CompMaterial.WRITABLE_BOOK)
				.name("&b&lVoucher Description")
				.lore(
						"",
						"&7Current&f: "
				)
				.lore(this.voucher.getFilteredDescription())
				.lore(
						"",
						"&b&lClick &8» &7To edit description"
				)
				.make(), click -> click.manager.showGUI(click.player, new GUIListEditor(this.voucher)));

		setButton(3, 1, QuickItem
				.of(CompMaterial.REPEATER)
				.name("&b&lVoucher Settings")
				.lore(
						"",
						"&b&lClick &8» &7To adjust settings"
				)
				.make(), click -> click.manager.showGUI(click.player, new GUIVoucherSettings(this.voucher)));

		setButton(3, 4, QuickItem
				.of(CompMaterial.HEART_OF_THE_SEA)
				.name("&b&lVoucher Reward Mode")
				.lore(
						"",
						"&7Current&f: &b" + ChatUtil.capitalizeFully(this.voucher.getRewardMode()),
						"",
						"&b&lClick &8» &7To switch mode"
				)
				.make(), click -> {

			this.voucher.setRewardMode(this.voucher.getRewardMode().next());
			this.voucher.sync(true);
			click.manager.showGUI(click.player, new GUIVoucherEdit(this.voucher));
		});

		setButton(3, 7, QuickItem
				.of(CompMaterial.DIAMOND)
				.name("&b&lVoucher Rewards")
				.lore(
						"",
						"&b&lClick &8» &7To edit rewards"
				)
				.make(), click -> click.manager.showGUI(click.player, new GUIRewardList(this.voucher)));

		applyBackExit();
	}
}
