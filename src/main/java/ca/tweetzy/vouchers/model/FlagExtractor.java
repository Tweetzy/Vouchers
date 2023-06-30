package ca.tweetzy.vouchers.model;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public final class FlagExtractor {

	public String grabWordsUntilFlag(@NonNull String[] words, final int start, @NonNull final String flagDelimiter) {
		final StringBuilder builder = new StringBuilder();
		words = Arrays.copyOfRange(words, start, words.length);

		for (String word : words) {
			if (word.equalsIgnoreCase(flagDelimiter)) break;
			builder.append(word).append(" ");
		}

		return builder.toString().trim();
	}

	public List<String> grabFlagArguments(@NonNull String[] words, @NonNull final String flagDelimiter) {
		int flagIndex = 0;
		boolean hasFlagArg = false;

		for (String word : words) {
			if (!word.equalsIgnoreCase(flagDelimiter)) flagIndex++;
			else {
				hasFlagArg = true;
				break;
			}
		}

		return hasFlagArg ? List.of(Arrays.copyOfRange(words, flagIndex + 1, words.length)) : Collections.emptyList();
	}
}
