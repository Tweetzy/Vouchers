package ca.tweetzy.vouchers.api.voucher.message;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;

public abstract class BaseMessage implements Message {

	@Getter
	private final MessageType messageType;

	@Getter
	@Setter
	private String primaryContent;

	public BaseMessage(@NonNull final MessageType messageType, final String primaryContent) {
		this.messageType = messageType;
		this.primaryContent = primaryContent;
	}

	protected abstract void send(final Player player);
}
