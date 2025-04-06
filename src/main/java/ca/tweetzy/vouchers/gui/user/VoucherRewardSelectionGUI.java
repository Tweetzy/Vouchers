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

package ca.tweetzy.vouchers.gui.user;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class VoucherRewardSelectionGUI extends VouchersPagedGUI<Reward> {

	private final Voucher voucher;
	private final String[] arguments;
	private final List<Reward> selectedRewards;

	public VoucherRewardSelectionGUI(@NonNull Player player, @NonNull final Voucher voucher, String[] arguments) {
		super(null, player, TranslationManager.string(Translations.GUI_REWARD_SELECTION_TITLE, "total_rewards", voucher.getSettings().getMaximumRewards()), Settings.GUI_REWARD_SELECTION_ROWS.getInt(), new ArrayList<>());
		setDefaultItem(QuickItem.bg(Settings.GUI_REWARD_SELECTION_BG.getString()));

		this.voucher = voucher;
		this.arguments = arguments;
		this.selectedRewards = new ArrayList<>();
		setAllowClose(false);

		draw();
	}

	@Override
	protected void prePopulate() {
		this.items = this.voucher.getRewards();
	}

	@Override
	protected void drawFixed() {
		// decoration
		Settings.GUI_REWARD_SELECTION_DECORATION.getStringList().forEach(line -> {
			final Map<String, String> parsedLine = VoucherHelper.extractKeyValuePairs(line);

			final ItemStack item = QuickItem.bg(parsedLine.get("item"));
			// slot range
			List<Integer> fillSlots = VoucherHelper.extractNumbers(parsedLine.get("slot"));

			fillSlots.forEach(slot -> setItem(slot, item));
		});
	}

	@Override
	protected ItemStack makeDisplayItem(Reward reward) {
		final BaseReward baseReward =  (BaseReward) reward;

		QuickItem item = QuickItem.of(CompMaterial.PAPER);
		if (baseReward instanceof ItemReward itemReward) {
			item = QuickItem.of(itemReward.getItem());
		}

		if (baseReward instanceof final CommandReward commandReward) {
			item.name(commandReward.getName());
			item.lore(commandReward.getDescription());
		}

		// chance
		final boolean selected = this.selectedRewards.contains(reward);
		item.lore(TranslationManager.list(selected ? Translations.GUI_REWARD_SELECTION_LORE_SELECTED : Translations.GUI_REWARD_SELECTION_LORE_UN_SELECTED, "reward_chance", reward.getChance()));

		if (selected) {
			item.glow(true);
		}

		return item.make();
	}

	@Override
	protected void onClick(Reward reward, GuiClickEvent clickEvent) {
		if (this.selectedRewards.contains(reward)) {
			this.selectedRewards.remove(reward);
			draw();
			return;
		}

		if (this.selectedRewards.size() != this.voucher.getSettings().getMaximumRewards()) {
			this.selectedRewards.add(reward);
		}

		if (this.selectedRewards.size() == this.voucher.getSettings().getMaximumRewards()) {
			this.selectedRewards.forEach(selectedReward -> {
				if (VoucherHelper.runChance(selectedReward.getChance())) {
					final BaseReward baseReward = (BaseReward) selectedReward;
					baseReward.execute(clickEvent.player, this.arguments);
				}
			});

			this.selectedRewards.clear();
			clickEvent.gui.exit();
			return;
		}

		draw();
	}

	@Override
	protected List<Integer> fillSlots() {
		return Settings.GUI_REWARD_SELECTION_FILL_SLOTS.getIntList();
	}

	@Override
	protected boolean autoApplyBackExit() {
		return false;
	}
}
