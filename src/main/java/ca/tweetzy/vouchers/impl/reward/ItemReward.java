package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Setter
@Getter
public final class ItemReward extends BaseReward {

	private ItemStack item;

	public ItemReward(@NonNull final ItemStack item, final double chance, final int delay, @NonNull final List<Message> messages) {
		super(RewardType.COMMAND, chance, delay, messages);
		this.item = item;
	}

	@Override
	public void execute(@NonNull Player player, String[] args) {

		if (getDelay() >= 1) {
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> PlayerUtil.giveItem(player, this.item), getDelay());
		} else {
			PlayerUtil.giveItem(player, this.item);
		}

		getMessages().stream().map(msg -> (BaseMessage) msg).forEach(msg -> msg.send(player, args));
	}
}
