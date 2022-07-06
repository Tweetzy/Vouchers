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

import ca.tweetzy.feather.config.tweetzy.ConfigEntry;
import ca.tweetzy.feather.config.tweetzy.TweetzyYamlConfig;
import ca.tweetzy.vouchers.Vouchers;

public final class Locale {

	static final TweetzyYamlConfig config = Vouchers.getLangConfig();

	public static final ConfigEntry VOUCHER_EXISTS_ALREADY = new ConfigEntry(config, "Voucher Already Exists", "&cA voucher with that id already exists");
	public static final ConfigEntry NOT_A_NUMBER = new ConfigEntry(config, "Not A Number", "&cThat is not a valid number!");


	public static boolean setup() {
		return config.init();
	}
}
