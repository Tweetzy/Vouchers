package ca.tweetzy.vouchers.impl.message;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.VoucherHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class VoucherActionBarMessage extends BaseMessage {

	public VoucherActionBarMessage(String primaryContent) {
		super(MessageType.ACTION_BAR, primaryContent);
	}

	@Override
	public void send(Player player, String[] variables) {
		String formattedContent = PAPIHook.tryReplace(player, Common.colorize(this.getPrimaryContent()));
		formattedContent = VoucherHelper.dynamicVariablesReplace(formattedContent, variables);
		formattedContent = formattedContent.replace("%player%", player.getName());

		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(formattedContent));
	}
}
