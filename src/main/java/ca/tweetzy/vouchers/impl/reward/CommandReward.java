package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
@Getter
public final class CommandReward extends BaseReward {

	private String command;

	public CommandReward(@NonNull final String command, final double chance, final int delay, @NonNull final List<Message> messages) {
		super(RewardType.COMMAND, chance, delay, messages);
		this.command = command;
	}

	@Override
	public void execute(@NonNull Player player) {

	}
}
