package ca.tweetzy.vouchers.settings;

import ca.tweetzy.tweety.configuration.Config;
import ca.tweetzy.tweety.configuration.ConfigSetting;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;

import java.util.Arrays;
import java.util.Collections;

/**
 * Date Created: March 10 2022
 * Time Created: 1:25 p.m.
 *
 * @author Kiran Hart
 */
public final class Settings {

	static final Config config = Vouchers.getCoreConfig();

	public static final ConfigSetting GUI_BACK_BTN_ITEM = new ConfigSetting(config, "settings.guis.back button.item", CompMaterial.DARK_OAK_DOOR.name());
	public static final ConfigSetting GUI_BACK_BTN_NAME = new ConfigSetting(config, "settings.guis.back button.name", "#13c279Back");
	public static final ConfigSetting GUI_BACK_BTN_LORE = new ConfigSetting(config, "settings.guis.back button.lore", Collections.singletonList(
			"&dClick &7to go back"
	));

	public static final ConfigSetting GUI_PREV_BTN_ITEM = new ConfigSetting(config, "settings.guis.previous button.item", CompMaterial.ARROW.name());
	public static final ConfigSetting GUI_PREV_BTN_NAME = new ConfigSetting(config, "settings.guis.previous button.name", "#13c279Previous Page");
	public static final ConfigSetting GUI_PREV_BTN_LORE = new ConfigSetting(config, "settings.guis.previous button.lore", Collections.singletonList(
			"&dClick &7to go back a page"
	));

	public static final ConfigSetting GUI_NEXT_BTN_ITEM = new ConfigSetting(config, "settings.guis.next button.item", CompMaterial.ARROW.name());
	public static final ConfigSetting GUI_NEXT_BTN_NAME = new ConfigSetting(config, "settings.guis.next button.name", "#13c279Next Page");
	public static final ConfigSetting GUI_NEXT_BTN_LORE = new ConfigSetting(config, "settings.guis.next button.lore", Collections.singletonList(
			"&dClick &7to go to next page"
	));

	public static final ConfigSetting LANG = new ConfigSetting(config, "lang", "en_US", "Default language file");
	public static final ConfigSetting PREFIX = new ConfigSetting(config, "prefix", "&8[&eVouchers&8]");
	public static final ConfigSetting COMMAND_ALIASES = new ConfigSetting(config, "Command Aliases", Arrays.asList("vouchers", "voucher"));
	public static final ConfigSetting AUTO_BSTATS = new ConfigSetting(config, "Auto bStats", true);

	public static final ConfigSetting VOUCHER_LIST_MENU_TITLE = new ConfigSetting(config, "Voucher List Menu.Title", "&eVouchers");
	public static final ConfigSetting VOUCHER_LIST_MENU_BACKGROUND = new ConfigSetting(config, "Voucher List Menu.Background Item", "BLACK_STAINED_GLASS_PANE");

	public static final ConfigSetting CONFIRM_MENU_TITLE = new ConfigSetting(config, "Confirm Menu.Title", "&eConfirm Redeem");
	public static final ConfigSetting CONFIRM_MENU_ROWS = new ConfigSetting(config, "Confirm Menu.Rows", 3);
	public static final ConfigSetting CONFIRM_MENU_BACKGROUND = new ConfigSetting(config, "Confirm Menu.Background Item", "BLACK_STAINED_GLASS_PANE");

	public static final ConfigSetting CONFIRM_MENU_ITEMS_CONFIRM_MATERIAL = new ConfigSetting(config, "Confirm Menu.Confirm Item.Material", "LIME_STAINED_GLASS_PANE");
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CONFIRM_NAME = new ConfigSetting(config, "Confirm Menu.Confirm Item.Name", "&a&lConfirm");
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CONFIRM_LORE = new ConfigSetting(config, "Confirm Menu.Confirm Item.Lore", Arrays.asList(
			"",
			"&dClick &7to confirm redeem"
	));
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CONFIRM_SLOTS = new ConfigSetting(config, "Confirm Menu.Cancel Item.Slots", Arrays.asList(14, 15, 16));

	public static final ConfigSetting CONFIRM_MENU_ITEMS_CANCEL_MATERIAL = new ConfigSetting(config, "Confirm Menu.Cancel Item.Material", "RED_STAINED_GLASS_PANE");
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CANCEL_NAME = new ConfigSetting(config, "Confirm Menu.Cancel Item.Name", "&c&lCancel");
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CANCEL_LORE = new ConfigSetting(config, "Confirm Menu.Cancel Item.Lore", Arrays.asList(
			"",
			"&dClick &7to cancel redeem"
	));
	public static final ConfigSetting CONFIRM_MENU_ITEMS_CANCEL_SLOTS = new ConfigSetting(config, "Confirm Menu.Cancel Item.Slots", Arrays.asList(10, 11, 12));

	public static final ConfigSetting REWARD_SELECT_MENU_TITLE = new ConfigSetting(config, "Reward Select Menu.Title", "&eSelect a Reward");
	public static final ConfigSetting REWARD_SELECT_MENU_ROWS = new ConfigSetting(config, "Reward Select Menu.Rows", 3);
	public static final ConfigSetting REWARD_SELECT_MENU_ALWAYS_GIVE = new ConfigSetting(config, "Reward Select Menu.Always Give", true, "If true, vouchers will ignore that reward's chance and always give the user the item they selected");
	public static final ConfigSetting REWARD_SELECT_MENU_BACKGROUND = new ConfigSetting(config, "Reward Select Menu.Background Item", "BLACK_STAINED_GLASS_PANE");
	public static final ConfigSetting REWARD_SELECT_MENU_REWARD_SLOTS = new ConfigSetting(config, "Reward Select Menu.Reward Slots", Arrays.asList(
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26
	));
	public static final ConfigSetting REWARD_SELECT_MENU_COMMAND_MATERIAL = new ConfigSetting(config, "Reward Select Menu.Command Item", "PAPER");
	public static final ConfigSetting REWARD_SELECT_MENU_COMMAND_NAME = new ConfigSetting(config, "Reward Select Menu.Command Name", "&e&lCommand Reward");
	public static final ConfigSetting REWARD_SELECT_MENU_COMMAND_LORE = new ConfigSetting(config, "Reward Select Menu.Command Lore", Arrays.asList(
			"",
			"&7Command&f: &e{reward_command}",
			""
	));
	public static final ConfigSetting REWARD_SELECT_MENU_COMMAND_LORE_CHANCE = new ConfigSetting(config, "Reward Select Menu.Command Lore Chance", Arrays.asList(
			"",
			"&7Command&f: &e{reward_command}",
			"&7Chance&f: &a{reward_chance}&f%",
			""
	));


	public static void setup() {
		config.load();
		config.setAutoremove(false).setAutosave(true);
		config.saveChanges();
	}
}
