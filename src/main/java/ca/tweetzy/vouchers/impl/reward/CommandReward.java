package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.VoucherHelper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
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
	public void execute(@NonNull Player player, String[] args) {
		final String cmd = VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, this.command), args).replace("%player%", player.getName());

		if (getDelay() >= 1) {
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd), getDelay());
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}

		getMessages().stream().map(msg -> (BaseMessage) msg).forEach(msg -> msg.send(player, args));
	}
}
