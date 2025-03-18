package ca.tweetzy.vouchers.api.voucher.reward;

import ca.tweetzy.vouchers.api.sync.Navigable;

public enum RewardMode implements Navigable<RewardMode> {

	AUTOMATIC,
	RANDOM,
	SELECTION;

	@Override
	public Class<RewardMode> enumClass() {
		return RewardMode.class;
	}
}
