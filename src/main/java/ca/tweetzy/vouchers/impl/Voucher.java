package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.RandomUtil;
import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.remain.Remain;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.api.voucher.IVoucher;
import ca.tweetzy.vouchers.api.voucher.IVoucherSettings;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:10 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Voucher implements IVoucher, ConfigSerializable {

	private final String id;
	private CompMaterial icon;
	private String displayName;
	private final List<String> description;
	private final IVoucherSettings settings;
	private final List<VoucherReward> rewards;

	public Voucher(@NonNull final String id, @NonNull final CompMaterial icon, @NonNull final String displayName, @NonNull final List<String> description, @NonNull final IVoucherSettings settings, @NonNull final List<VoucherReward> rewards) {
		this.id = id;
		this.icon = icon;
		this.displayName = displayName;
		this.description = description;
		this.settings = settings;
		this.rewards = rewards;
	}

	@Override
	public @NonNull String getId() {
		return this.id;
	}

	@Override
	public @NonNull CompMaterial getIcon() {
		return this.icon;
	}

	@Override
	public void setIcon(@NonNull CompMaterial icon) {
		this.icon = icon;
	}

	@Override
	public @NonNull String getDisplayName() {
		return this.displayName;
	}

	@Override
	public void setDisplayName(@NonNull String name) {
		this.displayName = name;
	}

	@Override
	public @NonNull List<String> getDescription() {
		return this.description;
	}

	@Override
	public IVoucherSettings getSettings() {
		return this.settings;
	}

	@Override
	public @NotNull List<VoucherReward> getRewards() {
		return this.rewards;
	}

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"id", this.id,
				"icon", this.icon,
				"display name", this.displayName,
				"description", this.description,
				"settings", this.settings,
				"rewards", this.rewards
		);
	}

	public static Voucher deserialize(SerializedMap map) {
		return new Voucher(
				map.getString("id"),
				map.getMaterial("icon"),
				map.getString("display name"),
				map.getStringList("description"),
				map.get("settings", VoucherSettings.class),
				map.getList("rewards", VoucherReward.class)
		);
	}

	public ItemStack build() {
		return ItemCreator
				.of(this.icon, this.displayName)
				.lore(this.description)
				.glow(this.settings.isGlowing())
				.hideTags(true)
				.unbreakable(true)
				.tag("Tweetzy:Vouchers", this.id)
				.make();
	}

	public void execute(@NonNull final Player player, @NonNull final ItemStack voucherItemstack) {
		if (this.settings.requiresUsePermission() && !player.hasPermission(this.settings.getPermission())) return;

		if (this.settings.sendTitle() && this.settings.sendSubtitle())
			Remain.sendTitle(player, this.settings.getTitle(), this.settings.getSubtitle());
		else if (this.settings.sendTitle() && !this.settings.sendSubtitle())
			Remain.sendTitle(player, this.settings.getTitle(), "");
		else
			Remain.sendTitle(player, "", this.settings.getSubtitle());

		if (this.settings.sendActionBar())
			Remain.sendActionBar(player, this.settings.getActionBar());

		Common.tell(player, this.settings.getRedeemMessage().replace("{voucher_name}", this.displayName).replace("{voucher_id}", this.id));

		if (this.settings.broadcastRedeem())
			Remain.getOnlinePlayers().forEach(onlinePlayer -> {
				Common.tell(onlinePlayer, this.settings.getBroadcastMessage().replace("{player}", player.getName()).replace("{voucher_name}", this.displayName).replace("{voucher_id}", this.id));
			});

		if (this.settings.removeOnUse())
			PlayerUtil.takeOnePiece(player, voucherItemstack);

		this.getRewards().forEach(reward -> {
			if (RandomUtil.chanceD(reward.getChance())) {
				if (reward.getRewardType() == RewardType.ITEM && reward.getItem() != null)
					PlayerUtil.addItems(player.getInventory(), reward.getItem());
				else
					Common.dispatchCommand(player, reward.getCommand());
			}
		});
	}
}
