/*
 * Vouchers
 * Copyright 2022-2025 Kiran Hart
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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.config.ConfigEntry;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.FlightSettings;
import ca.tweetzy.vouchers.Vouchers;

import java.util.Arrays;
import java.util.List;

public final class Settings extends FlightSettings {


	public static final ConfigEntry PREFIX = create("prefix", "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8»", "The global prefix for the plugin");
	public static final ConfigEntry LANGUAGE = create("language", "en_us", "The default language for the plugin");

	public static final ConfigEntry LOG_VOUCHER_GIVE_STATUS = create("log voucher give status", true, "If true, vouchers will log if the voucher was placed in the user's inventory or dropped");
	public static final ConfigEntry PREVENT_REDEEM_WHILE_SNEAKING = create("prevent redeem while sneaking", false, "If true, players cannot redeem a voucher while shifting/sneaking");

	public static final ConfigEntry ALLOW_COMMAND_REWARDS = create("security.allow command rewards", true, "If false, command-type rewards never run.");
	public static final ConfigEntry COMMAND_REWARD_PREFIX_WHITELIST = create("security.command reward prefix whitelist", List.<String>of(),
			"If non-empty, the final command (after placeholders) must start with one of these prefixes (case-insensitive). Empty = no prefix restriction.");
	public static final ConfigEntry LOG_COMMAND_REWARDS = create("security.log command rewards at info", true, "Log each command reward execution at INFO with player name and command.");

	public static final ConfigEntry CATEGORIES = create("categories", List.of(
			"id:food name:&eFood Vouchers item:APPLE",
			"id:money name:&aMoney Vouchers item:SUNFLOWER"
	), "Categories for vouchers, these can be applied in each voucher file.");


	public static final ConfigEntry TIME_ALIAS_YEAR = create("time aliases.year", Arrays.asList("y", "year", "years"), "Time aliases for year, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_MONTH = create("time aliases.month", Arrays.asList("mo", "month", "months"), "Time aliases for month, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_WEEK = create("time aliases.week", Arrays.asList("w", "week", "weeks"), "Time aliases for week, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_DAY = create("time aliases.day", Arrays.asList("d", "day", "days"), "Time aliases for day, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_HOUR = create("time aliases.hour", Arrays.asList("h", "hour", "hours"), "Time aliases for hour, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_MINUTE = create("time aliases.minute", Arrays.asList("min", "minute", "minutes"), "Time aliases for minute, Must be in lowercase.");
	public static final ConfigEntry TIME_ALIAS_SECOND = create("time aliases.second", Arrays.asList("s", "second", "seconds"), "Time aliases for second, Must be in lowercase.");


	public static final ConfigEntry GUI_SHARED_ITEMS_BACK_BUTTON = create("gui.shared buttons.back button.item", CompMaterial.DARK_OAK_DOOR.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_EXIT_BUTTON = create("gui.shared buttons.exit button.item", CompMaterial.BARRIER.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON = create("gui.shared buttons.previous button.item", CompMaterial.ARROW.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_NEXT_BUTTON = create("gui.shared buttons.next button.item", CompMaterial.ARROW.name());

	public static final ConfigEntry GUI_CONFIRM_ITEMS_YES = create("gui.user menus.confirm.items.yes.item", CompMaterial.LIME_STAINED_GLASS_PANE.name());
	public static final ConfigEntry GUI_CONFIRM_ITEMS_NO = create("gui.user menus.confirm.items.no.item", CompMaterial.RED_STAINED_GLASS_PANE.name());
	public static final ConfigEntry GUI_CONFIRM_BG = create("gui.user menus.confirm.background", CompMaterial.BLACK_STAINED_GLASS_PANE.name());

	public static final ConfigEntry GUI_REWARD_SELECTION_BG = create("gui.user menus.reward selection.background", CompMaterial.BLACK_STAINED_GLASS_PANE.name());
	public static final ConfigEntry GUI_REWARD_SELECTION_ROWS = create("gui.user menus.reward selection.rows", 6);
	public static final ConfigEntry GUI_REWARD_SELECTION_FILL_SLOTS = create("gui.user menus.reward selection.fill slots", InventoryBorder.getInsideBorders(6));
	public static final ConfigEntry GUI_REWARD_SELECTION_DECORATION = create("gui.user menus.reward selection.decoration", List.of(
			"slot:0-8 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:45-53 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:9 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:18 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:27 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:36 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:17 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:26 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:35 item:LIGHT_BLUE_STAINED_GLASS_PANE",
			"slot:44 item:LIGHT_BLUE_STAINED_GLASS_PANE"
	));


	public static void init() {
		Vouchers.getCoreConfig().init();
	}
}
