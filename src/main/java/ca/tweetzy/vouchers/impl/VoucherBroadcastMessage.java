package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import org.bukkit.entity.Player;

public final class VoucherBroadcastMessage extends BaseMessage {

	public VoucherBroadcastMessage(String primaryContent) {
		super(MessageType.BROADCAST, primaryContent);
	}

	@Override
	protected void send(Player player) {
		// TODO SEND BROADCAST MESSAGES
	}
}
