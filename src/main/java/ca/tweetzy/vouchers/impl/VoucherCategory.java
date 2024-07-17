package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Category;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@AllArgsConstructor
public final class VoucherCategory implements Category {

	private final String id;
	private String name;
	private String description;
	private ItemStack icon;
	private HashSet<String> vouchers;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public ItemStack getItem() {
		return this.icon;
	}

	@Override
	public void setItem(ItemStack icon) {
		this.icon = icon;
	}

	@Override
	public Set<String> getVoucherIds() {
		return this.vouchers;
	}

	@Override
	public String getJSONString() {
		return "";
	}

	@Override
	public void store(@NonNull Consumer<Category> stored) {

	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
	}
}
