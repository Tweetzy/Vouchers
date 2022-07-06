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
import ca.tweetzy.feather.gui.template.PagedGUI;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Message;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

public final class GUIMessagesList extends PagedGUI<Message> {

	private final Voucher voucher;

	public GUIMessagesList(@NonNull final Voucher voucher) {
		super(new GUIVoucherSettings(voucher), "&bVouchers &8> &7" + voucher.getId() + " &8> &7Messages", 6, voucher.getOptions().getMessages());
		this.voucher = voucher;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Message message) {
		return QuickItem.of(CompMaterial.PAPER).make();
	}

	@Override
	protected void drawAdditional() {

	}

	@Override
	protected void onClick(Message message, GuiClickEvent click) {

	}
}
