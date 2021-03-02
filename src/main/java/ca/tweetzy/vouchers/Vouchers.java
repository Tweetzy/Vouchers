package ca.tweetzy.vouchers;

import ca.tweetzy.core.TweetyCore;
import ca.tweetzy.core.TweetyPlugin;
import ca.tweetzy.core.commands.CommandManager;
import ca.tweetzy.core.compatibility.ServerVersion;
import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.utils.Metrics;
import ca.tweetzy.vouchers.commands.CommandCreate;
import ca.tweetzy.vouchers.settings.Settings;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 5:15 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Vouchers extends TweetyPlugin {

    private static Vouchers instance;
    private final Config data = new Config(this, "data.yml");

    protected Metrics metrics;
    private CommandManager commandManager;

    @Override
    public void onPluginLoad() {
        instance = this;
    }

    @Override
    public void onPluginEnable() {
        TweetyCore.registerPlugin(this, 6, "PAPER");
        TweetyCore.initEvents(this);

        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_7)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Settings
        Settings.setup();

        // Locale
        setLocale(Settings.LANG.getString(), false);

        // Data File
        this.data.load();

        // Commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addMainCommand("vouchers").addSubCommands(new CommandCreate());

        // Metrics
        if (Settings.METRICS.getBoolean()) {
            this.metrics = new Metrics(this, 10530);
        }
    }

    @Override
    public void onPluginDisable() {
        instance = null;
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

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
