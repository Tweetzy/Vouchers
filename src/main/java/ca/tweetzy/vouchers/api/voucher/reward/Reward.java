package ca.tweetzy.vouchers.api.voucher.reward;

import ca.tweetzy.vouchers.api.voucher.message.Message;

import java.util.List;

public interface Reward {

	double getChance();

	void setChance(final double chance);

	int getDelay();

	void setDelay(final int delay);

	List<Message> getMessages();

}
