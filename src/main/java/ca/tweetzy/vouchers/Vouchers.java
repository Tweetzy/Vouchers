package ca.tweetzy.vouchers;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.Messenger;
import ca.tweetzy.tweety.plugin.SimplePlugin;
import ca.tweetzy.vouchers.settings.Settings;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 22 2021
 * Time Created: 6:24 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Vouchers extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		normalizePrefix();
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
