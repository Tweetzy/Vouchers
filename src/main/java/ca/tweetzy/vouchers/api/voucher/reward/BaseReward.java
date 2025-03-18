package ca.tweetzy.vouchers.api.voucher.reward;

import ca.tweetzy.vouchers.api.voucher.message.Message;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
public abstract class BaseReward implements Reward {

	protected RewardType type;
	private double chance;
	private int delay;
	private List<Message> messages;

	@Override
	public double getChance() {
		return this.chance;
	}

	@Override
	public void setChance(double chance) {
		this.chance = chance;
	}

	@Override
	public int getDelay() {
		return this.delay;
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public List<Message> getMessages() {
		return this.messages;
	}

	public abstract void execute(@NonNull final Player player);
}
