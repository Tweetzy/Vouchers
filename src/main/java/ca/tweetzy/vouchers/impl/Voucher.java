package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.IVoucher;
import ca.tweetzy.vouchers.api.voucher.IVoucherSettings;
import ca.tweetzy.vouchers.menu.MenuConfirm;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Voucher implements IVoucher, ConfigSerializable {

	private final String id;
	private CompMaterial icon;
	private String displayName;
	private final List<String> description;
	private final IVoucherSettings settings;
	private final List<VoucherReward> rewards;

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
		if (this.settings.askConfirm()) {
			new MenuConfirm(this, voucherItemstack).displayTo(player);
		} else {
			Vouchers.getVoucherManager().executeVoucher(player, this, voucherItemstack);
		}
	}
}
