package ca.tweetzy.vouchers.settings;

import ca.tweetzy.tweety.settings.SimpleLocalization;

/**
 * The current file has been created by Kiran Hart
 * Date Created: December 17 2021
 * Time Created: 11:01 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class Localization extends SimpleLocalization {

	public static final class VoucherCreation {

		public static String TITLE;
		public static String SUBTITLE;

		private static void init() {
			pathPrefix("Voucher Creation");
			TITLE = getString("Title");
			SUBTITLE = getString("Subtitle");
		}
	}

	public static final class VoucherEdit {

		public static String ENTER_NAME_TITLE;
		public static String ENTER_NAME_SUBTITLE;

		public static String ENTER_DESC_TITLE;
		public static String ENTER_DESC_SUBTITLE;

		public static String ENTER_BROADCAST_TITLE;
		public static String ENTER_BROADCAST_SUBTITLE;

		public static String ENTER_CHAT_MSG_TITLE;
		public static String ENTER_CHAT_MSG_SUBTITLE;

		public static String ENTER_TITLE_TITLE;
		public static String ENTER_TITLE_SUBTITLE;

		public static String ENTER_SUBTITLE_TITLE;
		public static String ENTER_SUBTITLE_SUBTITLE;

		public static String ENTER_ACTIONBAR_TITLE;
		public static String ENTER_ACTIONBAR_SUBTITLE;

		public static String ENTER_PERMISSION_TITLE;
		public static String ENTER_PERMISSION_SUBTITLE;

		public static String ENTER_COMMAND_TITLE;
		public static String ENTER_COMMAND_SUBTITLE;

		public static String ENTER_CHANCE_TITLE;
		public static String ENTER_CHANCE_SUBTITLE;

		private static void init() {
			pathPrefix("Voucher Edit.Enter Name");
			ENTER_NAME_TITLE = getString("Title");
			ENTER_NAME_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Description");
			ENTER_DESC_TITLE = getString("Title");
			ENTER_DESC_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Broadcast");
			ENTER_BROADCAST_TITLE = getString("Title");
			ENTER_BROADCAST_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Chat Message");
			ENTER_CHAT_MSG_TITLE = getString("Title");
			ENTER_CHAT_MSG_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Title");
			ENTER_TITLE_TITLE = getString("Title");
			ENTER_TITLE_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Subtitle");
			ENTER_SUBTITLE_TITLE = getString("Title");
			ENTER_SUBTITLE_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Actionbar");
			ENTER_ACTIONBAR_TITLE = getString("Title");
			ENTER_ACTIONBAR_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Permission");
			ENTER_PERMISSION_TITLE = getString("Title");
			ENTER_PERMISSION_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Command");
			ENTER_COMMAND_TITLE = getString("Title");
			ENTER_COMMAND_SUBTITLE = getString("Subtitle");

			pathPrefix("Voucher Edit.Enter Chance");
			ENTER_CHANCE_TITLE = getString("Title");
			ENTER_CHANCE_SUBTITLE = getString("Subtitle");
		}
	}

	public static final class Error {

		public static String VOUCHER_ALREADY_EXISTS;
		public static String VOUCHER_DOES_NOT_EXIST;
		public static String CHANCE_TOO_HIGH;
		public static String CHANCE_TOO_LOW;
		public static String MISSING_REWARD_ITEM;

		private static void init() {
			pathPrefix("Error");
			VOUCHER_ALREADY_EXISTS = getString("Voucher Already Exists");
			VOUCHER_DOES_NOT_EXIST = getString("Voucher Does Not Exist");
			CHANCE_TOO_HIGH = getString("Chance Too High");
			CHANCE_TOO_LOW = getString("Chance Too Low");
			MISSING_REWARD_ITEM = getString("Missing Reward Item");
		}
	}

	public static final class Success {

		public static String VOUCHER_CREATED;

		private static void init() {
			pathPrefix("Success");
			VOUCHER_CREATED = getString("Voucher Created");
		}
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
