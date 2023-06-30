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

	public static final TranslationEntry VOUCHER_EXISTS_ALREADY = create("error.voucher already exists", "&cA voucher with that id already exists");
	public static final TranslationEntry VOUCHER_NOT_FOUND = create("error.voucher not found", "&cCannot find a voucher with the id: &4%voucher_id%");
	public static final TranslationEntry REDEEM_LIMIT_REACHED = create("error.redeem limit reached", "&cYou cannot redeem that voucher anymore!");
	public static final TranslationEntry REDEEM_HISTORY_CLEARED = create("error.redeem history cleared", "&aSuccessfully cleared the redeem history of specified player(s)");
	public static final TranslationEntry NOT_ALLOWED_TO_USE = create("error.not allowed to use", "&cYou are not allowed to use that voucher");
	public static final TranslationEntry WAIT_FOR_COOLDOWN = create("error.wait for cooldown", "&cYou can redeem that voucher in &4%cooldown_time% &cseconds");

	public static final TranslationEntry DROP_NEAR_PLAYER = create("info.give.dropped near player", "Voucher was dropped near player: %player_name% (full inventory)");
	public static final TranslationEntry GIVEN_TO_PLAYER = create("info.give.placed into inventory", "Voucher was placed in player: %player_name%'s inventory");


	public static final TranslationEntry GUI_CONFIRM_TITLE = create("gui.confirm.title", "&bVouchers &8> &7Confirm Action");
	public static final TranslationEntry GUI_CONFIRM_ITEM_YES_NAME = create("gui.confirm.items.yes.name", "&a&lconfirm");
	public static final TranslationEntry GUI_CONFIRM_ITEM_YES_LORE = create("gui.confirm.items.yes.lore", "&b&lClick &8» &7To confirm action");
	public static final TranslationEntry GUI_CONFIRM_ITEM_NO_NAME = create("gui.confirm.items.no.name", "&c&lCancel");
	public static final TranslationEntry GUI_CONFIRM_ITEM_NO_LORE = create("gui.confirm.items.no.lore", "&b&lClick &8» &7To cancel action");

	public static final TranslationEntry GUI_REWARD_SELECT_TITLE = create("gui.select reward.title", "&bVouchers &8> &7Select Reward");
	public static final TranslationEntry GUI_REWARD_SELECT_CMD_NAME = create("gui.select reward.items.command.name", "&B&lCommand Reward");
	public static final TranslationEntry GUI_REWARD_SELECT_CMD_LORE = create("gui.select reward.items.command.lore",
			"&7Command&f: &b%reward_command%",
			"&7Chance&F: &b%reward_chance%",
			"",
			"&b&lClick &8» &7To select this reward"
	);

	public static final TranslationEntry GUI_REWARD_SELECT_ITEM_LORE = create("gui.select reward.items.item.lore",
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
