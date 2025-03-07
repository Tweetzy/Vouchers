package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import org.bukkit.entity.Player;

public final class VoucherActionBarMessage extends BaseMessage {

	public VoucherActionBarMessage(String primaryContent) {
		super(MessageType.ACTION_BAR, primaryContent);
	}

	@Override
	protected void send(Player player) {
		// TODO SEND ACTION BAR MESSAGE
	}
}
