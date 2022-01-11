package ca.tweetzy.vouchers.model;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class LoreUtils {

	private int defaultWrapLength = 40;

	public List<String> wrapLore(String lore, boolean wordWrap) {
		return wrapLore(lore, defaultWrapLength, wordWrap);
	}

	public List<String> wrapLore(String lore, int maxLength, boolean wordWrap) {
		List<String> wrapedLore = new ArrayList<>();
		String[] lines = lore.split("\\r?\\n"); //Ensures that the method respects line breaks

		for (String line : lines) {
			String unwrapedLine = line.replaceFirst("\\s++$", ""); //Removes line trailing spaces
			while (unwrapedLine.length() > maxLength) {
				int splitIndex;
				if (wordWrap) {
					splitIndex = getWrapIndex(unwrapedLine, maxLength);
				} else {
					splitIndex = maxLength;
				}
				String newLine = unwrapedLine.substring(0, splitIndex);
				newLine = newLine.replaceFirst("\\s++$", "");//Removes new line trailing spaces

				unwrapedLine = "&E" + unwrapedLine.substring(splitIndex);
				wrapedLore.add(newLine);
			}

			//We add the last line
			wrapedLore.add(unwrapedLine);
		}

		return wrapedLore;
	}

	private int getWrapIndex(String line, int maxLength) {
		int splitIndex = maxLength;
		if (maxLength > line.length()) {
			splitIndex = line.length();
		}

		int spaceIndex = splitIndex;
		//Search for an empty space " " in the line before the maxLength index
		while (spaceIndex >= 0) {
			if (line.charAt(spaceIndex) == ' ') {
				return spaceIndex + 1;
			} else {
				spaceIndex--;
			}
		}

		//If you are here, there is not empty spaces in the line
		return splitIndex;
	}

	public void setDefauldWrapLength(int WrapLength) {
		defaultWrapLength = WrapLength;
	}

}