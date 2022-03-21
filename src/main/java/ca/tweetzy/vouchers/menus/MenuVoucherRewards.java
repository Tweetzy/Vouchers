package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.util.ItemUtil;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.model.LoreUtils;
import lombok.NonNull;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date Created: March 20 2022
 * Time Created: 11:24 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuVoucherRewards extends View {

	private final Voucher voucher;


	public MenuVoucherRewards(@NonNull final Voucher voucher) {
		super("&b" + voucher.getId() + " &8> &7Rewards");
		setRows(6);
		this.voucher = voucher;
		draw();

	}

	private void draw() {
		reset();

		final List<VoucherReward> itemsToFill = this.voucher.getRewards().getSource().stream().skip((page - 1) * (long) 45).limit(45).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(this.voucher.getRewards().size() / (double) 45));

		generateNavigation();
		setOnPage(e -> draw());

		int slot = 0;
		for (VoucherReward reward : itemsToFill) {
			final List<String> lore = new ArrayList<>();
			lore.add("");

			if (reward.getRewardType() == RewardType.ITEM)
				lore.addAll(getItemLore(reward.getItem()));
			else
				lore.addAll(LoreUtils.wrapLore("&7Current&f: &e" + reward.getCommand(), true));

			lore.add("&7Chance&f: &a" + reward.getChance() + "&f%");
			lore.add("");
			lore.add("&dPress Drop &7to delete this reward");

			setButton(slot++, ItemCreator
					.of(reward.getRewardType() == RewardType.ITEM ? reward.getItem() : CompMaterial.PAPER.toItem())
					.name(reward.getRewardType() == RewardType.ITEM ? getItemName(reward.getItem()) : "&eCommand Reward")
					.lore(lore)
					.make(), ClickType.DROP, click -> {

				this.voucher.getRewards().removeWeak(reward);
				// todo save voucher file
				click.manager.showGUI(click.player, new MenuVoucherRewards(this.voucher));
			});
		}

		// add reward button
		setButton(9 * 6 - 5, ItemCreator.of(CompMaterial.SLIME_BALL, "&E&lAdd Reward", "", "&dClick &7to add a new reward").make(), click -> click.manager.showGUI(click.player, new MenuNewReward(this.voucher, new VoucherReward())));

		// back button
		setButton(9 * 6 - 9, ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back").make(), click -> {

		});
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
