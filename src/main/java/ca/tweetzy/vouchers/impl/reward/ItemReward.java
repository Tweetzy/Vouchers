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

package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.AbstractReward;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.model.Chance;
import ca.tweetzy.vouchers.model.Giver;
import ca.tweetzy.vouchers.model.ItemEncoder;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ItemReward extends AbstractReward {

	@Getter
	private final ItemStack item;

	public ItemReward(ItemStack item, double chance) {
		super(RewardType.ITEM, chance, 0);
		this.item = item;
	}


	@Override
	public boolean execute(Player player, boolean guarantee, List<String> args) {
		if (guarantee) {
			if (this.getDelay() != -1)
				Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> Giver.giveItem(player, this.item), this.getDelay());
			else
				Giver.giveItem(player, this.item);
			return true;
		}

		if (!Chance.tryChance(this.getChance())) return false;

		if (this.getDelay() != -1)
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> Giver.giveItem(player, this.item), this.getDelay());
		else
			Giver.giveItem(player, this.item);

		return true;
	}

	@Override
	public String toJsonString() {
		final JsonObject object = new JsonObject();

		object.addProperty("item", ItemEncoder.encodeItem(this.item));
		object.addProperty("chance", this.getChance());
		object.addProperty("type", RewardType.ITEM.name());

		return object.toString();
	}


}
