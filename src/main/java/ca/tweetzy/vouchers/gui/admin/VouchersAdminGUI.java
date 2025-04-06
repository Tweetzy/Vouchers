/*
 * Vouchers
 * Copyright 2022-2025 Kiran Hart
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
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import org.bukkit.entity.Player;

public final class VouchersAdminGUI extends VouchersBaseGUI {

	public VouchersAdminGUI(Player player) {
		super(null, player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7v%s".formatted(Vouchers.getInstance().getVersion()), 6);
		draw();
	}

	@Override
	protected void draw() {

		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));


		setButton(2, 2, QuickItem
				.of(CompMaterial.PAPER)
				.name("<GRADIENT:B3EBF2>&lVoucher List</GRADIENT:AEC6CF>")
				.lore(
						"&8View any created vouchers",
						"&7You can view any vouchers that were",
						"&7created here, as well as make new ones.",
						"",
						"&b&lClick &8» &7To view vouchers"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherListGUI(click.player)));

		setButton(getRows() - 2, 4, QuickItem
				.of(CompMaterial.DIAMOND)
				.name("&e&lPatreon")
				.lore(
						"&8Support me on Patreon",
						"&7By supporting me on Patreon you will",
						"&7be helping me be able to continue updating",
						"&7and creating free plugins.",
						"",
						"&e&lClick &8» &7To view Patreon"
				)
				.glow(true)
				.make(), click -> {

			click.gui.close();
			Common.tellNoPrefix(click.player,
					"&8&m-----------------------------------------------------",
					"",
					ChatUtil.centerMessage("&E&lTweetzy Patreon"),
					ChatUtil.centerMessage("&bhttps://patreon.tweetzy.ca"),
					"&8&m-----------------------------------------------------"
			);
		});
	}
}
