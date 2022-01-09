package ca.tweetzy.vouchers.impl;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.settings.YamlSectionConfig;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 08 2022
 * Time Created: 11:10 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VVoucher extends YamlSectionConfig implements Voucher {

	private final String id;
	private ItemStack icon;
	private String displayName;
	private final List<String> description;
	private final VoucherSettings settings;

	public VVoucher(@NonNull final String id, @NonNull final ItemStack icon, @NonNull final String displayName, @NonNull final List<String> description, @NonNull final VoucherSettings settings) {
		super("Vouchers." + id.toLowerCase());
		this.id = id;
		this.icon = icon;
		this.displayName = displayName;
		this.description = description;
		this.settings = settings;
	}

	@Override
	public @NonNull String getId() {
		return this.id;
	}

	@Override
	public @NonNull ItemStack getIcon() {
		return this.icon;
	}

	@Override
	public void setIcon(@NonNull ItemStack icon) {
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
	public VoucherSettings getSettings() {
		return this.settings;
	}

	@Override
	protected SerializedMap serialize() {
		return super.serialize();
	}
}
