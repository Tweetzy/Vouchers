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

package ca.tweetzy.vouchers.gui.admin.rewards;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.gui.admin.messages.VoucherMessageTypeGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.model.input.UserInput;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherRewardListGUI extends VouchersPagedGUI<Reward> {

	private Voucher voucher;

	public VoucherRewardListGUI(@NonNull Player player, @NonNull final Voucher voucher) {
		super(new VoucherOverviewGUI(player, voucher), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Edit Rewards", 6, new ArrayList<>());
		this.voucher = voucher;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void prePopulate() {
		// Refresh voucher from manager to get latest data
		final Voucher relocated = Vouchers.getVoucherManager().get(this.voucher.getId());
		if (relocated != null) {
			this.voucher = relocated; // Update voucher reference
			this.items = new ArrayList<>(relocated.getRewards());
		}
	}

	@Override
	protected void drawFixed() {
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		final List<String> rewardModeLore = switch (this.voucher.getSettings().getRewardMode()) {
			case AUTOMATIC -> List.of("&7This mode will attempt to give the player", "&7all the rewards in the voucher based on its chance.");
			case RANDOM -> List.of("&7This mode will give the player 1+ random", "&7reward(s) based on their specified chances.");
			case SELECTION -> List.of("&7This mode will allow the player to pick", "&71+ rewards, they will be given based on their chance.");
		};

		// reward type button
		setButton(getRows() - 1, 2, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:B3EBF2>&LReward Mode</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change the reward mode",
						"",
						"&e&lCurrent Mode&F: <GRADIENT:B3EBF2>&L%s</GRADIENT:AEC6CF>".formatted(ChatUtil.capitalizeFully(this.voucher.getSettings().getRewardMode()))
				)
				.lore(rewardModeLore)
				.lore(
						"",
						"&e&lClick",
						"&7To cycle reward modes"
				)
				.make(), click -> {

			this.voucher.getSettings().setRewardMode(this.voucher.getSettings().getRewardMode().next());
			this.voucher.sync(result -> draw());
		});

		// max rewards  button
		if (this.voucher.getSettings().getRewardMode() != RewardMode.AUTOMATIC)
			setButton(getRows() - 1, 6, QuickItem
					.of(CompMaterial.LEVER)
					.name("<GRADIENT:B3EBF2>&LTotal Rewards</GRADIENT:AEC6CF>")
					.lore(
							"&8The max # of rewards to be given",
							"&7When the reward mode isn't set to automatic vouchers",
							"&7will give keep rolling to give rewards until the player",
							"&7receives the # of rewards specified.",
							"",
							"&7Total Rewards&F: &e%s".formatted(this.voucher.getSettings().getMaximumRewards()),
							"",
							"&e&lClick",
							"&7To adjust the total rewards given"
					)
					.make(), click -> {

				UserInput.get(click.player, "<GRADIENT:B3EBF2>&LReward Options</GRADIENT:AEC6CF>", "&eEnter total # of rewards to be given", result -> {
					this.voucher.getSettings().setMaximumRewards(result);
					saveAndReOpen(click.player, true);
				}, (input) -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", input)), () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), MathUtil::isInt, Integer::parseInt);
			});

		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:77DD77>&lAdd Reward</GRADIENT:C1E1C1>")
				.lore(
						"&8Used to add a new reward",
						"&7You can add two different types of rewards",
						"&7either a &eItem &7or &eCommand &7reward.",
						"",
						"&e&lClick",
						"&7To create a new command reward",
						"",
						"&e&lDrag N' Drop",
						"&7An item onto this button to create an item reward"
				)
				.make(), click -> {

			final ItemStack cursor = click.cursor != null ? click.cursor.clone() : null;
			if (cursor != null && cursor.getType() != CompMaterial.AIR.get()) {
				this.voucher.getRewards().add(new ItemReward(cursor, 100, 0, new ArrayList<>()));
				saveAndReOpen(click.player, false);
				draw();
			} else {
				UserInput.get(click.player, "<GRADIENT:B3EBF2>&LVoucher Reward</GRADIENT:AEC6CF>", "&eEnter the reward command in chat without the /", result -> {
					final List<String> defaultDesc = new ArrayList<>();
					defaultDesc.add("&7Default command description");
					this.voucher.getRewards().add(new CommandReward(ChatColor.stripColor(result), 100, 0, "<GRADIENT:B3EBF2>&LVoucher Command Reward</GRADIENT:AEC6CF>", defaultDesc, new ArrayList<>()));
					saveAndReOpen(click.player);
				}, null, () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), validate -> !validate.isEmpty());

			}
		});

		// new reward
		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(Reward reward) {

		QuickItem item = QuickItem.of(CompMaterial.PAPER);
		List<String> lore = new ArrayList<>();

		if (reward instanceof final ItemReward itemReward) {
			item = QuickItem.of(itemReward.getItem());
		}

		if (reward instanceof final CommandReward commandReward) {
			item.name("<GRADIENT:B3EBF2>&LCommand Reward</GRADIENT:AEC6CF>");
			lore.addAll(List.of(
					"&8This is a command reward.",
					"",
					"&b&LCommand&F:",
					"&f/&e%s".formatted(commandReward.getCommand())
			));
		}

		lore.addAll(List.of(
				"",
				"&7Chance&F: &a%s".formatted(reward.getChance()),
				"&7Delay&F: &e%s".formatted(TimeConverter.convertSecondsToHumanReadable(reward.getDelay())),
				"",
				"&7Total Messages&f: &b%s".formatted(reward.getMessages().size()),
				"",
				"&e&lLeft Click",
				"&7To adjust the reward chance",
				"",
				"&e&lRight Click",
				"&7To adjust the reward delay",
				"",
				"&e&lShift+Left Click",
				"&7To &aadd&7/&eedit&7/&cremove &7reward messages",
				"",
				"&e&lDrop Key &7(Press Q)",
				"&7To &cremove &7this reward from the voucher"
		));

		return item.lore(lore).make();
	}

	@Override
	protected void onClick(Reward reward, GuiClickEvent click) {
		switch (click.clickType) {
			case LEFT -> {
				UserInput.get(click.player, "<GRADIENT:B3EBF2>&LReward Options</GRADIENT:AEC6CF>", "&eEnter reward chance in chat", result -> {
					reward.setChance(result < 0 ? 1 : result > 100 ? 100 : result);
					saveAndReOpen(click.player);
				}, (input) -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", input)), () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), MathUtil::isDouble, Double::parseDouble);
			}

			case RIGHT -> {
				UserInput.get(click.player, "<GRADIENT:B3EBF2>&LReward Options</GRADIENT:AEC6CF>", "&eEnter the reward delay", result -> {
					reward.setDelay(result);
					saveAndReOpen(click.player);
				}, (input) -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", input)), () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), MathUtil::isInt, Integer::parseInt);
			}

			case SHIFT_LEFT -> {
				click.manager.showGUI(click.player, new VoucherMessageTypeGUI(click.player, this.voucher, reward.getMessages(), true));
			}

			case DROP -> {
				this.voucher.getRewards().remove(reward);
				saveAndReOpen(click.player);
			}
		}
	}

	private void saveAndReOpen(@NonNull final Player player) {
		saveAndReOpen(player, true);
	}

	private void saveAndReOpen(@NonNull final Player player, boolean open) {
		this.voucher.sync(result -> {
			if (result == SynchronizeResult.FAILURE) {
				Common.tell(this.player, "&cSomething went wrong while saving the voucher.");
				return;
			}

			// Get fresh voucher from manager after successful save
			final Voucher freshVoucher = Vouchers.getVoucherManager().get(this.voucher.getId());
			if (freshVoucher != null) {
				this.voucher = freshVoucher; // Update local reference
			}

			if (open) {
				// Use fresh voucher or current voucher
				Vouchers.getGuiManager().showGUI(player, new VoucherRewardListGUI(player, freshVoucher != null ? freshVoucher : this.voucher));
			}
		});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
