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

import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public final class Giver {

	public void giveItem(@NonNull final Player player, @NonNull final ItemStack item) {
		giveItem(player, item, false);
	}

	public void giveItem(@NonNull final Player player, @NonNull final ItemStack item, boolean voucher) {
		if (player.getInventory().firstEmpty() == -1) {
			player.getWorld().dropItemNaturally(player.getLocation(), item);
			if (voucher && Settings.LOG_VOUCHER_GIVE_STATUS.getBoolean())
				Common.log(TranslationManager.string(Translations.DROP_NEAR_PLAYER, "player_name", player.getName()));
		} else {
			player.getInventory().addItem(item);
			if (voucher && Settings.LOG_VOUCHER_GIVE_STATUS.getBoolean())
				Common.log(TranslationManager.string(Translations.GIVEN_TO_PLAYER, "player_name", player.getName()));

		}
	}
}
