package ca.tweetzy.vouchers.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class VoucherHelper {

	public boolean runChance(double chance) {
		// Convert the chance to a decimal between 0 and 1
		double decimalChance = chance / 100;

		double randomValue = ThreadLocalRandom.current().nextDouble();
		return randomValue < decimalChance;
	}

	public Map<String, String> extractKeyValuePairs(String input) {
		Map<String, String> map = new HashMap<>();
		String[] pairs = input.split("\\s+(?=\\w+:)");

		for (String pair : pairs) {
			String[] keyValue = pair.split(":");
			if (keyValue.length == 2) {
				map.put(keyValue[0], keyValue[1]);
			}
		}

		return map;
	}

	public List<Integer> extractNumbers(String input) {
		List<Integer> numbers = new ArrayList<>();
		String[] parts = input.split(",");

		for (String part : parts) {
			if (part.contains("-")) {
				// Handle range
				String[] rangeParts = part.split("-");
				int start = Integer.parseInt(rangeParts[0].trim());
				int end = Integer.parseInt(rangeParts[1].trim());

				for (int i = start; i <= end; i++) {
					numbers.add(i);
				}
			} else {
				// Handle single number
				numbers.add(Integer.parseInt(part.trim()));
			}
		}

		return numbers;
	}

	public String dynamicVariablesReplace(String input, String[] values) {
		Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
		Matcher matcher = pattern.matcher(input);
		StringBuffer result = new StringBuffer();

		while (matcher.find()) {
			int index = Integer.parseInt(matcher.group(1));
			if (index < values.length) {
				matcher.appendReplacement(result, values[index]);
			} else {
				matcher.appendReplacement(result, matcher.group(0));
			}
		}
		matcher.appendTail(result);

		return result.toString();
	}

	public List<String> dynamicVariablesReplace(List<String> list, String[] values) {
		List<String> replaced = new ArrayList<>();

		for (String s : list) {
			replaced.add(dynamicVariablesReplace(s, values));
		}

		return replaced;
	}

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
