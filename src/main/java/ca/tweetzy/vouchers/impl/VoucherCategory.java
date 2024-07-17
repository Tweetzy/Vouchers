package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Category;
import com.google.gson.JsonArray;
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
		final JsonArray array = new JsonArray();

		getVoucherIds().forEach(array::add);

		return array.toString();
	}


	@Override
	public void store(@NonNull Consumer<Category> stored) {
		Vouchers.getDataManager().createCategory(this, (error, created) -> {
			if (error == null && created != null) {
				Vouchers.getCategoryManager().add(created);
				stored.accept(created);
			} else {
				stored.accept(null);
			}
		});
	}

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.getDataManager().deleteCategory(getId().toLowerCase(), (error, res) -> {
			if (error == null && res)
				Vouchers.getCategoryManager().remove(getId());

			if (syncResult != null)
				syncResult.accept(error == null && res ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		});
	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.getDataManager().updateCategory(this, (error, res) -> {
			if (syncResult != null)
				syncResult.accept(error == null && res ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		});
	}
}
