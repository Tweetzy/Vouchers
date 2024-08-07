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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.config.ConfigEntry;
import ca.tweetzy.flight.settings.FlightSettings;
import ca.tweetzy.vouchers.Vouchers;

public final class Settings extends FlightSettings {


	public static final ConfigEntry PREFIX = create("prefix", "<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8»", "The global prefix for the plugin");
	public static final ConfigEntry LANGUAGE = create("language", "en_us", "The default language for the plugin");
	public static final ConfigEntry REWARD_PICK_IS_GUARANTEED = create("reward select always gives", true, "If true, the reward picker menu will ignore reward chances");
	public static final ConfigEntry HAVING_VOUCHER_PERM_BLOCKS_USAGE = create("voucher permission blocks usage", false, "If true, anyone can redeem a voucher, but if they have the permission, it prevents redeems");

	public static final ConfigEntry LOG_VOUCHER_GIVE_STATUS = create("log voucher give status", true, "If true, vouchers will log if the voucher was placed in the user's inventory or dropped");
	public static final ConfigEntry SHOW_VOUCHER_REWARD_INFO = create("show voucher reward info", true, "If true, vouchers will tell the player what they got");
	public static final ConfigEntry PREVENT_REDEEM_WHILE_SNEAKING = create("prevent redeem while sneaking", false, "If true, players cannot redeem a voucher while shifting/sneaking");
	public static final ConfigEntry BROADCAST_INDIVIDUAL_REWARDS = create("broadcast individual rewards", true, "If true, each reward will be broadcasted assuming you have a broadcast msg ");

	public static final ConfigEntry GUI_SHARED_ITEMS_BACK_BUTTON = create("gui.shared buttons.back button.item", CompMaterial.DARK_OAK_DOOR.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_EXIT_BUTTON = create("gui.shared buttons.exit button.item", CompMaterial.BARRIER.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON = create("gui.shared buttons.previous button.item", CompMaterial.ARROW.name());
	public static final ConfigEntry GUI_SHARED_ITEMS_NEXT_BUTTON = create("gui.shared buttons.next button.item", CompMaterial.ARROW.name());


	public static void init() {
		Vouchers.getCoreConfig().init();
	}
}
