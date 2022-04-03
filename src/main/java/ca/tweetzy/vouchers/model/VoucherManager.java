package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.HookManager;
import ca.tweetzy.tweety.remain.CompMetadata;
import ca.tweetzy.tweety.remain.Remain;
import ca.tweetzy.tweety.util.PlayerUtil;
import ca.tweetzy.tweety.util.RandomUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.RewardMode;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.menus.MenuRewardSelect;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:30 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

	private final List<Voucher> vouchers = new ArrayList<>();
	private final HashMap<UUID, HashMap<String, Long>> cooldowns = new HashMap<>();

	public void addPlayerToCoolDown(UUID player, Voucher voucher) {
		HashMap<String, Long> voucherCooldowns = new HashMap<>();
		if (this.cooldowns.containsKey(player)) {
			voucherCooldowns = this.cooldowns.get(player);
		}

		voucherCooldowns.put(voucher.getId(), System.currentTimeMillis() + (voucher.getSettings().getCooldown() * 1000L));
		this.cooldowns.put(player, voucherCooldowns);
	}

	public boolean isPlayerInCoolDown(UUID player) {
		return this.cooldowns.containsKey(player);
	}

	public boolean isPlayerInCoolDownForVoucher(UUID player, Voucher voucher) {
		return this.cooldowns.containsKey(player) && this.cooldowns.get(player).containsKey(voucher.getId());
	}

	public long getCoolDownTime(UUID player, Voucher voucher) {
		return this.cooldowns.get(player).get(voucher.getId());
	}

	public Voucher findVoucher(@NonNull final String id) {
		return this.vouchers.stream().filter(voucher -> voucher.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public void addVoucher(@NonNull final Voucher voucher) {
		if (this.vouchers.contains(voucher)) return;
		this.vouchers.add(voucher);
	}

	public void deleteVoucher(@NonNull final String id) {
		final Voucher voucher = findVoucher(id);
		this.vouchers.remove(voucher);
	}

	public List<Voucher> getVouchers() {
		return this.vouchers;
	}

	public boolean isVoucher(@NonNull final ItemStack itemstack) {
		return CompMetadata.hasMetadata(itemstack, "Tweetzy:Vouchers") || CompMetadata.hasMetadata(itemstack, "tweetzy:voucher:id");
	}

	public String getVoucherId(@NonNull final ItemStack itemstack) {
		if (CompMetadata.hasMetadata(itemstack, "Tweetzy:Vouchers"))
			return CompMetadata.getMetadata(itemstack, "Tweetzy:Vouchers");
		return CompMetadata.getMetadata(itemstack, "tweetzy:voucher:id");
	}

	// yea I know i'm repeating replace() a lot here, i'll fix it
	public void executeVoucher(@NonNull final Player player, @NonNull final Voucher voucher, @NonNull final ItemStack voucherItem) {
		if (voucher.getSettings().requiresUsePermission() && !player.hasPermission(voucher.getSettings().getPermission())) {
			Common.tell(player, Locale.ERROR_NO_PERMISSION.getString());
			return;
		}

		if (isPlayerInCoolDown(player.getUniqueId())) {
			if (isPlayerInCoolDownForVoucher(player.getUniqueId(), voucher)) {
				long coolDownTime = getCoolDownTime(player.getUniqueId(), voucher);
				if (System.currentTimeMillis() < coolDownTime) {
					Common.tell(player, Locale.ERROR_COOLDOWN.getString().replace("{remaining_time}", String.format("%,.2f", (coolDownTime - System.currentTimeMillis()) / 1000F)));
					return;
				}
			}
		}

		addPlayerToCoolDown(player.getUniqueId(), voucher);
		voucher.getSettings().getSound().play(player);

		if (voucher.getSettings().sendTitle() && voucher.getSettings().sendSubtitle())
			Remain.sendTitle(player, replaceIds(player, voucher, voucher.getSettings().getTitle()), replaceIds(player, voucher, voucher.getSettings().getSubtitle()));
		else if (voucher.getSettings().sendTitle() && !voucher.getSettings().sendSubtitle())
			Remain.sendTitle(player, replaceIds(player, voucher, voucher.getSettings().getTitle()), "");
		else
			Remain.sendTitle(player, "", replaceIds(player, voucher, voucher.getSettings().getSubtitle()));

		if (voucher.getSettings().sendActionBar())
			Remain.sendActionBar(player, replaceIds(player, voucher, voucher.getSettings().getActionBar()));

		Common.tell(player, replaceIds(player, voucher, voucher.getSettings().getRedeemMessage()));

		if (voucher.getSettings().broadcastRedeem())
			Remain.getOnlinePlayers().forEach(onlinePlayer -> {
				Common.tell(onlinePlayer, replaceIds(player, voucher, voucher.getSettings().getBroadcastMessage()));
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
			Vouchers.getGuiManager().showGUI(player, new MenuRewardSelect(voucher));
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
			Common.dispatchCommand(player, HookManager.replacePlaceholders(player, reward.getCommand()));
	}

	private String replaceIds(Player player, Voucher voucher, String value) {
		return HookManager.replacePlaceholders(player, value.replace("{player}", player.getName()).replace("{voucher_id}", voucher.getId()).replace("{voucher_name}", voucher.getDisplayName()));
	}

}
