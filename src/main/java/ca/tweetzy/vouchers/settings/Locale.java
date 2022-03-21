package ca.tweetzy.vouchers.settings;

import ca.tweetzy.tweety.configuration.Config;
import ca.tweetzy.tweety.configuration.ConfigSetting;
import ca.tweetzy.vouchers.Vouchers;

/**
 * Date Created: March 10 2022
 * Time Created: 1:22 p.m.
 *
 * @author Kiran Hart
 */
public final class Locale {

	static final Config config = Vouchers.getLocaleConfig();

	public static final ConfigSetting VOUCHER_CREATION_TITLE = new ConfigSetting(config, "Voucher Creation.Title", "&eVoucher Creation");
	public static final ConfigSetting VOUCHER_CREATION_SUBTITLE = new ConfigSetting(config, "Voucher Creation.Subtitle", "&7Enter voucher id");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_NAME_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Name.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_NAME_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Name.Subtitle", "&7Enter new display name");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_DESC_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Description.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_DESC_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Description.Subtitle", "&7Enter new description line");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_BROADCAST_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Broadcast.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_BROADCAST_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Broadcast.Subtitle", "&7Enter new broadcast message");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_CHAT_MGG_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Chat Message.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_CHAT_MSG_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Chat Message.Subtitle", "&7Enter new chat message");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_TITLE_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Title.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_TITLE_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Title.Subtitle", "&7Enter new title");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_SUBTITLE_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Subtitle.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_SUBTITLE_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Subtitle.Subtitle", "&7Enter new subtitle");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_ACTIONBAR_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Actionbar.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_ACTIONBAR_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Actionbar.Subtitle", "&7Enter new actionbar");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_PERM_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Permission.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_PERM_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Permission.Subtitle", "&7Enter new permission");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_CMD_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Command.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_CMD_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Command.Subtitle", "&7Enter new command");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_CHANCE_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Chance.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_CHANCE_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Chance.Subtitle", "&7Enter new chance");

	public static final ConfigSetting VOUCHER_EDIT_ENTER_COOLDOWN_TITLE = new ConfigSetting(config, "Voucher Edit.Enter Cooldown.Title", "&eVoucher Edit");
	public static final ConfigSetting VOUCHER_EDIT_ENTER_COOLDOWN_SUBTITLE = new ConfigSetting(config, "Voucher Edit.Enter Cooldown.Subtitle", "&7Enter new cooldown");

	public static final ConfigSetting ERROR_VOUCHER_ALREADY_EXISTS = new ConfigSetting(config, "Error.Voucher Already Exists", "&cThe voucher: &4{voucher_id}&c already exists");
	public static final ConfigSetting ERROR_VOUCHER_DOES_NOT_EXIST = new ConfigSetting(config, "Error.Voucher Does Not Exist", "&cThe voucher: &4{voucher_id}&c doesn't exist");
	public static final ConfigSetting ERROR_CHANCE_TOO_HIGH = new ConfigSetting(config, "Error.Chance Too High", "&cCannot set chance higher than 100%");
	public static final ConfigSetting ERROR_CHANCE_TOO_LOW = new ConfigSetting(config, "Error.Chance Too Low", "&cCannot set chance lower than 1%");
	public static final ConfigSetting ERROR_MISSING_REWARD_ITEM = new ConfigSetting(config, "Error.Missing Reward Item", "&cPlease provide a reward item");
	public static final ConfigSetting ERROR_NO_PERMISSION = new ConfigSetting(config, "Error.No Permission For Voucher", "&cYou do not have permission to use that voucher");
	public static final ConfigSetting ERROR_DID_NOT_GET_REWARD = new ConfigSetting(config, "Error.Did Not Get Reward", "&cYou weren't lucky enough to get that reward");
	public static final ConfigSetting ERROR_COOLDOWN = new ConfigSetting(config, "Error.Cooldown", "&cYou must wait &4{remaining_time}&cs before you can use that voucher");

	public static final ConfigSetting SUCCESS_CREATED = new ConfigSetting(config, "Success.Voucher Created", "&aCreated new voucher with the id: &2{voucher_id}");
	public static final ConfigSetting SUCCESS_DELETED = new ConfigSetting(config, "Success.Voucher Deleted", "&aDeleted voucher with the id: &2{voucher_id}");


	public static void setup() {
		config.load();
		config.setAutoremove(false).setAutosave(true);
		config.saveChanges();
	}
}
