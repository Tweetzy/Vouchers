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
			File voucherFile = new File(String.format("%s/voucher-files/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()));
			boolean success = false;
			try {
				if (voucherFile.exists()) {
					success = voucherFile.delete();
					if (success) {
						Vouchers.getInstance().getServer().getScheduler().runTask(Vouchers.getInstance(), () -> {
							Vouchers.getVoucherManager().remove(getId());
						});
					} else {
						Vouchers.getInstance().getLogger().warning("Failed to delete voucher file: " + voucherFile.getName());
					}
				} else {
					Vouchers.getInstance().getLogger().warning("Voucher file does not exist: " + voucherFile.getName());
					// Still remove from manager if file doesn't exist
					Vouchers.getInstance().getServer().getScheduler().runTask(Vouchers.getInstance(), () -> {
						Vouchers.getVoucherManager().remove(getId());
					});
					success = true; // Consider it successful if already removed
				}
			} catch (Exception e) {
				Vouchers.getInstance().getLogger().severe("Error deleting voucher file " + voucherFile.getName() + ": " + e.getMessage());
				e.printStackTrace();
			}

			if (syncResult != null)
				syncResult.accept(success ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		}).execute();
	}


}
