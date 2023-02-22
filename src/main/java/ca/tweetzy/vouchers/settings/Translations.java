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

package ca.tweetzy.vouchers.settings;

import ca.tweetzy.flight.settings.TranslationEntry;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.vouchers.Vouchers;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class Translations extends TranslationManager {

	public Translations(@NonNull JavaPlugin plugin) {
		super(plugin);
		this.mainLanguage = Settings.LANGUAGE.getString();
	}

	public static final TranslationEntry VOUCHER_EXISTS_ALREADY = create("error.Voucher Already Exists", "&cA voucher with that id already exists");
	public static final TranslationEntry REDEEM_LIMIT_REACHED = create("error.Redeem Limit Reached", "&cYou cannot redeem that voucher anymore!");
	public static final TranslationEntry NOT_ALLOWED_TO_USE = create("error.Not Allowed To Use", "&cYou are not allowed to use that voucher");
	public static final TranslationEntry WAIT_FOR_COOLDOWN = create("error.Wait For Cooldown", "&cYou can redeem that voucher in &4%cooldown_time% &cseconds");


	public static final TranslationEntry GUI_CONFIRM_TITLE = create("Gui.Confirm.Title", "&bVouchers &8> &7Confirm Action");
	public static final TranslationEntry GUI_CONFIRM_ITEM_YES_NAME = create("Gui.Confirm.Items.Yes.Name", "&a&lConfirm");
	public static final TranslationEntry GUI_CONFIRM_ITEM_YES_LORE = create("Gui.Confirm.Items.Yes.Lore", "&b&lClick &8» &7To confirm action");
	public static final TranslationEntry GUI_CONFIRM_ITEM_NO_NAME = create("Gui.Confirm.Items.No.Name", "&c&lCancel");
	public static final TranslationEntry GUI_CONFIRM_ITEM_NO_LORE = create("Gui.Confirm.Items.No.Lore", "&b&lClick &8» &7To cancel action");

	public static final TranslationEntry GUI_REWARD_SELECT_TITLE = create("Gui.Select Reward.Title", "&bVouchers &8> &7Select Reward");
	public static final TranslationEntry GUI_REWARD_SELECT_CMD_NAME = create("Gui.Select Reward.Items.Command.Name", "&B&lCommand Reward");
	public static final TranslationEntry GUI_REWARD_SELECT_CMD_LORE = create("Gui.Select Reward.Items.Command.Lore",
			"&7Command&f: &b%reward_command%",
			"&7Chance&F: &b%reward_chance%",
			"",
			"&b&lClick &8» &7To select this reward"
	);

	public static final TranslationEntry GUI_REWARD_SELECT_ITEM_LORE = create("Gui.Select Reward.Items.Item.Lore",
			"&7Chance&F: &b%reward_chance%",
			"",
			"&b&lClick &8» &7To select this reward"
	);

	public static final TranslationEntry VOUCHER_REWARD_INFO_HEADER = create("info.reward.structure.header",
			"<center><GRADIENT:fc67fa>&lVoucher Rewards</GRADIENT:f4c4f3>",
			""
	);

	public static final TranslationEntry VOUCHER_REWARD_INFO_FOOTER = create("info.reward.structure.footer",
			""

	);

	public static final TranslationEntry VOUCHER_REWARD_INFO_COMMAND = create("info.reward.command",
			"<center>&a&l+ &e%reward_command%"
	);

	public static final TranslationEntry VOUCHER_REWARD_INFO_ITEM = create("info.reward.item",
			"<center>&a&l+ &fx&7%item_quantity% &e%item_name%"
	);


	public static void init() {
		new Translations(Vouchers.getInstance()).setup();
	}
}
