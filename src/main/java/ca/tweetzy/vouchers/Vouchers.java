package ca.tweetzy.vouchers;

import ca.tweetzy.core.TweetyPlugin;
import ca.tweetzy.core.configuration.Config;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 5:15 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Vouchers extends TweetyPlugin {

    private static Vouchers instance;

    @Override
    public void onPluginLoad() {
        instance = this;
    }

    @Override
    public void onPluginEnable() {

    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onConfigReload() {

    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public static Vouchers getInstance() {
        return instance;
    }
}
