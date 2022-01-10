package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.ItemUtil;
import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.menu.model.MenuClickLocation;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.settings.Localization;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 10 2022
 * Time Created: 2:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuNewReward extends Menu {

	private final Voucher voucher;
	private final VoucherReward voucherReward;

	private final Button typeButton;
	private final Button decreaseButton;
	private final Button increaseButton;
	private final Button commandButton;
	private final Button confirmButton;

	private final Button backButton;
	private final Button chanceButton;

	public MenuNewReward(@NonNull final Voucher voucher, @NonNull final VoucherReward voucherReward) {
		this.voucher = voucher;
		this.voucherReward = voucherReward;
		setTitle("&b" + voucher.getId() + " &8> &7Add Reward");
		setSize(9 * 6);

		this.typeButton = Button.makeSimple(ItemCreator
				.of(this.voucherReward.getRewardType() == RewardType.COMMAND ? CompMaterial.PAPER : CompMaterial.DIAMOND_SWORD)
				.name("&e&lReward Type")
				.lore("", "&7Current&f: &e" + ItemUtil.bountifyCapitalized(this.voucherReward.getRewardType()), "", "&dClick &7to switch"), player -> {

			if (this.voucherReward.getRewardType() == RewardType.COMMAND) {
				this.voucherReward.setRewardType(RewardType.ITEM);
				this.getInventory().setItem(9 * 3 + 4, null);
			} else {
				this.voucherReward.setRewardType(RewardType.COMMAND);
			}

			newInstance().displayTo(player);
		});

		this.decreaseButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				double newChance = MenuNewReward.this.voucherReward.getChance() - 1.0;
				if (newChance >= 1.0) {
					MenuNewReward.this.voucherReward.setChance(newChance);
					assignItem();
					restartMenu();
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE, "&c&lDecrease Chance", "", "&7Current&f: &a" + MenuNewReward.this.voucherReward.getChance(), "", "&dClick &7to decrease by &c1%").make();
			}
		};

		this.increaseButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				double newChance = MenuNewReward.this.voucherReward.getChance() + 1.0;
				if (newChance <= 100.0) {
					MenuNewReward.this.voucherReward.setChance(newChance);
					assignItem();
					restartMenu();
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.LIME_STAINED_GLASS_PANE, "&a&lIncrease Chance", "", "&7Current&f: &a" + MenuNewReward.this.voucherReward.getChance(), "", "&dClick &7to increase by &a1%").make();
			}
		};

		this.commandButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&lCommand", "", "&7Current&F: &e" + this.voucherReward.getCommand(), "", "&dClick &7to change command"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_COMMAND_TITLE, Localization.VoucherEdit.ENTER_COMMAND_SUBTITLE, "&a{player} &bcan be used to get the player") {
			@Override
			public boolean onResult(String string) {
				if (string == null || string.length() < 3)
					return false;

				MenuNewReward.this.voucherReward.setCommand(string);
				newInstance().displayTo(player);
				return true;
			}
		});

		this.chanceButton = Button.makeSimple(ItemCreator.of(CompMaterial.CLOCK, "&e&lChance", "", "&7Current&F: &e" + this.voucherReward.getChance(), "", "&dClick &7to change chance"), player -> {
			assignItem();

			new TitleInput(player, Localization.VoucherEdit.ENTER_CHANCE_TITLE, Localization.VoucherEdit.ENTER_CHANCE_SUBTITLE) {
				@Override
				public boolean onResult(String string) {
					if (!isDouble(string)) return false;
					final double chance = Double.parseDouble(string);

					if (chance < 1.0) {
						tell(Localization.Error.CHANCE_TOO_LOW);
						return false;
					}

					if (chance > 100.0) {
						tell(Localization.Error.CHANCE_TOO_HIGH);
						return false;
					}

					MenuNewReward.this.voucherReward.setChance(chance);
					newInstance().displayTo(player);
					return true;
				}
			};
		});

		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherRewards(this.voucher).displayTo(player));

		this.confirmButton = Button.makeSimple(ItemCreator.of(CompMaterial.LIME_DYE, "&a&lConfirm", "", "&dClick &7to add this reward"), player -> {
			if (this.voucherReward.getRewardType() == RewardType.ITEM) {
				assignItem();

				if (this.voucherReward.getItem() == null) {
					tell(Localization.Error.MISSING_REWARD_ITEM);
					return;
				}
			}

			this.voucher.getRewards().add(this.voucherReward);
			Vouchers.getVoucherManager().getVoucherHolder().save();
			new MenuVoucherRewards(this.voucher).displayTo(player);
		});
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {

	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == this.getSize() - 9)
			return this.backButton.getItem();

		if (slot == 9 + 4)
			return this.typeButton.getItem();

		if (slot == 9 * 3 + 1)
			return this.decreaseButton.getItem();

		if (slot == 9 * 3 + 4)
			if (this.voucherReward.getRewardType() == RewardType.COMMAND)
				return this.commandButton.getItem();
			else
				return this.voucherReward.getItem() == null ? NO_ITEM : this.voucherReward.getItem();

		if (slot == 9 * 3 + 7)
			return this.increaseButton.getItem();

		if (slot == 9 * 4 + 4)
			return this.confirmButton.getItem();

		if (slot == getSize() - 1)
			return this.chanceButton.getItem();

		return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make();
	}

	@Override
	protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor) {
		return location == MenuClickLocation.PLAYER_INVENTORY || slot == 9 * 3 + 4 && this.voucherReward.getRewardType() == RewardType.ITEM;
	}

	@Override
	public Menu newInstance() {
		return new MenuNewReward(this.voucher, this.voucherReward);
	}

	@Override
	protected boolean allowShiftClick() {
		return true;
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
			final ItemStack rewardItem = this.getInventory().getItem(9 * 3 + 4);
			if (rewardItem == null) return;
			this.voucherReward.setItem(rewardItem);
		}
	}
}
