package ca.tweetzy.vouchers.impl.message;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class VoucherActionBarMessage extends BaseMessage {

	public VoucherActionBarMessage(String primaryContent) {
		super(MessageType.ACTION_BAR, primaryContent);
	}

	@Override
	public void send(Player player, Object[] variables) {
		String formattedContent = PAPIHook.tryReplace(player, Common.colorize(this.getPrimaryContent()));

		formattedContent = Replacer.replaceVariables(formattedContent, variables);

		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(formattedContent));
	}
}
