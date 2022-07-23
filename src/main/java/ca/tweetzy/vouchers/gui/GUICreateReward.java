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
import ca.tweetzy.feather.gui.template.BaseGUI;
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.feather.utils.input.TitleInput;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class GUICreateReward extends BaseGUI {

	private final Voucher voucher;
	private final RewardType rewardType;

	private CommandReward commandReward;
	private ItemReward itemReward;

	public GUICreateReward(@NonNull final Voucher voucher, @NonNull final RewardType rewardType) {
		this(voucher, rewardType, null, null);
	}

	public GUICreateReward(@NonNull final Voucher voucher, @NonNull final RewardType rewardType, CommandReward commandReward, ItemReward itemReward) {
		super(new GUIRewardType(voucher), "&bVouchers &8> &7" + voucher.getId() + " &8> &7New Reward", 6);
		this.voucher = voucher;
		this.rewardType = rewardType;

		if (this.rewardType == RewardType.ITEM) {
			setAcceptsItems(true);
			setUnlocked(1, 4);
			if (itemReward == null)
				this.itemReward = new ItemReward(null, 100D);
			else
				this.itemReward = itemReward;

			setItem(1, 4, this.itemReward.getItem());

		} else {
			if (commandReward == null)
				this.commandReward = new CommandReward("heal %player%", 100D, 20);
			else
				this.commandReward = commandReward;
		}

		draw();
	}

	@Override
	protected void draw() {

		if (this.rewardType == RewardType.ITEM) {

			setButton(3, 4, QuickItem
					.of(CompMaterial.REPEATER)
					.name("&b&lReward Chance")
					.lore(
							"&7The chance of the reward being given.",
							"",
							"&7Current&f: &b" + this.itemReward.getChance(),
							"",
							"&b&lClick &8» &7To edit reward chance"
					)
					.make(), click -> {

				this.itemReward = new ItemReward(getItem(1, 4), this.itemReward.getChance());

				new TitleInput(click.player, "&b&lReward Chance", "&fEnter new reward chance") {

					@Override
					public void onExit(Player player) {
						click.manager.showGUI(click.player, GUICreateReward.this);
					}

					@Override
					public boolean onResult(String string) {
						string = ChatColor.stripColor(string.toLowerCase());

						if (!NumberUtils.isNumber(string)) {
							Common.tell(click.player, Locale.NOT_A_NUMBER.getString());
							return false;
						}

						final double rate = Double.parseDouble(string);
						double finalRate = rate <= 0D ? 1D : Math.min(rate, 100D);

						click.manager.showGUI(click.player, new GUICreateReward(
								GUICreateReward.this.voucher,
								RewardType.ITEM,
								null,
								new ItemReward(GUICreateReward.this.itemReward.getItem(), finalRate)
						));

						return true;
					}
				};
			});

		}

		if (this.rewardType == RewardType.COMMAND) {

			setButton(1, 4, QuickItem
					.of(CompMaterial.PAPER)
					.name("&b&lReward Command")
					.lore(
							"&7The command that will be executed, use",
							"&7%player% to target the redeemer",
							"",
							"&7Current&f: &b" + this.commandReward.getCommand(),
							"",
							"&b&lClick &8» &7To edit reward command"
					)
					.make(), click -> new TitleInput(click.player, "&b&lReward Command", "&fEnter new reward command") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUICreateReward.this);
				}

				@Override
				public boolean onResult(String string) {
					click.manager.showGUI(click.player, new GUICreateReward(
							GUICreateReward.this.voucher,
							RewardType.COMMAND,
							new CommandReward(string, GUICreateReward.this.commandReward.getChance(), GUICreateReward.this.commandReward.getDelay()),
							null
					));

					return true;
				}
			});

			setButton(2, 4, QuickItem
					.of(CompMaterial.REPEATER)
					.name("&b&lReward Delay")
					.lore(
							"&7The delay before the command will be",
							"&7executed, 20 equals 1 second",
							"",
							"&7Current&f: &b" + this.commandReward.getDelay(),
							"",
							"&b&lClick &8» &7To edit reward delay"
					)
					.make(), click -> new TitleInput(click.player, "&b&lReward Delay", "&fEnter new reward delay") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUICreateReward.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());

					if (!NumberUtils.isNumber(string)) {
						Common.tell(click.player, Locale.NOT_A_NUMBER.getString());
						return false;
					}

					final int rate = Integer.parseInt(string);
					int finalRate = rate;
					if (rate <= 0)
						finalRate = -1;

					click.manager.showGUI(click.player, new GUICreateReward(
							GUICreateReward.this.voucher,
							RewardType.COMMAND,
							new CommandReward(GUICreateReward.this.commandReward.getCommand(), GUICreateReward.this.commandReward.getChance(), finalRate),
							null
					));

					return true;
				}
			});

			setButton(3, 4, QuickItem
					.of(CompMaterial.KNOWLEDGE_BOOK)
					.name("&b&lReward Chance")
					.lore(
							"&7The chance of the reward being given.",
							"",
							"&7Current&f: &b" + this.commandReward.getChance(),
							"",
							"&b&lClick &8» &7To edit reward chance"
					)
					.make(), click -> new TitleInput(click.player, "&b&lReward Chance", "&fEnter new reward chance") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUICreateReward.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());

					if (!NumberUtils.isNumber(string)) {
						Common.tell(click.player, Locale.NOT_A_NUMBER.getString());
						return false;
					}

					final double rate = Double.parseDouble(string);
					double finalRate = rate <= 0D ? 1D : Math.min(rate, 100D);

					click.manager.showGUI(click.player, new GUICreateReward(
							GUICreateReward.this.voucher,
							RewardType.COMMAND,
							new CommandReward(GUICreateReward.this.commandReward.getCommand(), finalRate, GUICreateReward.this.commandReward.getDelay()),
							null
					));

					return true;
				}
			});

		}

		setButton(5, 4, QuickItem
				.of(CompMaterial.SLIME_BALL)
				.name("&a&lCreate Reward")
				.lore("&b&lClick &8» &7To create reward")
				.make(), click -> {

			if (this.rewardType == RewardType.ITEM) {
				this.itemReward = new ItemReward(getItem(1, 4), this.itemReward.getChance());
				this.voucher.getRewards().add(this.itemReward);
			}

			if (this.rewardType == RewardType.COMMAND) {
				this.voucher.getRewards().add(this.commandReward);
			}

			this.voucher.sync(true);
			click.manager.showGUI(click.player, new GUIRewardList(this.voucher));

		});

		applyBackExit();
	}
}
