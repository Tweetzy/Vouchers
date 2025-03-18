package ca.tweetzy.vouchers.impl.message;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.flight.utils.messages.Titles;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.hook.PAPIHook;
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
	public void send(Player player, Object[] variables) {
		String formattedTitle = Replacer.replaceVariables(PAPIHook.tryReplace(player, Common.colorize(this.getPrimaryContent())), variables);
		String formattedSubtitle = Replacer.replaceVariables(PAPIHook.tryReplace(player, Common.colorize(this.getSecondaryContent())), variables);

		Titles.sendTitle(player, this.fadeInTime, this.stayTime, this.fadeOutTime, formattedTitle, formattedSubtitle);
	}
}
