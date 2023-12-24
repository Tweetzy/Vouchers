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
import ca.tweetzy.flight.gui.template.BaseGUI;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.gui.abstraction.VouchersBaseGUI;

public final class GUIVouchersAdmin extends VouchersBaseGUI {

	public GUIVouchersAdmin() {
		super(null, "&bVouchers", 3);
		draw();
	}

	@Override
	protected void draw() {

		setButton(1, 1, QuickItem
				.of(CompMaterial.PAPER)
				.name("&b&lVoucher List")
				.lore(
						"&8View any created vouchers",
						"&7You can view any vouchers that were",
						"&7created here, as well as make new ones.",
						"",
						"&b&lClick &8» &7To view vouchers"
				)
				.make(), click -> click.manager.showGUI(click.player, new GUIVoucherList()));

		setButton(1, 4, QuickItem
				.of(CompMaterial.KNOWLEDGE_BOOK)
				.name("&b&lRedeem History")
				.lore(
						"&8View voucher redeems",
						"&7You can view all the vouchers that were",
						"&7were redeemed by players here",
						"",
						"&b&lClick &8» &7To view redeems"
				)
				.make(), click -> click.manager.showGUI(click.player, new GUIVoucherRedeemList()));


		setButton(1, 7, QuickItem
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
