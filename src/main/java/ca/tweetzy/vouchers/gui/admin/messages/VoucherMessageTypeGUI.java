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

package ca.tweetzy.vouchers.gui.admin.messages;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.gui.admin.rewards.VoucherRewardListGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

public final class VoucherMessageTypeGUI extends VouchersBaseGUI {

	private final Voucher voucher;
	private final List<Message> messageList;

	private final boolean fromRewards;

	public VoucherMessageTypeGUI(@NonNull Player player, @NonNull final Voucher voucher, List<Message> messageList, boolean fromRewards) {
		super(fromRewards ? new VoucherRewardListGUI(player, voucher) : new VoucherOverviewGUI(player, voucher), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Voucher Messages", 5);
		this.voucher = voucher;
		this.messageList = messageList;
		this.fromRewards = fromRewards;
		draw();
	}

	@Override
	protected void draw() {
		InventoryBorder.getBorders(5).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		// broadcast
		setButton(2, 2, QuickItem
				.of(CompMaterial.NAUTILUS_SHELL)
				.name("<GRADIENT:B3EBF2>&lBroadcast Messages</GRADIENT:AEC6CF>")
				.lore(
						"&8These messages are sent to everyone",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.BROADCAST).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7broadcast messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messageList, MessageType.BROADCAST, this.fromRewards)));

		// chat
		setButton(2, 3, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:B3EBF2>&LChat Messages</GRADIENT:AEC6CF>")
				.lore(
						"&8These messages are sent to the player's chat.",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.CHAT).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7chat messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messageList, MessageType.CHAT, this.fromRewards)));

		// action bar
		setButton(2, 5, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:B3EBF2>&LAction Bar Messages</GRADIENT:AEC6CF>")
				.lore(
						"&8These messages are sent to the action bar",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.ACTION_BAR).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7action bar messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messageList, MessageType.ACTION_BAR, this.fromRewards)));

		// titles
		setButton(2, 6, QuickItem
				.of(CompMaterial.ENCHANTED_BOOK)
				.name("<GRADIENT:B3EBF2>&lTitle Messages</GRADIENT:AEC6CF>")
				.lore(
						"&8These are titles sent to the player",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.TITLE).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7title messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messageList, MessageType.TITLE, this.fromRewards)));

		applyBackExit();
	}
}
