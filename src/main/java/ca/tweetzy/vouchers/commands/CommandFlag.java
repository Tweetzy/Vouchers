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

package ca.tweetzy.vouchers.commands;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.EntityType;

@UtilityClass
public final class CommandFlag {

	public <T> T get(Class<T> type, @NonNull final String flagName, T def, String... args) {
		int flagIndex = -1;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-" + flagName)) {
				flagIndex = i;
				break;
			}
		}

		if (flagIndex == -1) return type.cast(def);

		try {
			if (type.isAssignableFrom(EntityType.class)) {
				try {
					return (T) EntityType.valueOf(args[flagIndex + 1].toUpperCase());
				} catch (IllegalArgumentException e) {
					return type.cast(def);
				}
			}

			if (type.isAssignableFrom(Integer.class)) {
				try {
					return (T) Integer.valueOf(Integer.parseInt(args[flagIndex + 1]));
				} catch (NumberFormatException e) {
					return type.cast(def);
				}
			}

			return type.cast(args[flagIndex + 1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return type.cast(def);
		}
	}
}
