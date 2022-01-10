package ca.tweetzy.vouchers.model;

import lombok.experimental.UtilityClass;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 7:35 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class BoolWord {

	public String get(final boolean val) {
		return val ? "&aTrue" : "&cFalse";
	}
}
