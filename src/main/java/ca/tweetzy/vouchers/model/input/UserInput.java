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
			TitleInputSuccessCallback onSuccess,
			TitleInputFailureCallback onFailure,
			TitleInputExitCallback onExit,
			Predicate<String> validation,
			Function<String, T> transformer
	) {
		TitleInput input = new TitleInput(
				Vouchers.getInstance(),
				player,
				title,
				subTitle
		) {
			@Override
			public boolean onResult(String string) {
				if (validation.test(string)) {
					onSuccess.onSuccess(string);
					return true;
				}
				onFailure.onFailure();
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
			TitleInputSuccessCallback onSuccess,
			TitleInputFailureCallback onFailure,
			TitleInputExitCallback onExit,
			Predicate<String> validation
	) {
		get(
				player,
				title,
				subTitle,
				onSuccess,
				onFailure,
				onExit,
				validation,
				Function.identity()
		);
	}
}
