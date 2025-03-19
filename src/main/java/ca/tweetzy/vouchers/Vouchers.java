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
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.api.VouchersAPI;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.commands.CommandReload;
import ca.tweetzy.vouchers.commands.VouchersCommand;
import ca.tweetzy.vouchers.database.DataManager;
import ca.tweetzy.vouchers.database.migrations.v3._1_InitialMigration;
import ca.tweetzy.vouchers.database.migrations.v3._2_CategoryMigration;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.listeners.BlockListeners;
import ca.tweetzy.vouchers.listeners.VoucherListeners;
import ca.tweetzy.vouchers.model.manager.VoucherManager;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;


public final class Vouchers extends FlightPlugin {
	private volatile boolean shuttingDown = false;

	//==========================================================================//
	private final Map<String, Long> lastModifiedTimes = new HashMap<>();

	private static TaskChainFactory taskChainFactory;

	private final GuiManager guiManager = new GuiManager(this);
	private final CommandManager commandManager = new CommandManager(this);
	private final VoucherManager voucherManager = new VoucherManager();

	private VouchersAPI API;

	@SuppressWarnings("FieldCanBeLocal")
	private DatabaseConnector databaseConnector;

	@SuppressWarnings("FieldCanBeLocal")
	private DataManager dataManager;

	// folder watching
	private WatchService dataWatcher;


	@SneakyThrows
	@Override
	protected void onFlight() {
		Settings.init();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getString());
		Common.setPluginName("<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF>");

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager, new _1_InitialMigration(), new _2_CategoryMigration());

		// run migrations for tables
		dataMigrationManager.runMigrations();

		getServer().getPluginManager().registerEvents(new VoucherListeners(), this);
		getServer().getPluginManager().registerEvents(new BlockListeners(), this);

		// ideally initialize after the load
		taskChainFactory = BukkitTaskChainFactory.create(this);

		this.guiManager.init();
		this.commandManager.registerCommandDynamically(new VouchersCommand()).addSubCommands(
				new CommandReload()
		);

		this.voucherManager.load();

		// Placeholder API
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PAPIHook().register();
		}


		// SETUP WATCHER
		this.dataWatcher = FileSystems.getDefault().newWatchService();
		final Path pluginFolder = getDataFolder().toPath();
		final Path monitorFolder = pluginFolder.resolve("voucher-files");
		if (!new File(String.valueOf(monitorFolder)).exists()) {
			new File(String.valueOf(monitorFolder)).mkdir();
		}


		monitorFolder.register(dataWatcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

		Thread eventHandler = new Thread(() -> {
			while (!shuttingDown) {
				WatchKey key;
				try {
					key = dataWatcher.take();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				} catch (ClosedWatchServiceException e) {
					// Handle the exception during shutdown
					if (shuttingDown) {
						return;
					} else {
						// Handle unexpected closure
						System.err.println("WatchService closed unexpectedly.");
						return;
					}
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}

					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					String fileName = ev.context().toString();

					// Get the current file timestamp
					Path filePath = monitorFolder.resolve(fileName);
					long currentTimestamp = filePath.toFile().lastModified();

					// Perform actions based on event type
					if (fileName.endsWith(".json")) {
						final String normalFileName = fileName.replace(".json", "");

						try {
							if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
								try {
									final Voucher voucher = this.voucherManager.loadVoucherFromFile(filePath.toFile());

									if (voucher != null && !this.voucherManager.getManagerContent().containsKey(normalFileName)) {
										this.voucherManager.add(normalFileName, voucher);
									}
								} catch (IllegalStateException ignored) {
								}

							} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
								lastModifiedTimes.remove(normalFileName);
							} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
								try {
									// Check if the file was modified recently
									long currentTime = System.currentTimeMillis();
									if (lastModifiedTimes.containsKey(fileName) && currentTime - lastModifiedTimes.get(fileName) < 500) {
										continue;
									}

									final Voucher voucher = this.voucherManager.loadVoucherFromFile(filePath.toFile());
									if (voucher != null) {
										this.voucherManager.remove(normalFileName);
										this.voucherManager.add(normalFileName, voucher);
									}
								} catch (IllegalStateException ignored) {
								}
							}
						}catch (Exception ignored) {

						}
						// Update the file timestamp in the map
						lastModifiedTimes.put(fileName, currentTimestamp);
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		});
		eventHandler.start();

	}

	@Override
	protected int getBStatsId() {
		return 10530;
	}

	@Override
	protected void onSleep() {
		shuttingDown = true;
		try {
			if (dataWatcher != null) {
				dataWatcher.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		shutdownDataManager(this.dataManager);
	}

	// instance
	public static Vouchers getInstance() {
		return (Vouchers) FlightPlugin.getInstance();
	}

	public static <T> TaskChain<T> newChain() {
		return taskChainFactory.newChain();
	}

	public static <T> TaskChain<T> newSharedChain(String name) {
		return taskChainFactory.newSharedChain(name);
	}

	// data manager
	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}

	public static VouchersAPI getAPI() {
		return getInstance().API;
	}

	public static VoucherManager getVoucherManger() {
		return getInstance().voucherManager;
	}

	// gui manager
	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}
}
