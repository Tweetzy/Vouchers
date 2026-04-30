/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.api.DoubleProbabilityCollection;
import ca.tweetzy.vouchers.api.sync.*;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
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
			// Sound + messages only; listener opens selection GUI and defers redeem / item / cooldown until picks complete.
			return false;
		}

		return true;
	}
}
