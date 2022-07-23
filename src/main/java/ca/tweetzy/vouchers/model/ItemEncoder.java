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

package ca.tweetzy.vouchers.model;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public final class ItemEncoder {

	public String encodeItem(@NonNull final ItemStack itemStack) {
		final YamlConfiguration config = new YamlConfiguration();
		config.set("i", itemStack);
		return config.saveToString();
	}

	@SneakyThrows
	public ItemStack decodeItem(@NonNull final String string) {
		final YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(string);
		} catch (IllegalArgumentException | InvalidConfigurationException e) {
			return null;
		}
		return config.getItemStack("i", null);
	}
}
