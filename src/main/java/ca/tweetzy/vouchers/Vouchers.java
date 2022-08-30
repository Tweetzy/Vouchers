/*
 * Vouchers
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers;

import ca.tweetzy.feather.FeatherCore;
import ca.tweetzy.feather.FeatherPlugin;
import ca.tweetzy.feather.command.CommandManager;
import ca.tweetzy.feather.comp.enums.CompMaterial;
import ca.tweetzy.feather.config.tweetzy.TweetzyYamlConfig;
import ca.tweetzy.feather.database.DataMigrationManager;
import ca.tweetzy.feather.database.DatabaseConnector;
import ca.tweetzy.feather.database.SQLiteConnector;
import ca.tweetzy.feather.gui.GuiManager;
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.vouchers.api.VouchersAPI;
import ca.tweetzy.vouchers.commands.CommandGive;
import ca.tweetzy.vouchers.commands.CommandImport;
import ca.tweetzy.vouchers.commands.VouchersCommand;
import ca.tweetzy.vouchers.database.DataManager;
import ca.tweetzy.vouchers.database.migrations._1_InitialMigration;
import ca.tweetzy.vouchers.impl.VouchersAPIImplementation;
import ca.tweetzy.vouchers.listeners.BlockListeners;
import ca.tweetzy.vouchers.listeners.VoucherListeners;
import ca.tweetzy.vouchers.model.manager.CooldownManager;
import ca.tweetzy.vouchers.model.manager.RedeemManager;
import ca.tweetzy.vouchers.model.manager.VoucherManager;
import ca.tweetzy.vouchers.settings.Locale;
import ca.tweetzy.vouchers.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


public final class Vouchers extends FeatherPlugin {

	private final TweetzyYamlConfig coreConfig = new TweetzyYamlConfig(this, "config.yml");
	private TweetzyYamlConfig languageConfig;

	private final GuiManager guiManager = new GuiManager(this);
	private final CommandManager commandManager = new CommandManager(this);
	private final VoucherManager voucherManager = new VoucherManager();
	private final RedeemManager redeemManager = new RedeemManager();
	private final CooldownManager cooldownManager = new CooldownManager();

	private VouchersAPI API;

	@SuppressWarnings("FieldCanBeLocal")
	private DatabaseConnector databaseConnector;

	@SuppressWarnings("FieldCanBeLocal")
	private DataManager dataManager;

	@Override
	protected void onFlight() {
		FeatherCore.registerPlugin(this, 7, CompMaterial.PAPER.name());

		if (Settings.setup()) {
			this.languageConfig = new TweetzyYamlConfig(this, "locales" + File.separator + Settings.LANGUAGE.getString() + ".yml");
			Locale.setup();

			Common.setPrefix(Settings.PREFIX.getString());
		}

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration()
		);

		// run migrations for tables
		dataMigrationManager.runMigrations();

		getServer().getPluginManager().registerEvents(new VoucherListeners(), this);
		getServer().getPluginManager().registerEvents(new BlockListeners(), this);

		this.voucherManager.load();
		this.redeemManager.load();

		// ideally initialize after the load
		this.API = new VouchersAPIImplementation();

		this.guiManager.init();
		this.commandManager.registerCommandDynamically(new VouchersCommand()).addSubCommands(new CommandImport(), new CommandGive());
	}

	@Override
	protected void onSleep() {
		shutdownDataManager(this.dataManager);
	}

	@NotNull
	@Override
	public List<TweetzyYamlConfig> getConfigs() {
		return List.of(this.coreConfig);
	}

	// instance
	public static Vouchers getInstance() {
		return (Vouchers) FeatherPlugin.getInstance();
	}

	public static TweetzyYamlConfig getLangConfig() {
		return getInstance().languageConfig;
	}

	// data manager
	public static TweetzyYamlConfig getCoreConfig() {
		return getInstance().coreConfig;
	}

	// data manager
	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}

	public static VoucherManager getVoucherManager() {
		return getInstance().voucherManager;
	}

	public static RedeemManager getRedeemManager() {
		return getInstance().redeemManager;
	}

	public static CooldownManager getCooldownManager() {
		return getInstance().cooldownManager;
	}

	public static VouchersAPI getAPI() {
		return getInstance().API;
	}

	// gui manager
	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}
}
