package ca.tweetzy.vouchers.model;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public final class QuickReplace {

	public String getColouredAndReplaced(@NonNull final Player player, @NonNull final String message, final Voucher voucher) {
		return Common.colorize(PAPIHook.tryReplace(player, Replacer.replaceVariables(message,
				"player", player.getName(),
				"voucher_id", voucher == null ? "Unknown" : voucher.getId(),
				"voucher_name", voucher == null ? "Unknown" : voucher.getName(),
				"pl_prefix", Settings.PREFIX.getString()
		)));
	}
}
