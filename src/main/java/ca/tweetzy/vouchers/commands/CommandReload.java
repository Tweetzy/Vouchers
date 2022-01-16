package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.debug.LagCatcher;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.menu.MenuVoucherEdit;
import ca.tweetzy.vouchers.settings.Localization;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 4:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandReload extends AbstractVoucherCommand {

	public CommandReload() {
		super("reload|rl");
	}

	@Override
	protected void onCommand() {
		Vouchers.getInstance().reload();
		tell("&aSuccessfully reloaded files.");
	}
}
