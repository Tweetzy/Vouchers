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

package ca.tweetzy.vouchers.impl.importer;

import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.Importer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class VouchersImporter implements Importer {

	@Override
	public String getName() {
		return "Vouchers";
	}

	@Override
	public String getAuthor() {
		return "Kiran Hart";
	}

	@Override
	public void load() {
		final File file = new File(Vouchers.getInstance().getDataFolder() + File.separator + "data.db");
		if (!file.exists()) {
			return;
		}

		final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		configuration.getStringList("vouchers").forEach(key -> {
			Common.log(key);
		});

	}
}
