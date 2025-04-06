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

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public abstract class BaseVoucher implements Voucher {

	@Setter
	private VoucherType voucherType;

	@Setter
	private String[] args;

	public abstract ItemStack generatePhysicalVoucher(Player player);

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.newChain().async(() -> {
			File voucherFile = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files/%s.json".formatted(getId().toLowerCase()));
			boolean success = voucherFile.delete();

			if (success)
				Vouchers.getVoucherManager().remove(getId());

			if (syncResult != null)
				syncResult.accept(success ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		}).execute();
	}


}
