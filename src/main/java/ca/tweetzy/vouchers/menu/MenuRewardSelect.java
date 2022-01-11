package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.MathUtil;
import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.RandomUtil;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.model.Replacer;
import ca.tweetzy.tweety.model.SimpleSound;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.settings.Localization;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 4:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuRewardSelect extends MenuPagged<VoucherReward> {

	private final Voucher voucher;

	public MenuRewardSelect(@Nonnull final Voucher voucher) {
		super(null, Settings.RewardSelectMenu.REWARD_SLOTS, voucher.getRewards());
		this.voucher = voucher;
		setTitle(Settings.RewardSelectMenu.TITLE);
		setSize(MathUtil.atLeast(9, Settings.RewardSelectMenu.ROWS * 9));
		setSound(new SimpleSound(this.voucher.getSettings().getSound().getSound(), 1.0F));
	}

	@Override
	protected ItemStack backgroundItem() {
		return ItemCreator.of(Settings.RewardSelectMenu.BACKGROUND_ITEM).name(" ").make();
	}

	@Override
	protected ItemStack convertToItemStack(VoucherReward reward) {
		if (reward.getRewardType() == RewardType.ITEM)
			return reward.getItem();

		return ItemCreator
				.of(Settings.RewardSelectMenu.CMD_REWARD_ITEM)
				.name(Settings.RewardSelectMenu.CMD_REWARD_NAME)
				.lore(Replacer.replaceArray(Settings.RewardSelectMenu.ALWAYS_GIVE ? Settings.RewardSelectMenu.CMD_REWARD_LORE : Settings.RewardSelectMenu.CMD_REWARD_LORE_CHANCE,
						"reward_command", reward.getCommand(),
						"reward_chance", reward.getChance()
				))
				.make();
	}

	@Override
	protected void onPageClick(Player player, VoucherReward reward, ClickType click) {
		if (Settings.RewardSelectMenu.ALWAYS_GIVE) {
			applyReward(player, reward);
		} else {
			if (!RandomUtil.chanceD(reward.getChance())) {
				tell(Localization.Error.NOT_LUCKY_ENOUGH);
			} else {
				applyReward(player, reward);
			}

		}
		player.closeInventory();
	}

	private void applyReward(@NonNull final Player player, @NonNull final VoucherReward reward) {
		if (reward.getRewardType() == RewardType.ITEM && reward.getItem() != null)
			PlayerUtil.addItems(player.getInventory(), reward.getItem());
		else
			Common.dispatchCommand(player, reward.getCommand());
	}


	@Override
	public Menu newInstance() {
		return new MenuRewardSelect(this.voucher);
	}
}
