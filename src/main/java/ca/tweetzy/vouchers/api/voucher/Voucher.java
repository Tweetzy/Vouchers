package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.DoubleProbabilityCollection;
import ca.tweetzy.vouchers.api.sync.*;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.gui.user.VoucherRewardSelectionGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

public interface Voucher extends Displayable, Identifiable<String>, Storeable<Voucher>, Synchronize, Trackable, Jsonable {

	String getItem();

	void setItem(@NonNull final String item);

	VoucherSettings getSettings();

	List<Message> getMessages();

	List<Reward> getRewards();

	default String getCategoryId() {
		return "allvouchers";
	}

	default boolean execute(Player player, String[] arguments) {
		// play sound
		getSettings().getSound().play(player);

		// send messages
		getMessages().stream().map(msg -> (BaseMessage) msg).forEach(baseMessage -> baseMessage.send(player, arguments));

		// rewards
		if (getSettings().getRewardMode() == RewardMode.AUTOMATIC) {
			getRewards().stream().map(reward -> (BaseReward) reward).forEach(baseReward -> baseReward.execute(player, arguments));
		}

		if (getSettings().getRewardMode() == RewardMode.RANDOM) {
			int rewardsGiven = 0;

			final DoubleProbabilityCollection<Reward> rewardProbabilityCollection = new DoubleProbabilityCollection<>();
			for (Reward reward : getRewards()) {
				rewardProbabilityCollection.add(reward, reward.getChance());
			}

			while (rewardsGiven < getSettings().getMaximumRewards()) {
				if (rewardProbabilityCollection.isEmpty()) {
					break;
				}

				try {

					final BaseReward selected = (BaseReward) rewardProbabilityCollection.get();
					selected.execute(player, arguments);
					rewardsGiven++;

				} catch (Exception e) {
					Common.log("Error selecting reward: " + e.getMessage());
					break;
				}
			}
		}

		if (getSettings().getRewardMode() == RewardMode.SELECTION) {
			// open selection menu
			Vouchers.getGuiManager().showGUI(player, new VoucherRewardSelectionGUI(player, this, arguments));
		}


		return true;
	}
}
