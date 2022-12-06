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

package ca.tweetzy.vouchers.hook;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class PAPIHook {

	public String tryReplace(Player player, String msg) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			msg = PlaceholderAPI.setPlaceholders(player, msg);
		return msg;
	}

	public List<String> tryReplace(Player player, List<String> msgs) {
		return msgs.stream().map(line -> tryReplace(player, line)).collect(Collectors.toList());
	}
}
