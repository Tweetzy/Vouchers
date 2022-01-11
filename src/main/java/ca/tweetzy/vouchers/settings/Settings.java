package ca.tweetzy.vouchers.settings;

import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.settings.SimpleSettings;
import ca.tweetzy.vouchers.model.ConfigItem;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 17 2021
 * Time Created: 11:00 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Settings extends SimpleSettings {

	public static String PREFIX;

	private static void init() {
		pathPrefix(null);
		PREFIX = getString("Prefix");
	}

	public static final class ConfirmMenu {

		public static String TITLE;
		public static Integer ROWS;
		public static CompMaterial BACKGROUND_ITEM;
		public static ConfigItem CONFIRM_ITEM;
		public static ConfigItem CANCEL_ITEM;

		private static void init() {
			pathPrefix("Confirm Menu");

			TITLE = getString("Title");
			ROWS = getInteger("Rows");
			BACKGROUND_ITEM = getMaterial("Background Item");
			CONFIRM_ITEM = get("Confirm Item", ConfigItem.class);
			CANCEL_ITEM = get("Cancel Item", ConfigItem.class);
		}
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
