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

package ca.tweetzy.vouchers.gui.admin.message;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Message;
import ca.tweetzy.vouchers.api.voucher.MessageType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.gui.admin.GUIVoucherSettings;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GUIMessagesList extends VouchersPagedGUI<Message> {

	private final Voucher voucher;
	private int selectedIndex = -1;

	public GUIMessagesList(@NonNull final Player player, @NonNull final Voucher voucher) {
		super(new GUIVoucherSettings(player, voucher), player, "&bVouchers &8> &7" + voucher.getId() + " &8> &7Messages", 6, voucher.getOptions().getMessages());
		this.voucher = voucher;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Message message) {

		final List<String> lore = new ArrayList<>();

		lore.add("");
		lore.add("&7Type&f: &b" + ChatUtil.capitalizeFully(message.getMessageType()));

		if (message.getMessageType() == MessageType.TITLE || message.getMessageType() == MessageType.SUBTITLE) {
			lore.add("&7Fade In&f: &b" + message.getFadeInDuration());
			lore.add("&7Stay&f: &b" + message.getStayDuration());
			lore.add("&7Fade Out&f: &b" + message.getFadeOutDuration());
		}

		lore.add("");
		lore.add("&b&lLeft Click &8» &7To swap with another message");
		lore.add("&c&lPress 1 &8» &7To delete this message");

		return QuickItem
				.of(CompMaterial.PAPER)
				.name(message.getMessage())
				.lore(lore)
				.make();
	}

	@Override
	protected void drawFixed() {
		setButton(5, 4, QuickItem
				.of(CompMaterial.SLIME_BALL)
				.name("&a&lAdd Message")
				.lore("&b&lClick &8» &7To add new message")
				.make(), click -> click.manager.showGUI(click.player, new GUIMessageType(click.player, this.voucher)));
	}

	@Override
	protected void onClick(Message message, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT) {
			final int clickedIndex = this.voucher.getOptions().getMessages().indexOf(message);

			if (this.selectedIndex == -1)
				this.selectedIndex = clickedIndex;
			else {
				if (this.selectedIndex == clickedIndex) return;
				Collections.swap(this.voucher.getOptions().getMessages(), this.selectedIndex, clickedIndex);
				this.voucher.sync(true);

				click.manager.showGUI(click.player, new GUIMessagesList(click.player, GUIMessagesList.this.voucher));
			}
		}

		if (click.clickType == ClickType.NUMBER_KEY) {
			this.voucher.getOptions().getMessages().remove(message);
			this.voucher.sync(true);
			click.manager.showGUI(click.player, new GUIMessagesList(click.player, this.voucher));
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
