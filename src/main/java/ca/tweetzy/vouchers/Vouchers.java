package ca.tweetzy.vouchers;

import ca.tweetzy.tweety.TweetyPlugin;
import ca.tweetzy.tweety.configuration.Config;
import ca.tweetzy.tweety.gui.GuiManager;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.Messenger;
import ca.tweetzy.tweety.util.MinecraftVersion;
import ca.tweetzy.vouchers.commands.VouchersCommandHandler;
import ca.tweetzy.vouchers.listener.VoucherListeners;
import ca.tweetzy.vouchers.model.VoucherManager;
import ca.tweetzy.vouchers.settings.Locale;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 22 2021
 * Time Created: 6:24 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Vouchers extends TweetyPlugin {

	private final VoucherManager voucherManager = new VoucherManager();

	private boolean bStats = false;

	private final Config coreConfig = new Config(this);
	private Config langConfig;

	private GuiManager guiManager;

	@Override
	protected void onPluginStart() {
		Settings.setup();

		normalizePrefix();

		this.langConfig = new Config(this, "/locales/", Settings.LANG.getString() + ".yml");
		Locale.setup();

		this.guiManager = new GuiManager(this);
		this.guiManager.init();

		registerEvents(new VoucherListeners());

		if (Settings.AUTO_BSTATS.getBoolean()) {
			final File file = new File("plugins" + File.separator + "bStats" + File.separator + "config.yml");
			if (!file.exists()) bStats = true;
			else {
				final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
				configuration.set("enabled", true);
				try {
					configuration.save(file);
					bStats = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!bStats) {
			Common.logFramed("&cPlease enable bStats within your plugins folder", "&cit helps me collect data on Vouchers.");
		}

		VouchersCommandHandler.getInstance().register();
	}

	public static VoucherManager getVoucherManager() {
		return ((Vouchers) TweetyPlugin.getInstance()).voucherManager;
	}

	public static Config getCoreConfig() {
		return ((Vouchers) TweetyPlugin.getInstance()).coreConfig;
	}

	public static Config getLocaleConfig() {
		return ((Vouchers) TweetyPlugin.getInstance()).langConfig;
	}

	public static GuiManager getGuiManager() {
		return ((Vouchers) TweetyPlugin.getInstance()).guiManager;
	}

	@Override
	public MinecraftVersion.V getMinimumVersion() {
		return MinecraftVersion.V.v1_8;
	}

	private void normalizePrefix() {
		Common.ADD_TELL_PREFIX = Settings.PREFIX.getString().length() != 0;
		Common.ADD_LOG_PREFIX = true;

		Common.setLogPrefix(Settings.PREFIX.getString() + " ");
		Common.setTellPrefix(Settings.PREFIX.getString());

		final String prefix = Settings.PREFIX.getString() + (Settings.PREFIX.getString().length() != 0 ? " " : "");

		Messenger.setInfoPrefix(prefix);
		Messenger.setAnnouncePrefix(prefix);
		Messenger.setErrorPrefix(prefix);
		Messenger.setQuestionPrefix(prefix);
		Messenger.setSuccessPrefix(prefix);
		Messenger.setWarnPrefix(prefix);
	}
}
