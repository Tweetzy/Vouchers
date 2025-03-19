package ca.tweetzy.vouchers.gui.admin.messages;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.impl.message.VoucherActionBarMessage;
import ca.tweetzy.vouchers.impl.message.VoucherBroadcastMessage;
import ca.tweetzy.vouchers.impl.message.VoucherChatMessage;
import ca.tweetzy.vouchers.impl.message.VoucherTitleMessage;
import ca.tweetzy.vouchers.model.input.UserInput;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherMessageListGUI extends VouchersPagedGUI<Message> {

	private Voucher voucher;
	private List<Message> messages;
	private MessageType messageType;

	private Message lastClickedMessage;
	private final boolean fromRewards;

	public VoucherMessageListGUI(@NonNull Player player, Voucher voucher, List<Message> messageList, MessageType messageType, boolean fromRewards) {
		super(new VoucherMessageTypeGUI(player, voucher, messageList, fromRewards), player, "<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8» &7Edit Messages", 6, new ArrayList<>(messageList));
		this.voucher = voucher;
		this.messages = messageList;
		this.messageType = messageType;
		this.fromRewards =fromRewards;
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items = new ArrayList<>(this.items.stream().filter(msg -> msg.getMessageType() == messageType).toList());
	}

	@Override
	protected void drawFixed() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.PINK_STAINED_GLASS_PANE).glow(true).make()
		)));

		// add button
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:fc67fa>&LAdd Message</GRADIENT:f4c4f3>")
				.make(), click -> {

			//
			UserInput.get(
					click.player,
					"vouchers",
					"enter msg",
					result -> {
						if (this.messageType != MessageType.TITLE) {
							switch (this.messageType) {
								case ACTION_BAR -> this.messages.add(new VoucherActionBarMessage(result));
								case CHAT -> this.messages.add(new VoucherChatMessage(result));
								case BROADCAST -> this.messages.add(new VoucherBroadcastMessage(result));
							}

							this.voucher.sync((synchronizeResult) -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messages, this.messageType, this.fromRewards)));
						} else {
							UserInput.get(
									click.player,
									"vouchers",
									"enter subtitle",
									subtitle -> {
										this.messages.add(new VoucherTitleMessage(
												result,
												subtitle,
												20, 20, 20
										));
										this.voucher.sync((synchronizeResult) -> click.manager.showGUI(click.player, new VoucherMessageListGUI(click.player, this.voucher, this.messages, this.messageType, this.fromRewards)));
									},
									null,
									() -> click.manager.showGUI(click.player, VoucherMessageListGUI.this),
									validate -> !validate.isEmpty()
							);
						}
					},
					null,
					() -> click.manager.showGUI(click.player, VoucherMessageListGUI.this),
					validate -> !validate.isEmpty()
			);
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(Message rawMessage) {
		final BaseMessage baseMessage = (BaseMessage) rawMessage;
		String name = baseMessage.getPrimaryContent();
		List<String> lore = new ArrayList<>(List.of(
				"&8Use the following options to adjust",
				"",
				"&e&lLeft Click",
				"&7To select/swap with another selected message.",
				"",
				"&e&lRight Click",
				"&7To run this message. Players will see it if",
				"&7it is a broadcast message.",
				"",
				"&e&lDrop Key",
				"&7To &cdelete &7this message"
		));

		if (baseMessage instanceof VoucherTitleMessage titleMessage) {
			name = "<GRADIENT:fc67fa>&LTitle Message</GRADIENT:f4c4f3>";
			lore.addAll(0, List.of(
					"&7Title&f: ",
					"%s".formatted(titleMessage.getPrimaryContent().isEmpty() ? "&cNot Set" : titleMessage.getPrimaryContent()),
					"",
					"&7Subtitle&f:",
					"%s".formatted(titleMessage.getSecondaryContent().isEmpty() ? "&cNot Set" : titleMessage.getSecondaryContent()),
					""
			));
		}

		return QuickItem
				.of(CompMaterial.PAPER)
				.name(name)
				.lore(lore)
				.make();
	}

	@Override
	protected void onClick(Message message, GuiClickEvent clickEvent) {
		if (clickEvent.clickType == ClickType.LEFT) {
			handleSwap(message);
			saveVoucher();
		} else if (clickEvent.clickType == ClickType.RIGHT) {
			final BaseMessage msg = (BaseMessage) message;
			msg.send(clickEvent.player, "player", player.getName());
		} else if (clickEvent.clickType == ClickType.DROP) {
			// Handle drop click to remove description
			this.messages.remove(message);
			this.items.remove(message);
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

	private void handleSwap(Message message) {
		if (lastClickedMessage != null) {
			// Swap logic here
			int index1 = messages.indexOf(lastClickedMessage);
			int index2 = messages.indexOf(message);
			if (index1 != -1 && index2 != -1) {
				Message temp = messages.get(index1);
				messages.set(index1, messages.get(index2));
				messages.set(index2, temp);
				draw(); // Redraw the GUI
			}
			lastClickedMessage = null;
		} else {
			lastClickedMessage = message;
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
