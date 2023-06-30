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
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

		final QuickItem quickItem = QuickItem.of(displayItem == null ? Objects.requireNonNull(CompMaterial.PAPER.parseItem()) : displayItem);

		if (reward instanceof CommandReward)
			quickItem.name("&B&lCommand Reward");


		quickItem.lore("");

		if (reward instanceof final CommandReward commandReward) {
			quickItem.lore("&7Command&f: &b" + commandReward.getCommand());
			quickItem.lore("&7Message&f: &b" + (commandReward.getClaimMessage().isEmpty() ? "No Message Set" : commandReward.getClaimMessage()));
		}

		quickItem.lore(
				"&7Chance&f: &b" + reward.getChance(),
				"&7Delay&f: &b" + reward.getDelay(),
				""

		);

		if (reward instanceof CommandReward)
			quickItem.lore("&b&lRight Click &8» &7To edit message");

		quickItem.lore("&c&lPress 1 &8» &7To delete this reward");

		return quickItem.make();
	}

	@Override
	protected void drawAdditional() {
		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
				.name("&a&lNew Reward")
				.lore("&b&lLeft Click &8» &7To add a reward")
				.lore("&a&lRight Click &8» &7To quick-add inventory")
				.make(), click -> {

			if (click.clickType == ClickType.LEFT)
				click.manager.showGUI(click.player, new GUIRewardType(this.voucher));

			if (click.clickType == ClickType.RIGHT) {
				final List<ItemStack> toAdd = Arrays.stream(click.player.getInventory().getStorageContents()).filter(item -> item != null && item.getType() != CompMaterial.AIR.parseMaterial() && item.getAmount() != 0 && !Vouchers.getVoucherManager().isVoucher(item)).toList();

				new TitleInput(Vouchers.getInstance(), click.player, "&b&lReward Chance", "&fEnter new reward chance") {

					@Override
					public void onExit(Player player) {
						click.manager.showGUI(click.player, GUIRewardList.this);
					}

					@Override
					public boolean onResult(String string) {
						string = ChatColor.stripColor(string.toLowerCase());

						if (!NumberUtils.isNumber(string)) {
							Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, string));
							return false;
						}

						final double rate = Double.parseDouble(string);
						double finalRate = rate <= 0D ? 1D : Math.min(rate, 100D);

						click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
						toAdd.forEach(item -> GUIRewardList.this.voucher.addReward(new ItemReward(
								item,
								finalRate
						)));

						click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
						return true;
					}
				};
			}
		});
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

		if (click.clickType == ClickType.RIGHT && reward instanceof final CommandReward commandReward) {
			click.gui.exit();
			new TitleInput(Vouchers.getInstance(), click.player, "&b&lReward Message", "&fEnter new reward message") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUIRewardList.this);
				}

				@Override
				public boolean onResult(String string) {
					commandReward.setClaimMessage(string);
					GUIRewardList.this.voucher.sync(true);

					click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
					return true;
				}
			};
		}

		if (click.clickType == ClickType.NUMBER_KEY) {
			this.voucher.removeReward(reward);
			click.manager.showGUI(click.player, new GUIRewardList(GUIRewardList.this.voucher));
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(5);
	}
}
