package ca.tweetzy.vouchers.gui.admin.messages;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

public final class VoucherMessageTypeGUI extends VouchersBaseGUI {

	private final Voucher voucher;
	private final List<Message> messageList;

	public VoucherMessageTypeGUI(@NonNull Player player, @NonNull final Voucher voucher, List<Message> messageList) {
		super(new VoucherOverviewGUI(player, voucher), player, "<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8» &7Voucher Messages", 5);
		this.voucher = voucher;
		this.messageList = messageList;
		draw();
	}

	@Override
	protected void draw() {
		InventoryBorder.getBorders(5).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.PINK_STAINED_GLASS_PANE).glow(true).make()
		)));

		// broadcast
		setButton(2,2, QuickItem
				.of(CompMaterial.NAUTILUS_SHELL)
				.name("<GRADIENT:fc67fa>&lBroadcast Messages</GRADIENT:f4c4f3>")
				.lore(
						"&8These messages are sent to everyone",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.BROADCAST).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7broadcast messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.voucher.getMessages(), MessageType.BROADCAST)));

		// chat
		setButton(2,3, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:fc67fa>&LChat Messages</GRADIENT:f4c4f3>")
				.lore(
						"&8These messages are sent to the player's chat.",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.CHAT).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7chat messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.voucher.getMessages(), MessageType.CHAT)));

		// action bar
		setButton(2,5, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:fc67fa>&LAction Bar Messages</GRADIENT:f4c4f3>")
				.lore(
						"&8These messages are sent to the action bar",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.ACTION_BAR).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7action bar messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.voucher.getMessages(), MessageType.ACTION_BAR)));

		// titles
		setButton(2,6, QuickItem
				.of(CompMaterial.ENCHANTED_BOOK)
				.name("<GRADIENT:fc67fa>&lTitle Messages</GRADIENT:f4c4f3>")
				.lore(
						"&8These are titles sent to the player",
						"",
						"&7Total Messages&f: &a%s".formatted(messageList.stream().filter(msg -> msg.getMessageType() == MessageType.TITLE).count()),
						"",
						"&e&lClick",
						"&7To &aadd&7/&cremove &7title messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.voucher.getMessages(), MessageType.TITLE)));

		applyBackExit();
	}
}
