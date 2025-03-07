package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public final class VoucherTitleMessage extends BaseMessage {

	private String secondaryContent;
	private int fadeInTime;
	private int stayTime;
	private int fadeOutTime;

	public VoucherTitleMessage(final String primaryContent, final String secondaryContent, final int fadeInTime, final int stayTime, final int fadeOutTime) {
		super(MessageType.TITLE, primaryContent);
		this.secondaryContent = secondaryContent;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
	}

	@Override
	protected void send(Player player) {
		// TODO SEND TITLES MESSAGES
	}
}
