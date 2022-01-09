package ca.tweetzy.vouchers;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.Messenger;
import ca.tweetzy.tweety.MinecraftVersion;
import ca.tweetzy.tweety.plugin.TweetyPlugin;
import ca.tweetzy.tweety.settings.YamlStaticConfig;
import ca.tweetzy.vouchers.model.VoucherManager;
import ca.tweetzy.vouchers.model.VouchersDatabase;
import ca.tweetzy.vouchers.settings.Settings;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 22 2021
 * Time Created: 6:24 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Vouchers extends TweetyPlugin {

	private final VoucherManager voucherManager = new VoucherManager();

	@Override
	protected void onPluginStart() {
		normalizePrefix();
	}

	public static VoucherManager getVoucherManager() {
		return ((Vouchers) TweetyPlugin.getInstance()).voucherManager;
	}

	@Override
	public MinecraftVersion.V getMinimumVersion() {
		return MinecraftVersion.V.v1_8;
	}

	private void normalizePrefix() {
		Common.ADD_TELL_PREFIX = true;
		Common.ADD_LOG_PREFIX = true;
		Common.setTellPrefix(Settings.PREFIX);
		Common.setLogPrefix(Settings.PREFIX);
		Messenger.setInfoPrefix(Settings.PREFIX + " ");
		Messenger.setAnnouncePrefix(Settings.PREFIX + " ");
		Messenger.setErrorPrefix(Settings.PREFIX + " ");
		Messenger.setQuestionPrefix(Settings.PREFIX + " ");
		Messenger.setSuccessPrefix(Settings.PREFIX + " ");
		Messenger.setWarnPrefix(Settings.PREFIX + " ");
	}
}
