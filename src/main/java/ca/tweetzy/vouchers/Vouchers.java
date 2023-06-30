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

import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.command.CommandManager;
import ca.tweetzy.flight.database.DataMigrationManager;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.SQLiteConnector;
import ca.tweetzy.flight.gui.GuiManager;
import ca.tweetzy.flight.hooks.PlaceholderAPIHook;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.api.VouchersAPI;
import ca.tweetzy.vouchers.commands.*;
import ca.tweetzy.vouchers.database.DataManager;
import ca.tweetzy.vouchers.database.migrations._1_InitialMigration;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.impl.VouchersAPIImplementation;
import ca.tweetzy.vouchers.listeners.BlockListeners;
import ca.tweetzy.vouchers.listeners.VoucherListeners;
import ca.tweetzy.vouchers.model.manager.CooldownManager;
import ca.tweetzy.vouchers.model.manager.RedeemManager;
import ca.tweetzy.vouchers.model.manager.VoucherManager;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.Bukkit;


public final class Vouchers extends FlightPlugin {

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
		Settings.init();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getString());
		Common.setPluginName("<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3>");

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager, new _1_InitialMigration());

		// run migrations for tables
		dataMigrationManager.runMigrations();

		getServer().getPluginManager().registerEvents(new VoucherListeners(), this);
		getServer().getPluginManager().registerEvents(new BlockListeners(), this);

		this.voucherManager.load();
		this.redeemManager.load();

		// ideally initialize after the load
		this.API = new VouchersAPIImplementation();

		this.guiManager.init();
		this.commandManager.registerCommandDynamically(new VouchersCommand()).addSubCommands(
				new CommandImport(),
				new CommandGive(),
				new CommandClearRedeems(),
				new CommandReload()
		);

		// Placeholder API
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PAPIHook().register();
		}

	}

	@Override
	protected int getBStatsId() {
		return 10530;
	}

	@Override
	protected void onSleep() {
		shutdownDataManager(this.dataManager);
	}

	// instance
	public static Vouchers getInstance() {
		return (Vouchers) FlightPlugin.getInstance();
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
