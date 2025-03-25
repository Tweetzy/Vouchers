package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.vouchers.api.manager.ListManager;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.impl.VoucherCategory;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CategoryManager extends ListManager<Category> {

	public CategoryManager() {
		super("Category");
	}

	public Category get(@NonNull String id) {
		return this.managerContent.stream().filter(category -> category.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public Category getNextElement(Category current) {
		// Check if the list is empty
		if (this.managerContent.isEmpty()) {
			return null;
		}

		// Find the index of the current object
		int index = this.managerContent.indexOf(current);

		// Handle the case where the current object is not in the list
		if (index == -1) {
			return null;
		}

		// Calculate the next index, wrapping around if necessary
		int nextIndex = (index + 1) % this.managerContent.size();

		// Return the object at the next index
		return this.managerContent.get(nextIndex);
	}

	@Override
	public void load() {
		// load existing voucher files
		clear();

		add(new VoucherCategory("allvouchers", "<GRADIENT:B3EBF2>&LAll Vouchers</GRADIENT:AEC6CF>", "CHEST"));

		Settings.CATEGORIES.getStringList().forEach(categoryLine -> {
			final Map<String, String> keyed = VoucherHelper.extractKeyValuePairs(categoryLine);

			final String id = keyed.get("id");
			final String name = keyed.get("name");
			final String item = keyed.get("item");

			add(new VoucherCategory(id.toLowerCase(), name, item));
		});
	}
}
