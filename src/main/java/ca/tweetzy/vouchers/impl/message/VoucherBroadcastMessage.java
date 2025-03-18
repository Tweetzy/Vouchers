package ca.tweetzy.vouchers.impl.message;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class VoucherBroadcastMessage extends BaseMessage {

	public VoucherBroadcastMessage(String primaryContent) {
		super(MessageType.BROADCAST, primaryContent);
	}

	@Override
	public void send(Player player, Object[] variables) {
		String formattedContent = PAPIHook.tryReplace(player, Common.colorize(this.getPrimaryContent()));
		formattedContent = Replacer.replaceVariables(formattedContent, variables);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			Common.tellNoPrefix(onlinePlayer, formattedContent);
		}
	}
}
