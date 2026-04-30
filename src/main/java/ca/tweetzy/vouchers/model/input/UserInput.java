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

package ca.tweetzy.vouchers.model.input;

import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;

public final class UserInput {

	public static <T> void get(
			Player player,
			String title,
			String subTitle,
			TitleInputSuccessCallback<T> onSuccess,
			TitleInputFailureCallback onFailure,
			TitleInputExitCallback onExit,
			Predicate<String> validation,
			Function<String, T> transformer
	) {
		if (player == null || !player.isOnline()) {
			Vouchers.getInstance().getLogger().warning("Attempted to get user input from null or offline player");
			return;
		}
		
		try {
			new TitleInput(Vouchers.getInstance(), player, title, subTitle) {
				@Override
				public boolean onResult(String string) {
					try {
						if (validation.test(string)) {
							T transformed = transformer.apply(string);
							onSuccess.onSuccess(transformed);
							return true;
						}
						if (onFailure != null) {
							onFailure.onFailure(string);
						}
						return false;
					} catch (Exception e) {
						Vouchers.getInstance().getLogger().severe("Error processing user input result: " + e.getMessage());
						e.printStackTrace();
						return false;
					}
				}

				@Override
				public void onExit(Player player) {
					try {
						if (onExit != null) {
							onExit.onExit();
						}
					} catch (Exception e) {
						Vouchers.getInstance().getLogger().severe("Error in user input exit callback: " + e.getMessage());
						e.printStackTrace();
					}
				}
			};
		} catch (Exception e) {
			Vouchers.getInstance().getLogger().severe("Failed to create TitleInput for player " + player.getName() + ": " + e.getMessage());
			e.printStackTrace();
			if (onExit != null) {
				try {
					onExit.onExit();
				} catch (Exception ex) {
					Vouchers.getInstance().getLogger().severe("Error calling exit callback after TitleInput failure: " + ex.getMessage());
				}
			}
		}
	}

	public static void get(
			Player player,
			String title,
			String subTitle,
			TitleInputSuccessCallback<String> onSuccess,
			TitleInputFailureCallback onFailure,
			TitleInputExitCallback onExit,
			Predicate<String> validation
	) {
		get(player, title, subTitle, onSuccess, onFailure, onExit, validation, Function.identity());
	}
}
