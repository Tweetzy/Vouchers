/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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

package ca.tweetzy.vouchers.model;

import ca.tweetzy.vouchers.settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConverter {

	private static Pattern TIME_PATTERN;

	static {
		updateTimePattern();
	}

	public static void updateTimePattern() {
		List<String> allTimeUnits = new ArrayList<>();
		allTimeUnits.addAll(Settings.TIME_ALIAS_YEAR.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_MONTH.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_WEEK.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_DAY.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_HOUR.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_MINUTE.getStringList());
		allTimeUnits.addAll(Settings.TIME_ALIAS_SECOND.getStringList());

		String timeUnitsRegex = String.join("|", allTimeUnits);
		TIME_PATTERN = Pattern.compile("(\\d+)\\s*(" + timeUnitsRegex + "s?)(?:\\s|$)");
	}

	public static long convertHumanReadableTime(String time) {
		Matcher matcher = TIME_PATTERN.matcher(time.toLowerCase());
		long totalMilliseconds = 0;

		while (matcher.find()) {
			int amount = Integer.parseInt(matcher.group(1));
			String unit = matcher.group(2).toLowerCase();

			// Remove trailing 's' if present
			if (unit.endsWith("s") && unit.length() > 1) {
				unit = unit.substring(0, unit.length() - 1);
			}

			long multiplier = getMultiplierForUnit(unit);
			totalMilliseconds += amount * multiplier;
		}
		return totalMilliseconds;
	}

	public static String convertSecondsToHumanReadable(long seconds) {
		long[] multipliers = {
				31536000L, // Year
				2592000L,  // Month (approx.)
				604800L,   // Week
				86400L,    // Day
				3600L,     // Hour
				60L,       // Minute
				1L         // Second
		};

		String[] units = {
				"year", "month", "week", "day", "hour", "minute", "second"
		};

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < multipliers.length; i++) {
			if (multipliers[i] > 0 && seconds >= multipliers[i]) {
				long count = seconds / multipliers[i];
				seconds %= multipliers[i];

				if (result.length() > 0) {
					result.append(" ");
				}
				result.append(count).append(" ").append(units[i]);
				if (count > 1) {
					result.append("s");
				}
			}
		}

		if (result.length() == 0) {
			return "0 seconds";
		}

		return result.toString();
	}

	private static long getMultiplierForUnit(String unit) {
		// Check for more specific aliases first
		if (Settings.TIME_ALIAS_MINUTE.getStringList().contains(unit)) {
			return 60 * 1000L;
		} else if (Settings.TIME_ALIAS_MONTH.getStringList().contains(unit)) {
			return 30 * 24 * 60 * 60 * 1000L;
		} else if (Settings.TIME_ALIAS_YEAR.getStringList().contains(unit)) {
			return 365 * 24 * 60 * 60 * 1000L;
		} else if (Settings.TIME_ALIAS_WEEK.getStringList().contains(unit)) {
			return 7 * 24 * 60 * 60 * 1000L;
		} else if (Settings.TIME_ALIAS_DAY.getStringList().contains(unit)) {
			return 24 * 60 * 60 * 1000L;
		} else if (Settings.TIME_ALIAS_HOUR.getStringList().contains(unit)) {
			return 60 * 60 * 1000L;
		} else if (Settings.TIME_ALIAS_SECOND.getStringList().contains(unit)) {
			return 1000L;
		} else {
			return 0L;
		}
	}
}