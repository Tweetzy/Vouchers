package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.IVoucher;
import ca.tweetzy.vouchers.api.voucher.IVoucherSettings;
import ca.tweetzy.vouchers.menus.MenuConfirm;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:10 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
public class Voucher implements IVoucher {

	private final String id;
	private CompMaterial icon;
	private String displayName;
	private final StrictList<String> description;
	private final IVoucherSettings settings;
	private final StrictList<VoucherReward> rewards;

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
	public @NonNull StrictList<String> getDescription() {
		return this.description;
	}

	@Override
	public IVoucherSettings getSettings() {
		return this.settings;
	}

	@Override
	public @NotNull StrictList<VoucherReward> getRewards() {
		return this.rewards;
	}


//	public static Voucher deserialize(SerializedMap map) {
//		return new Voucher(
//				map.getString("id"),
//				map.getMaterial("icon"),
//				map.getString("display name"),
//				new StrictList<>(map.getStringList("description")),
//				map.get("settings", VoucherSettings.class),
//				new StrictList<>(map.getList("rewards", VoucherReward.class))
//		);
//	}

	public ItemStack build() {
		return ItemCreator
				.of(this.icon, this.displayName)
				.lore(this.description.getSource())
				.glow(this.settings.isGlowing())
				.hideTags(true)
				.unbreakable(true)
				.tag("Tweetzy:Vouchers", this.id)
				.make();
	}

	public void execute(@NonNull final Player player, @NonNull final ItemStack voucherItemstack) {
		if (this.settings.askConfirm()) {
			Vouchers.getGuiManager().showGUI(player, new MenuConfirm(this, voucherItemstack));
		} else {
			Vouchers.getVoucherManager().executeVoucher(player, this, voucherItemstack);
		}
	}
}
