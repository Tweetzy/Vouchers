package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.database.SimpleDatabase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:36 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VouchersDatabase extends SimpleDatabase {

	@Getter
	private final static VouchersDatabase instance = new VouchersDatabase();

	@Override
	protected void onConnected() {
		update("CREATE TABLE IF NOT EXISTS vouchers (");
	}
}
