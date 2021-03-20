package ca.tweetzy.vouchers.database.migrations;

import ca.tweetzy.core.database.DataMigration;
import ca.tweetzy.core.database.MySQLConnector;
import ca.tweetzy.vouchers.Vouchers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 19 2021
 * Time Created: 12:34 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class _1_InitialMigration extends DataMigration {

    public _1_InitialMigration() {
        super(1);
    }

    @Override
    public void migrate(Connection connection, String tablePrefix) throws SQLException {
        String autoIncrement = Vouchers.getInstance().getDatabaseConnector() instanceof MySQLConnector ? " AUTO_INCREMENT" : "";

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE " + tablePrefix + "vouchers (" +
                    "voucher_id VARCHAR(72) PRIMARY KEY, " +
                    "voucher_content TEXT NOT NULL )"
            );
        }
    }
}
