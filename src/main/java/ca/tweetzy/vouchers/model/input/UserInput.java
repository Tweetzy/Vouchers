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
		new TitleInput(Vouchers.getInstance(), player, title, subTitle) {
			@Override
			public boolean onResult(String string) {
				if (validation.test(string)) {
					T transformed = transformer.apply(string);
					onSuccess.onSuccess(transformed);
					return true;
				}
				onFailure.onFailure(string);
				return false;
			}

			@Override
			public void onExit(Player player) {
				onExit.onExit();
			}
		};
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
