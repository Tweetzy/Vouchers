package ca.tweetzy.vouchers.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@UtilityClass
public final class Extractor {

	public String getTextureUrlFromBase(String base64String) {
		try {
			// Decode the Base64 string
			byte[] decodedBytes = Base64.getDecoder().decode(base64String);
			String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

			Gson gson = new GsonBuilder().create();
			JsonObject jsonObject = gson.fromJson(decodedString, JsonObject.class);
			JsonObject textures = jsonObject.getAsJsonObject("textures");
			JsonObject skin = textures.getAsJsonObject("SKIN");

			// Extract the URL
			return skin.get("url").getAsString();
		} catch (Exception e) {
			return null;
		}
	}

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
