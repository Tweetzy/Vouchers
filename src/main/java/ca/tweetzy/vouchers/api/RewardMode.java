package ca.tweetzy.vouchers.api;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:00 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public enum RewardMode {

	AUTOMATIC,
	REWARD_SELECT,
	RANDOM;

	private static final RewardMode[] states = values();

	public RewardMode previous() {
		return states[(ordinal() - 1 + states.length) % states.length];
	}

	public RewardMode next() {
		return states[(this.ordinal() + 1) % states.length];
	}
}
