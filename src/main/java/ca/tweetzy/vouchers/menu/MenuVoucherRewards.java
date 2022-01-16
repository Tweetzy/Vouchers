package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.ChatUtil;
import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.ItemUtil;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.button.ButtonMenu;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.model.LoreUtils;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 10 2022
 * Time Created: 2:17 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuVoucherRewards extends MenuPagged<VoucherReward> {

	private final Voucher voucher;

	private final Button addRewardButton;
	private final Button backButton;

	public MenuVoucherRewards(@NonNull final Voucher voucher) {
		super(voucher.getRewards());
		setTitle("&b" + voucher.getId() + " &8> &7Rewards");
		this.voucher = voucher;

		this.addRewardButton = new ButtonMenu(new MenuNewReward(this.voucher, new VoucherReward()), ItemCreator.of(CompMaterial.SLIME_BALL, "&E&lAdd Reward", "", "&dClick &7to add a new reward"));
		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherEdit(this.voucher).displayTo(player));

	}

	@Override
	protected List<Button> getButtonsToAutoRegister() {
		return Arrays.asList(this.addRewardButton, this.backButton);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == this.getSize() - 9)
			return this.backButton.getItem();

		if (slot == getBottomCenterSlot())
			return this.addRewardButton.getItem();

		return super.getItemAt(slot);
	}

	@Override
	protected ItemStack convertToItemStack(VoucherReward reward) {
		final List<String> lore = new ArrayList<>();
		lore.add("");

		if (reward.getRewardType() == RewardType.ITEM)
			lore.addAll(getItemLore(reward.getItem()));
		else
			lore.addAll(LoreUtils.wrapLore("&7Current&f: &e" + reward.getCommand(), true));

		lore.add("&7Chance&f: &a" + reward.getChance() + "&f%");
		lore.add("");
		lore.add("&dPress Drop &7to delete this reward");

		return ItemCreator
				.of(reward.getRewardType() == RewardType.ITEM ? reward.getItem() : CompMaterial.PAPER.toItem())
				.name(reward.getRewardType() == RewardType.ITEM ? getItemName(reward.getItem()) : "&eCommand Reward")
				.lore(lore)
				.make();
	}

	@Override
	protected void onPageClick(Player player, VoucherReward reward, ClickType click) {
		if (click == ClickType.DROP) {
			this.voucher.getRewards().removeWeak(reward);
			Vouchers.getVoucherManager().getVoucherHolder().save();
			newInstance().displayTo(player);
		}
	}

	@Override
	public Menu newInstance() {
		return new MenuVoucherRewards(this.voucher);
	}

	private String getItemName(@NonNull final ItemStack item) {
		if (item.getItemMeta() != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
			return item.getItemMeta().getDisplayName();
		return "&e" + ItemUtil.bountifyCapitalized(item.getType());
	}

	private List<String> getItemLore(@NonNull final ItemStack item) {
		if (item.getItemMeta() != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			return item.getItemMeta().getLore();
		}
		return Collections.emptyList();
	}
}
