package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.model.Replacer;
import ca.tweetzy.tweety.util.MathUtil;
import ca.tweetzy.tweety.util.PlayerUtil;
import ca.tweetzy.tweety.util.RandomUtil;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Locale;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date Created: March 20 2022
 * Time Created: 11:58 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuRewardSelect extends View {

	private final Voucher voucher;
	private final List<Integer> fillSlots = new ArrayList<>();


	public MenuRewardSelect(@Nonnull final Voucher voucher) {
		super(Settings.REWARD_SELECT_MENU_TITLE.getString());
		this.voucher = voucher;
		this.fillSlots.addAll(Settings.REWARD_SELECT_MENU_REWARD_SLOTS.getIntegerList());

		setRows(MathUtil.atLeast(1, Settings.REWARD_SELECT_MENU_ROWS.getInt()));
		setDefaultItem(ItemCreator.of(Settings.REWARD_SELECT_MENU_BACKGROUND.getMaterial()).name(" ").make());
		setDefaultSound(this.voucher.getSettings().getSound());

		draw();
	}

	private void draw() {
		reset();

		final List<VoucherReward> itemsToFill = this.voucher.getRewards().getSource().stream().skip((page - 1) * (long) this.fillSlots.size()).limit(this.fillSlots.size()).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(this.voucher.getRewards().size() / (double) this.fillSlots.size()));

		generateNavigation();
		setOnPage(e -> draw());

		for (int i = 0; i < this.rows * 9; i++) {
			if (this.fillSlots.contains(i) && this.fillSlots.indexOf(i) < itemsToFill.size()) {
				final VoucherReward reward = itemsToFill.get(this.fillSlots.indexOf(i));

				setButton(i, reward.getRewardType() == RewardType.ITEM ? reward.getItem() : ItemCreator
						.of(Settings.REWARD_SELECT_MENU_COMMAND_MATERIAL.getMaterial())
						.name(Settings.REWARD_SELECT_MENU_COMMAND_NAME.getString())
						.lore(Replacer.replaceArray(Settings.REWARD_SELECT_MENU_ALWAYS_GIVE.getBoolean() ? Settings.REWARD_SELECT_MENU_COMMAND_LORE.getStringList() : Settings.REWARD_SELECT_MENU_COMMAND_LORE_CHANCE.getStringList(),
								"reward_command", reward.getCommand(),
								"reward_chance", reward.getChance()
						))
						.make(), e -> {

					if (Settings.REWARD_SELECT_MENU_ALWAYS_GIVE.getBoolean()) {
						applyReward(e.player, reward);
					} else {
						if (!RandomUtil.chanceD(reward.getChance())) {
							Common.tell(e.player, Locale.ERROR_DID_NOT_GET_REWARD.getString());
						} else {
							applyReward(e.player, reward);
						}

					}

					e.player.closeInventory();
				});
			}
		}
	}

	private void applyReward(@NonNull final Player player, @NonNull final VoucherReward reward) {
		if (reward.getRewardType() == RewardType.ITEM && reward.getItem() != null)
			PlayerUtil.addItems(player.getInventory(), reward.getItem());
		else
			Common.dispatchCommand(player, reward.getCommand());
	}
}
