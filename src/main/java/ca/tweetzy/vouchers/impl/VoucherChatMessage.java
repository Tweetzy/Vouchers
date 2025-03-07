package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import org.bukkit.entity.Player;

public final class VoucherChatMessage extends BaseMessage {

	public VoucherChatMessage(String primaryContent) {
		super(MessageType.CHAT, primaryContent);
	}

	@Override
	protected void send(Player player) {
		// TODO SEND CHAT MESSAGES
	}
}
