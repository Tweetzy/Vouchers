package ca.tweetzy.vouchers.api.voucher.reward;

import ca.tweetzy.vouchers.api.sync.Navigable;

public enum RewardType implements Navigable<RewardType> {

	ITEM,
	COMMAND;

	@Override
	public Class<RewardType> enumClass() {
		return RewardType.class;
	}
}
