package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.util.ItemUtil;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: March 20 2022
 * Time Created: 10:46 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuNewReward extends View {

	private final Voucher voucher;
	private final VoucherReward voucherReward;

	public MenuNewReward(@NonNull final Voucher voucher, @NonNull final VoucherReward voucherReward) {
		super("&b" + voucher.getId() + " &8> &7Add Reward");
		setRows(6);
		setAcceptsItems(true);

		this.voucher = voucher;
		this.voucherReward = voucherReward;

		draw();
	}

	private void draw() {
		reset();

		setUnlocked(9 * 3 + 4, this.voucherReward.getRewardType() == RewardType.ITEM);

		// type btn
		setButton(9 + 4, ItemCreator
				.of(this.voucherReward.getRewardType() == RewardType.COMMAND ? CompMaterial.PAPER : CompMaterial.DIAMOND_SWORD)
				.name("&e&lReward Type")
				.lore("", "&7Current&f: &e" + ItemUtil.bountifyCapitalized(this.voucherReward.getRewardType()), "", "&dClick &7to switch").make(), click -> {

			if (this.voucherReward.getRewardType() == RewardType.COMMAND) {
				this.voucherReward.setRewardType(RewardType.ITEM);
				setItem(9 * 3 + 4, CompMaterial.AIR.toItem());
			} else {
				this.voucherReward.setRewardType(RewardType.COMMAND);
			}

			draw();
		});

		// decrease button
		setButton(9 * 3 + 1, ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE, "&c&lDecrease Chance", "", "&7Current&f: &a" + this.voucherReward.getChance(), "", "&dClick &7to decrease by &c1%").make(), click -> {
			double newChance = this.voucherReward.getChance() - 1.0;
			if (newChance >= 1.0) {
				this.voucherReward.setChance(newChance);
				assignItem();
				draw();
			}
		});

		// increase button
		setButton(9 * 3 + 7, ItemCreator.of(CompMaterial.LIME_STAINED_GLASS_PANE, "&a&lIncrease Chance", "", "&7Current&f: &a" + this.voucherReward.getChance(), "", "&dClick &7to increase by &a1%").make(), click -> {
			double newChance = this.voucherReward.getChance() + 1.0;
			if (newChance <= 100.0) {
				this.voucherReward.setChance(newChance);
				assignItem();
				draw();
			}
		});

		// command button
		if (this.voucherReward.getRewardType() == RewardType.COMMAND) {
			assignItem();
			setButton(9 * 3 + 4, ItemCreator.of(CompMaterial.PAPER, "&e&lCommand", "", "&7Current&F: &e" + this.voucherReward.getCommand(), "", "&dClick &7to change command").make(), click -> {
				new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_CMD_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_CMD_SUBTITLE.getString(), "&a{player} &bcan be used to get the player") {
					@Override
					public boolean onResult(String string) {
						if (string == null || string.length() < 3)
							return false;

						MenuNewReward.this.voucherReward.setCommand(string);
						return true;
					}
				};
			});
		} else {
			setItem(9 * 3 + 4, this.voucherReward.getItem() == null ? null : this.voucherReward.getItem());
		}

		// chance button
		setButton(9 * 6 - 1, ItemCreator.of(CompMaterial.CLOCK, "&e&lChance", "", "&7Current&F: &e" + this.voucherReward.getChance(), "", "&dClick &7to change chance").make(), click -> {
			assignItem();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_CHANCE_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_CHANCE_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String string) {
					if (!isDouble(string)) return false;
					final double chance = Double.parseDouble(string);

					if (chance < 1.0) {
						Common.tell(click.player, Locale.ERROR_CHANCE_TOO_LOW.getString());
						return false;
					}

					if (chance > 100.0) {
						Common.tell(click.player, Locale.ERROR_CHANCE_TOO_HIGH.getString());
						return false;
					}

					MenuNewReward.this.voucherReward.setChance(chance);
					click.manager.showGUI(click.player, new MenuNewReward(MenuNewReward.this.voucher, MenuNewReward.this.voucherReward));
					return true;
				}
			};
		});

		// confirm button
		setButton(9 * 4 + 4, ItemCreator.of(CompMaterial.LIME_DYE, "&a&lConfirm", "", "&dClick &7to add this reward").make(), click -> {
			if (this.voucherReward.getRewardType() == RewardType.ITEM) {
				assignItem();

				if (this.voucherReward.getItem() == null) {
					Common.tell(click.player, Locale.ERROR_MISSING_REWARD_ITEM.getString());
					return;
				}
			}

			this.voucher.getRewards().addWeak(this.voucherReward);
			// todo save voucher file
			click.manager.showGUI(click.player, new MenuVoucherRewards(this.voucher));
		});

		// back button
		setButton(9 * 6 - 9, ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back").make(), click -> {
		});
	}

	private boolean isDouble(@NonNull final String value) {
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private void assignItem() {
		if (this.voucherReward.getRewardType() == RewardType.ITEM) {
			final ItemStack rewardItem = getItem(9 * 3 + 4);
			if (rewardItem == null) return;
			this.voucherReward.setItem(rewardItem);
		}
	}
}
