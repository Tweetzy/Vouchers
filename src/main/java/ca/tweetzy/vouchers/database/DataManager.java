package ca.tweetzy.vouchers.database;

import ca.tweetzy.core.database.DataManagerAbstract;
import ca.tweetzy.core.database.DatabaseConnector;
import ca.tweetzy.vouchers.Helpers;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 19 2021
 * Time Created: 12:28 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class DataManager extends DataManagerAbstract {

    public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
        super(databaseConnector, plugin);
    }

    public void createVoucher(Voucher voucher, Consumer<Boolean> result) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String createVoucher = "INSERT IGNORE INTO " + this.getTablePrefix() + "vouchers SET voucher_id = ?, voucher_content = ?";
            try (PreparedStatement statement = connection.prepareStatement(createVoucher)) {
                statement.setString(1, voucher.getId());
                statement.setString(2, Helpers.toString(voucher));
                int status = statement.executeUpdate();
                this.sync(() -> result.accept(status == 0));
            }
        }));
    }

    public void removeVoucher(String voucherId, Consumer<Boolean> result) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String removeVoucher = "DELETE FROM " + this.getTablePrefix() + "vouchers WHERE voucher_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(removeVoucher)) {
                statement.setString(1, voucherId);
                int status = statement.executeUpdate();
                this.sync(() -> result.accept(status == 0));
            }
        }));
    }

    public void getVouchers(Consumer<List<Voucher>> callback) {
        List<Voucher> vouchers = new ArrayList<>();
        this.async(() -> this.databaseConnector.connect(connection -> {
            try (Statement statement = connection.createStatement()) {
                String getVouchers = "SELECT * FROM " + this.getTablePrefix() + "vouchers";
                ResultSet result = statement.executeQuery(getVouchers);
                while (result.next()) {
                    vouchers.add((Voucher) Helpers.fromString(result.getString("voucher_content")));
                }
            }

            this.sync(() -> callback.accept(vouchers));
        }));
    }
}
