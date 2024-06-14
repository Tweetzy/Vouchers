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

package ca.tweetzy.vouchers.database;

import ca.tweetzy.flight.database.Callback;
import ca.tweetzy.flight.database.DataManagerAbstract;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.api.voucher.Redeem;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.RewardMode;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.ActiveVoucher;
import ca.tweetzy.vouchers.impl.VoucherRedeem;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.model.ItemEncoder;
import ca.tweetzy.vouchers.model.RewardFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class DataManager extends DataManagerAbstract {

	public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
		super(databaseConnector, plugin);
	}

	public void createVoucher(@NotNull final Voucher voucher, Callback<Voucher> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "voucher (id, name, description, item, options, rewards,reward_mode) VALUES (?, ?, ?, ?, ?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "voucher WHERE id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, voucher.getId().toLowerCase());

				preparedStatement.setString(1, voucher.getId().toLowerCase());
				preparedStatement.setString(2, voucher.getName());
				preparedStatement.setString(3, String.join(";;;", voucher.getDescription()));
				preparedStatement.setString(4, ItemEncoder.encodeItem(voucher.getItem()));
				preparedStatement.setString(5, voucher.getOptions().toJsonString());
				preparedStatement.setString(6, voucher.getRewardJson());
				preparedStatement.setString(7, voucher.getRewardMode().name());

				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractVoucher(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void getVouchers(@NonNull final Callback<List<Voucher>> callback) {
		final List<Voucher> vouchers = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "voucher")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					vouchers.add(extractVoucher(resultSet));
				}

				callback.accept(null, vouchers);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateVoucher(@NonNull final Voucher voucher, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + this.getTablePrefix() + "voucher SET name = ?, description = ?, item = ?, options = ?, rewards = ?, reward_mode = ? WHERE id = ?")) {

				preparedStatement.setString(1, voucher.getName());
				preparedStatement.setString(2, String.join(";;;", voucher.getDescription()));
				preparedStatement.setString(3, ItemEncoder.encodeItem(voucher.getItem()));
				preparedStatement.setString(4, voucher.getOptions().toJsonString());
				preparedStatement.setString(5, voucher.getRewardJson());
				preparedStatement.setString(6, voucher.getRewardMode().name());
				preparedStatement.setString(7, voucher.getId().toLowerCase());

				int result = preparedStatement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteVoucher(@NonNull final String id, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "voucher WHERE id = ?")) {
				statement.setString(1, id);

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void createVoucherRedeem(@NotNull final Redeem redeem, Callback<Redeem> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "voucher_redeem (id, user, voucher, time) VALUES (?, ?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "voucher_redeem WHERE id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, redeem.getId().toString());

				preparedStatement.setString(1, redeem.getId().toString());
				preparedStatement.setString(2, redeem.getUser().toString());
				preparedStatement.setString(3, redeem.getVoucherId().toLowerCase());
				preparedStatement.setLong(4, redeem.getTime());

				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractVoucherRedeem(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void getVoucherRedeems(@NonNull final Callback<List<Redeem>> callback) {
		final List<Redeem> redeems = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "voucher_redeem")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					redeems.add(extractVoucherRedeem(resultSet));
				}

				callback.accept(null, redeems);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteRedeems(@NonNull final UUID player, @NonNull final String voucherId, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "voucher_redeem WHERE user = ? AND voucher = ?")) {
				statement.setString(1, player.toString());
				statement.setString(2, voucherId.toLowerCase());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void deleteAllRedeems(@NonNull final UUID player, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "voucher_redeem WHERE user = ?")) {
				statement.setString(1, player.toString());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void deleteAllRedeems(Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "voucher_redeem")) {

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void deleteAllRedeems(@NonNull final String voucherId, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "voucher_redeem WHERE voucher = ?")) {
				statement.setString(1, voucherId.toLowerCase());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	private Voucher extractVoucher(final ResultSet resultSet) throws SQLException {
		final JsonArray object = JsonParser.parseString(resultSet.getString("rewards")).getAsJsonArray();

		final List<Reward> rewardList = new ArrayList<>();
		object.forEach(el -> rewardList.add(RewardFactory.decode(el.getAsJsonObject().toString())));

		List<String> desc = Arrays.asList(resultSet.getString("description").split(";;;"));
		if (desc.size() == 1 && desc.get(0).isBlank())
			desc = new ArrayList<>();

		return new ActiveVoucher(
				resultSet.getString("id"),
				resultSet.getString("name"),
				ItemEncoder.decodeItem(resultSet.getString("item")),
				new ArrayList<>(desc),
				RewardMode.valueOf(resultSet.getString("reward_mode").toUpperCase()),
				VoucherSettings.decode(resultSet.getString("options")),
				rewardList
		);
	}

	private Redeem extractVoucherRedeem(final ResultSet resultSet) throws SQLException {
		return new VoucherRedeem(
				UUID.fromString(resultSet.getString("id")),
				UUID.fromString(resultSet.getString("user")),
				resultSet.getString("voucher"),
				resultSet.getLong("time")
		);
	}

	private void resolveCallback(@Nullable Callback<?> callback, @NotNull Exception ex) {
		if (callback != null) {
			callback.accept(ex, null);
		} else {
			ex.printStackTrace();
		}
	}
}
