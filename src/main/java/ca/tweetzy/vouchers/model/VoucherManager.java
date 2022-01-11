package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.RandomUtil;
import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.tweety.remain.Remain;
import ca.tweetzy.vouchers.api.RewardMode;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.menu.MenuRewardSelect;
import ca.tweetzy.vouchers.settings.Localization;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

	@Getter
	private VoucherHolder voucherHolder;

	public Voucher findVoucher(@NonNull final String id) {
		return this.voucherHolder.getVouchers().getSource().stream().filter(voucher -> voucher.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public void addVoucher(@NonNull final Voucher voucher) {
		this.voucherHolder.getVouchers().addIfNotExist(voucher);
		this.voucherHolder.save();
	}

	public void deleteVoucher(@NonNull final String id) {
		final Voucher voucher = this.findVoucher(id);
		if (voucher == null) return;
		this.voucherHolder.getVouchers().removeWeak(voucher);
		this.voucherHolder.save();
	}

	public List<Voucher> getVouchers() {
		return Collections.unmodifiableList(this.voucherHolder.getVouchers().getSource());
	}

	public boolean isVoucher(@NonNull final ItemStack itemstack) {
		return CompMetadata.hasMetadata(itemstack, "Tweetzy:Vouchers") || CompMetadata.hasMetadata(itemstack, "tweetzy:voucher:id");
	}

	public String getVoucherId(@NonNull final ItemStack itemstack) {
		if (CompMetadata.hasMetadata(itemstack, "Tweetzy:Vouchers"))
			return CompMetadata.getMetadata(itemstack, "Tweetzy:Vouchers");
		return CompMetadata.getMetadata(itemstack, "tweetzy:voucher:id");
	}

	public void executeVoucher(@NonNull final Player player, @NonNull final Voucher voucher, @NonNull final ItemStack voucherItem) {
		if (voucher.getSettings().requiresUsePermission() && !player.hasPermission(voucher.getSettings().getPermission())) {
			Common.tell(player, Localization.Error.NO_VOUCHER_PERMISSION);
			return;
		}

		if (voucher.getSettings().sendTitle() && voucher.getSettings().sendSubtitle())
			Remain.sendTitle(player, voucher.getSettings().getTitle(), voucher.getSettings().getSubtitle());
		else if (voucher.getSettings().sendTitle() && !voucher.getSettings().sendSubtitle())
			Remain.sendTitle(player, voucher.getSettings().getTitle(), "");
		else
			Remain.sendTitle(player, "", voucher.getSettings().getSubtitle());

		if (voucher.getSettings().sendActionBar())
			Remain.sendActionBar(player, voucher.getSettings().getActionBar());

		Common.tell(player, voucher.getSettings().getRedeemMessage().replace("{voucher_name}", voucher.getDisplayName()).replace("{voucher_id}", voucher.getId()));

		if (voucher.getSettings().broadcastRedeem())
			Remain.getOnlinePlayers().forEach(onlinePlayer -> {
				Common.tell(onlinePlayer, voucher.getSettings().getBroadcastMessage().replace("{player}", player.getName()).replace("{voucher_name}", voucher.getDisplayName()).replace("{voucher_id}", voucher.getId()));
			});

		if (voucher.getSettings().removeOnUse())
			PlayerUtil.takeOnePiece(player, voucherItem);

		if (voucher.getSettings().getRewardMode() == RewardMode.RANDOM) {
			final ProbabilityCollection<VoucherReward> rewardProbabilityCollection = new ProbabilityCollection<>();
			if (voucher.getRewards().size() == 0) return;
			voucher.getRewards().forEach(reward -> rewardProbabilityCollection.add(reward, (int) reward.getChance()));

			final VoucherReward selectedReward = rewardProbabilityCollection.get();
			applyReward(player, selectedReward);

		} else if (voucher.getSettings().getRewardMode() == RewardMode.REWARD_SELECT) {
			new MenuRewardSelect(voucher).displayTo(player);
		} else {
			voucher.getRewards().forEach(reward -> {
				if (RandomUtil.chanceD(reward.getChance())) {
					applyReward(player, reward);
				}
			});
		}
	}

	private void applyReward(@NonNull final Player player, @NonNull final VoucherReward reward) {
		if (reward.getRewardType() == RewardType.ITEM && reward.getItem() != null)
			PlayerUtil.addItems(player.getInventory(), reward.getItem());
		else
			Common.dispatchCommand(player, reward.getCommand());
	}

	public void load() {
		this.voucherHolder = new VoucherHolder();
	}
}
