package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.constants.TweetyConstants;
import ca.tweetzy.tweety.settings.YamlSectionConfig;
import ca.tweetzy.vouchers.impl.Voucher;
import lombok.Getter;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 5:33 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class VoucherHolder extends YamlSectionConfig {

	@Getter
	private StrictList<Voucher> vouchers;

	public VoucherHolder() {
		super(null);
		this.vouchers = new StrictList<>();

		this.loadConfiguration(NO_DEFAULT, TweetyConstants.File.DATA);
	}

	@Override
	protected void onLoadFinish() {
		this.vouchers = new StrictList<>(getList("vouchers", Voucher.class));
	}

	@Override
	protected SerializedMap serialize() {
		return SerializedMap.ofArray("vouchers", this.vouchers);
	}
}
