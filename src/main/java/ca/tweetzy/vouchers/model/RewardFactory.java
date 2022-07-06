/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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

package ca.tweetzy.vouchers.model;

import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class RewardFactory {

	public Reward decode(@NonNull final String json) {
		final JsonObject object = JsonParser.parseString(json).getAsJsonObject();

		final RewardType rewardType = RewardType.valueOf(object.get("type").getAsString().toUpperCase());

		return switch(rewardType) {
			case COMMAND -> new CommandReward(object.get("command").getAsString(), object.get("chance").getAsDouble(), object.get("delay").getAsInt());
			case ITEM -> new ItemReward(ItemEncoder.decodeItem(object.get("item").getAsString()), object.get("chance").getAsDouble());
		};
	}
}
