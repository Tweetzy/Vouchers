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

package ca.tweetzy.vouchers.gui;

import ca.tweetzy.feather.comp.enums.CompMaterial;
import ca.tweetzy.feather.gui.events.GuiClickEvent;
import ca.tweetzy.feather.gui.helper.InventoryBorder;
import ca.tweetzy.feather.gui.template.PagedGUI;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public final class GUIRewardList extends PagedGUI<Reward> {

	private final Voucher voucher;
	private int selectedIndex = -1;


	public GUIRewardList(@NonNull final Voucher voucher) {
		super(new GUIVoucherEdit(voucher), "&bVouchers &8> &7" + voucher.getId() + " &8> &7Rewards", 6, voucher.getRewards());
		this.voucher = voucher;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Reward reward) {
		ItemStack displayItem = reward instanceof ItemReward ? ((ItemReward) reward).getItem() : CompMaterial.PAPER.parseItem();

		final QuickItem quickItem = QuickItem.of(displayItem);

		if (reward instanceof CommandReward)
			quickItem.name("&B&lCommand Reward");


		quickItem.lore("");

		if (reward instanceof CommandReward)
			quickItem.lore("&7Command&f: &b" + ((CommandReward) reward).getCommand());

		quickItem.lore(
				"&7Chance&f: &b" + reward.getChance(),
				"&7Delay&f: &b" + reward.getDelay(),
				"",
				"&c&lPress 1 &8» &7To delete this reward"
		);

		return quickItem.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
				.name("&a&lNew Reward")
				.lore("&b&lClick &8» &7To add a reward")
				.make(), click -> click.manager.showGUI(click.player, new GUIRewardType(this.voucher)));
	}

	@Override
	protected void onClick(Reward reward, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT) {
			final int clickedIndex = this.voucher.getRewards().indexOf(reward);

			if (this.selectedIndex == -1)
				this.selectedIndex = clickedIndex;
			else {
				if (this.selectedIndex == clickedIndex) return;
				Collections.swap(this.voucher.getRewards(), this.selectedIndex, clickedIndex);
				this.voucher.sync(true);

				click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
			}
		}

		if (click.clickType == ClickType.NUMBER_KEY) {
			this.voucher.getRewards().remove(reward);
			this.voucher.sync(true);
			click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
