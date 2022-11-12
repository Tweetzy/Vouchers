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

import ca.tweetzy.flight.files.ConfigSetting;
import ca.tweetzy.flight.files.file.YamlFile;
import ca.tweetzy.vouchers.Vouchers;
import lombok.SneakyThrows;

public final class Settings {

	static final YamlFile config = Vouchers.getCoreConfig();

	public static final ConfigSetting PREFIX = new ConfigSetting(config, "prefix", "<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8»", "The global prefix for the plugin");
	public static final ConfigSetting LANGUAGE = new ConfigSetting(config, "language", "english", "The default language for the plugin");
	public static final ConfigSetting REWARD_PICK_IS_GUARANTEED = new ConfigSetting(config, "reward select always gives", true, "If true, the reward picker menu will ignore reward chances");

	public static final ConfigSetting LOG_VOUCHER_GIVE_STATUS = new ConfigSetting(config, "log voucher give status", true, "If true, vouchers will log if the voucher was placed in the user's inventory or dropped");


	@SneakyThrows
	public static void setup() {
		config.applySettings();
		config.save();
	}
}
