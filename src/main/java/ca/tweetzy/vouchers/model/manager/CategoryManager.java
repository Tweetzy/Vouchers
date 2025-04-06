/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.vouchers.api.manager.ListManager;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.impl.VoucherCategory;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.NonNull;

import java.util.Map;

public final class CategoryManager extends ListManager<Category> {

	public CategoryManager() {
		super("Category");
	}

	public Category get(@NonNull String id) {
		return this.managerContent.stream().filter(category -> category.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public Category getNextElement(Category current) {
		if (this.managerContent.isEmpty()) {
			return null;
		}

		int index = this.managerContent.indexOf(current);

		if (index == -1) {
			return null;
		}

		int nextIndex = (index + 1) % this.managerContent.size();
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
