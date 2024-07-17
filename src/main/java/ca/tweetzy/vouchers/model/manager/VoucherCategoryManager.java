package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.manager.KeyValueManager;
import ca.tweetzy.vouchers.api.voucher.Category;
import lombok.NonNull;

import java.util.List;

public final class VoucherCategoryManager extends KeyValueManager<String, Category> {

	public VoucherCategoryManager() {
		super("Voucher Category");
	}

	public void add(Category category) {
		if (this.managerContent.containsKey(category.getId().toLowerCase())) return;
		this.managerContent.put(category.getId().toLowerCase(), category);
	}

	public <T> T getNextElement(List<T> list, T currentElement) {
		int index = list.indexOf(currentElement);
		if (index == -1) {
			throw new IllegalArgumentException("Element not found in the list");
		}
		int nextIndex = (index + 1) % list.size();
		return list.get(nextIndex);
	}


	public Category find(@NonNull final String id) {
		return this.managerContent.getOrDefault(id.toLowerCase(), null);
	}

	@Override
	public void load() {
		clear();

		Vouchers.getDataManager().getCategories((error, all) -> {
			if (error == null)
				all.forEach(category -> {
					add(category.getId(), category);
				});
			else error.printStackTrace();
		});
	}
}
