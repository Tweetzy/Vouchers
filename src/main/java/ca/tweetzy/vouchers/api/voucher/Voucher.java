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

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.Synchronize;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface Voucher extends Synchronize {

	String getId();

	int getRewardCount();

	String getName();

	ItemStack getItem();

	List<String> getDescription();

	VoucherOptions getOptions();

	RewardMode getRewardMode();

	List<Reward> getRewards();

	void setName(String name);
	void setRewardCount(int rewardCount);

	void setItem(ItemStack item);

	void setRewardMode(RewardMode rewardMode);

	void setDescription(List<String> description);

	String getRewardJson();

	ItemStack buildItem(Player player);

	ItemStack buildItem(Player player, List<String> params);

	default List<String> getFilteredDescription() {
		List<String> desc = new ArrayList<>(getDescription());

		for (int i = 0; i < desc.size(); i++) {
			if (desc.get(i).contains("-Blank-"))
				desc.set(i, "");
		}

		return desc;
	}

	void addReward(Reward reward);

	void removeReward(Reward reward);
}
