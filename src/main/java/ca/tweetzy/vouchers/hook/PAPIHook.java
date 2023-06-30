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

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.model.FlagExtractor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class PAPIHook extends PlaceholderExpansion {

	public static String tryReplace(Player player, String msg) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			msg = PlaceholderAPI.setPlaceholders(player, msg);
		return msg;
	}

	public static List<String> tryReplace(Player player, List<String> msgs) {
		return msgs.stream().map(line -> tryReplace(player, line)).collect(Collectors.toList());
	}

	@Override
	public @NotNull
	String getIdentifier() {
		return "vouchers";
	}

	@Override
	public @NotNull
	String getAuthor() {
		return "KiranHart";
	}

	@Override
	public @NotNull
	String getVersion() {
		return "1.0.0";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String params) {
		final String[] paramSplit = params.split("_");

		if (paramSplit[0].equalsIgnoreCase("redeems")) {
			if (paramSplit.length < 2) return null;

			final String voucherId = FlagExtractor.grabWordsUntilFlag(paramSplit, 1, "-a");
			final int totalRedeems = Vouchers.getRedeemManager().getTotalRedeems(player.getUniqueId(), voucherId);

			return String.valueOf(totalRedeems);
		}

		return null;
	}
}
