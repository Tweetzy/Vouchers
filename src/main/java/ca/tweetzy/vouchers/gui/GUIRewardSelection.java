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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.gui.template.PagedGUI;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.settings.Locale;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public final class GUIRewardSelection extends PagedGUI<Reward> {

	private List<String> args;
	private final Consumer<Reward> selected;

	public GUIRewardSelection(@NonNull final Voucher voucher, List<String> args, @NonNull final Consumer<Reward> selected) {
		super(null, Locale.GUI_REWARD_SELECT_TITLE.getString(), 6, voucher.getRewards());
		this.args = args;
		this.selected = selected;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(Reward reward) {
		ItemStack displayItem = reward instanceof ItemReward ? ((ItemReward) reward).getItem() : CompMaterial.PAPER.parseItem();

		final QuickItem quickItem = QuickItem.of(displayItem);

		if (reward instanceof CommandReward)
			quickItem.name(Locale.GUI_REWARD_SELECT_CMD_NAME.getString());


		quickItem.lore("");

		if (reward instanceof final CommandReward commandReward) {
			quickItem.lore(Replacer.replaceVariables(Locale.GUI_REWARD_SELECT_CMD_LORE.getStringList(), "reward_command", commandReward.getCommand(), "reward_chance", Settings.REWARD_PICK_IS_GUARANTEED.getBoolean() ? 100D : commandReward.getChance()));
		} else {
			quickItem.lore(Replacer.replaceVariables(Locale.GUI_REWARD_SELECT_ITEM_LORE.getStringList(), "reward_chance", Settings.REWARD_PICK_IS_GUARANTEED.getBoolean() ? 100D : reward.getChance()));
		}

		return quickItem.make();
	}

	@Override
	protected void onClick(Reward reward, GuiClickEvent click) {
		reward.execute(click.player, Settings.REWARD_PICK_IS_GUARANTEED.getBoolean(), this.args);
		this.selected.accept(reward);
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
