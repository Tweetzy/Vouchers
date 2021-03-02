package ca.tweetzy.vouchers.settings;

import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.configuration.ConfigSetting;
import ca.tweetzy.vouchers.Vouchers;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 5:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Settings {

    static final Config config = Vouchers.getInstance().getCoreConfig();

    public static final ConfigSetting LANG = new ConfigSetting(config, "lang", "en_US", "Default language file");
    public static final ConfigSetting UPDATE_CHECKER = new ConfigSetting(config, "update checker", true, "Should vouchers check for updates?");
    public static final ConfigSetting METRICS = new ConfigSetting(config, "metrics", true, "Should the plugin use metrics?", "It simply allows me to see how many servers", "are currently using the vouchers plugin.");

    public static void setup() {
        config.load();
        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
