/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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

import ca.tweetzy.feather.comp.NBTEditor;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class VoucherManager extends Manager<String, Voucher> {

	@Override
	public List<Voucher> getAll() {
		return List.copyOf(this.contents.values());
	}

	@Override
	public Voucher find(@NonNull String key) {
		return this.contents.getOrDefault(key, null);
	}

	@Override
	public void add(@NonNull Voucher voucher) {
		this.contents.put(voucher.getId().toLowerCase(), voucher);
	}

	@Override
	public void remove(@NonNull String s) {
		this.contents.remove(s.toLowerCase());
	}

	public boolean isVoucher(@NonNull final ItemStack item) {
		return NBTEditor.contains(item, "Tweetzy:Vouchers");
	}

	@Override
	public void load() {
		this.contents.clear();

		Vouchers.getDataManager().getVouchers((error, all) -> {
			if (error == null)
				all.forEach(this::add);
		});
	}
}
