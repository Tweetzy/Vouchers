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

import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.feather.utils.PlayerUtil;
import ca.tweetzy.vouchers.api.voucher.AbstractReward;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.model.Chance;
import ca.tweetzy.vouchers.model.ItemEncoder;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ItemReward extends AbstractReward {

	@Getter
	private final ItemStack item;

	public ItemReward(ItemStack item, double chance) {
		super(RewardType.ITEM, chance, 0);
		this.item = item;
	}

	@Override
	public void execute(Player player) {
		if (!Chance.tryChance(this.getChance())) return;

		if (this.getDelay() != -1)
			Common.runLater(this.getDelay(), () -> giveItem(player));
		else
			giveItem(player);
	}

	@Override
	public String toJsonString() {
		final JsonObject object = new JsonObject();

		object.addProperty("item", ItemEncoder.encodeItem(this.item));
		object.addProperty("chance", this.getChance());
		object.addProperty("type", RewardType.ITEM.name());

		return object.toString();
	}

	private void giveItem(@NonNull final Player player) {
		if (player.getInventory().firstEmpty() == -1)
			player.getWorld().dropItemNaturally(player.getLocation(), this.item);
		else
			player.getInventory().addItem(this.item);
	}
}
