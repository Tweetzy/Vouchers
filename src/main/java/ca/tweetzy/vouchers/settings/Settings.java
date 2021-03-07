package ca.tweetzy.vouchers.settings;

import ca.tweetzy.core.compatibility.XMaterial;
import ca.tweetzy.core.compatibility.XSound;
import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.core.configuration.ConfigSetting;
import ca.tweetzy.core.configuration.editor.ConfigEditorGui;
import ca.tweetzy.vouchers.Vouchers;

import java.util.Arrays;
import java.util.Collections;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 01 2021
 * Time Created: 5:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Settings {

    static final Config config = Vouchers.getInstance().getCoreConfig();

    public static final ConfigSetting LANG = new ConfigSetting(config, "lang", "en_US", "Default language file");
    public static final ConfigSetting METRICS = new ConfigSetting(config, "metrics", true, "Should the plugin use metrics?", "It simply allows me to see how many servers", "are currently using the vouchers plugin.");

    public static final ConfigSetting DEFAULT_MATERIAL = new ConfigSetting(config, "defaults.material", "PAPER", "The default material to be used for the voucher");
    public static final ConfigSetting DEFAULT_PERMISSION = new ConfigSetting(config, "defaults.permission", "vouchers.use.%voucher_id%", "The default permission for a voucher");
    public static final ConfigSetting DEFAULT_DISPLAYNAME = new ConfigSetting(config, "defaults.display name", "&e%voucher_id%", "The default display name for a voucher");
    public static final ConfigSetting DEFAULT_LORE= new ConfigSetting(config, "defaults.lore", Collections.singletonList("&7Default voucher lore"), "The default lore for a voucher");
    public static final ConfigSetting DEFAULT_GLOW = new ConfigSetting(config, "defaults.use glow", true, "Should the voucher glow by default?");
    public static final ConfigSetting DEFAULT_ASK_TO_CONFIRM = new ConfigSetting(config, "defaults.ask to confirm", true, "Should the player be asked to confirm the redeem by default?");
    public static final ConfigSetting DEFAULT_UNBREAKABLE = new ConfigSetting(config, "defaults.unbreakable", true, "Should the voucher be unbreakable by default");
    public static final ConfigSetting DEFAULT_HIDE_ATTRIBUTES = new ConfigSetting(config, "defaults.hide attributes", true, "Should attributes be hidden by default");
    public static final ConfigSetting DEFAULT_REMOVE_ON_USE = new ConfigSetting(config, "defaults.remove on use", true, "Should the voucher be taken from the player on use?");
    public static final ConfigSetting DEFAULT_SEND_TITLE = new ConfigSetting(config, "defaults.send title", true, "Should a title be sent on redeem?");
    public static final ConfigSetting DEFAULT_SEND_ACTIONBAR = new ConfigSetting(config, "defaults.send actionbar", true, "Should an actionbar be sent on redeem?");
    public static final ConfigSetting DEFAULT_TITLE = new ConfigSetting(config, "defaults.title", "&e&lVoucher Redeemed!", "Default title to be sent on redeem");
    public static final ConfigSetting DEFAULT_SUBTITLE = new ConfigSetting(config, "defaults.subtitle", "&eYou redeemed the %voucher_id% voucher!", "Default subtitle to be sent on redeem");
    public static final ConfigSetting DEFAULT_ACTIONBAR = new ConfigSetting(config, "defaults.actionbar", "&e%voucher_id% voucher redeemed", "Default actionbar to be sent on redeem");
    public static final ConfigSetting DEFAULT_TITLE_FADE_IN = new ConfigSetting(config, "defaults.title fade in", 1, "Default fadein time in seconds for a voucher");
    public static final ConfigSetting DEFAULT_TITLE_STAY = new ConfigSetting(config, "defaults.title stay", 2, "Default stay time in seconds for a voucher");
    public static final ConfigSetting DEFAULT_TITLE_FADE_OUT = new ConfigSetting(config, "defaults.title fade out", 1, "Default fadein time in seconds for a voucher");
    public static final ConfigSetting DEFAULT_COMMANDS = new ConfigSetting(config, "defaults.commands", Collections.singletonList("heal %player%"), "Default commands to be ran on voucher redeem");
    public static final ConfigSetting DEFAULT_BROADCAST_MESSAGES = new ConfigSetting(config, "defaults.broadcast messages", Collections.singletonList("&e%player% redeemed the %voucher_id% voucher"),
            "Default messages to be broadcast to the server",
            "If empty, nothing will be sent",
            "%voucher_id%   -> the id of the voucher",
            "%voucher_title%   -> the display name of the voucher"
    );

    public static final ConfigSetting DEFAULT_PLAYER_MESSAGES = new ConfigSetting(config, "defaults.player messages", Collections.singletonList("&eYou redeemed the %voucher_id% voucher"),
            "Default messages to be sent to the player on voucher redeem",
            "If empty, nothing will be sent",
            "%voucher_id%   -> the id of the voucher",
            "%voucher_title%   -> the display name of the voucher"
    );
    public static final ConfigSetting DEFAULT_REDEEM_SOUND = new ConfigSetting(config, "defaults.redeem sound", XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound().name(), "The default sound to be played on redeem");

    public static final ConfigSetting GUI_BACK_BTN_ITEM = new ConfigSetting(config, "gui.back button.item", "ARROW", "Settings for the back button");
    public static final ConfigSetting GUI_BACK_BTN_NAME = new ConfigSetting(config, "gui.back button.name", "&e<< Back");
    public static final ConfigSetting GUI_BACK_BTN_LORE = new ConfigSetting(config, "gui.back button.lore", Arrays.asList("&7Click the button to go", "&7back to the previous page."));

    public static final ConfigSetting GUI_CLOSE_BTN_ITEM = new ConfigSetting(config, "gui.close button.item", "BARRIER", "Settings for the close button");
    public static final ConfigSetting GUI_CLOSE_BTN_NAME = new ConfigSetting(config, "gui.close button.name", "&cClose");
    public static final ConfigSetting GUI_CLOSE_BTN_LORE = new ConfigSetting(config, "gui.close button.lore", Collections.singletonList("&7Click to close this menu."));

    public static final ConfigSetting GUI_NEXT_BTN_ITEM = new ConfigSetting(config, "gui.next button.item", "ARROW", "Settings for the next button");
    public static final ConfigSetting GUI_NEXT_BTN_NAME = new ConfigSetting(config, "gui.next button.name", "&eNext >>");
    public static final ConfigSetting GUI_NEXT_BTN_LORE = new ConfigSetting(config, "gui.next button.lore", Arrays.asList("&7Click the button to go", "&7to the next page."));

    public static final ConfigSetting GUI_CONFIRM_TITLE = new ConfigSetting(config, "gui.confirm gui.title", "&a&lConfirm Redeem?");
    public static final ConfigSetting GUI_CONFIRM_YES_MATERIAL = new ConfigSetting(config, "gui.confirm gui.items.yes.material", "LIME_STAINED_GLASS_PANE");
    public static final ConfigSetting GUI_CONFIRM_YES_NAME = new ConfigSetting(config, "gui.confirm gui.items.yes.name", "&a&lRedeem");
    public static final ConfigSetting GUI_CONFIRM_YES_LORE = new ConfigSetting(config, "gui.confirm gui.items.yes.lore", Collections.singletonList("&7Click to redeem voucher"));
    public static final ConfigSetting GUI_CONFIRM_NO_MATERIAL = new ConfigSetting(config, "gui.confirm gui.items.no.material", "RED_STAINED_GLASS_PANE");
    public static final ConfigSetting GUI_CONFIRM_NO_NAME = new ConfigSetting(config, "gui.confirm gui.items.no.name", "&c&lCancel");
    public static final ConfigSetting GUI_CONFIRM_NO_LORE = new ConfigSetting(config, "gui.confirm gui.items.no.lore", Collections.singletonList("&7Click to cancel redeeming the voucher"));

    public static final ConfigSetting GUI_LIST_TITLE = new ConfigSetting(config, "gui.list gui.title", "&eListing Vouchers");
    public static final ConfigSetting GUI_LIST_BOTTOM_BAR_ITEM = new ConfigSetting(config, "gui.list gui.bottom bar.item", "LIGHT_GRAY_STAINED_GLASS_PANE", "Settings for the bottom bar");
    public static final ConfigSetting GUI_LIST_BOTTOM_BAR_NAME = new ConfigSetting(config, "gui.list gui.bottom bar.name", "");
    public static final ConfigSetting GUI_LIST_BOTTOM_BAR_LORE = new ConfigSetting(config, "gui.list gui.bottom bar.lore", Collections.singletonList(""));


    public static void setup() {
        config.load();
        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
