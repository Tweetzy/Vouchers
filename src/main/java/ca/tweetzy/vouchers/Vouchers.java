package ca.tweetzy.vouchers;

import ca.tweetzy.core.TweetyCore;
import ca.tweetzy.core.TweetyPlugin;
import ca.tweetzy.core.commands.CommandManager;
import ca.tweetzy.core.compatibility.ServerVersion;
import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.database.DataMigrationManager;
import ca.tweetzy.core.database.DatabaseConnector;
import ca.tweetzy.core.database.MySQLConnector;
import ca.tweetzy.core.gui.GuiManager;
import ca.tweetzy.core.utils.Metrics;
import ca.tweetzy.vouchers.api.UpdateChecker;
import ca.tweetzy.vouchers.commands.*;
import ca.tweetzy.vouchers.database.DataManager;
import ca.tweetzy.vouchers.database.migrations._1_InitialMigration;
import ca.tweetzy.vouchers.listener.PlayerListener;
import ca.tweetzy.vouchers.managers.VoucherManager;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.Bukkit;

import java.util.ArrayList;
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
    private final GuiManager guiManager = new GuiManager(this);

    protected Metrics metrics;
    private CommandManager commandManager;
    private VoucherManager voucherManager;

    private DatabaseConnector databaseConnector;
    private DataManager dataManager;

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
        this.commandManager.addMainCommand("vouchers").addSubCommands(
                new CommandCreate(),
                new CommandRemove(),
                new CommandEdit(),
                new CommandGive(),
                new CommandList(),
                new CommandSettings(),
                new CommandReload()
        );

        this.voucherManager = new VoucherManager();

        if (Settings.DATABASE_USE.getBoolean()) {
            this.databaseConnector = new MySQLConnector(this, Settings.DATABASE_HOST.getString(), Settings.DATABASE_PORT.getInt(), Settings.DATABASE_NAME.getString(), Settings.DATABASE_USERNAME.getString(), Settings.DATABASE_PASSWORD.getString(), Settings.DATABASE_USE_SSL.getBoolean());
            this.dataManager = new DataManager(this.databaseConnector, this);
            DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager, new _1_InitialMigration());
            dataMigrationManager.runMigrations();
        }

        this.voucherManager.loadVouchers(false, Settings.DATABASE_USE.getBoolean());
        this.guiManager.init();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // update check
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            new UpdateChecker(this, 89864, getConsole()).check();
        }, 1L);

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
        setLocale(Settings.LANG.getString(), true);
        this.locale.reloadMessages();
        this.data.load();
        this.voucherManager.loadVouchers(true, Settings.DATABASE_USE.getBoolean());
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

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public VoucherManager getVoucherManager() {
        return voucherManager;
    }

    public Config getData() {
        return data;
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
