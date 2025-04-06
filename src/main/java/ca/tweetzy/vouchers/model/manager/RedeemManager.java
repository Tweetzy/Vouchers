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

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.manager.KeyValueManager;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.redeem.Redeem;
import ca.tweetzy.vouchers.impl.VoucherRedeem;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RedeemManager extends KeyValueManager<UUID, Redeem> {

	public RedeemManager() {
		super("Redeem");
	}

	public void add(@NonNull Redeem redeem) {
		add(redeem.getId(), redeem);
	}

	public Redeem find(@NonNull final UUID uuid) {
		return this.managerContent.getOrDefault(uuid, null);
	}

	public int getTotalRedeems(@NonNull final UUID playerUUID, @NonNull final String voucherId) {
		return (int) this.managerContent.values().stream().filter(redeem -> redeem.getUser().equals(playerUUID) && redeem.getVoucherId().equalsIgnoreCase(voucherId)).count();
	}

	public int getTotalRedeems(@NonNull final Player player, @NonNull final Voucher voucher) {
		return getTotalRedeems(player.getUniqueId(), voucher.getId());
	}

	public boolean isAtRedeemLimit(@NonNull final Player player, @NonNull final Voucher voucher) {
		int maxVoucherUses = voucher.getSettings().getMaximumUses();
		if (maxVoucherUses <= 0) return false;

		return getTotalRedeems(player, voucher) >= maxVoucherUses;
	}

	public void registerRedeemIfApplicable(@NonNull final Player player, @NonNull final Voucher voucher) {
		Vouchers.getDataManager().createVoucherRedeem(new VoucherRedeem(UUID.randomUUID(), player.getUniqueId(), voucher.getId().toLowerCase(), System.currentTimeMillis()), (error, createdRedeem) -> {
			if (error == null)
				this.add(createdRedeem);
			else
				error.printStackTrace();
		});
	}

	public void deleteRedeems(@NonNull final Player player, @NonNull final String voucherId) {
		Vouchers.getDataManager().deleteRedeems(player.getUniqueId(), voucherId, (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(player, voucherId).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems(@NonNull final Player player) {
		Vouchers.getDataManager().deleteAllRedeems(player.getUniqueId(), (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(player).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems(@NonNull final String voucherId) {
		Vouchers.getDataManager().deleteAllRedeems(voucherId, (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(voucherId).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems() {
		Vouchers.getDataManager().deleteAllRedeems((error, deleted) -> {
			if (error == null && deleted) {
				clear();
			}
		});
	}

	private List<UUID> getRedeemIds(@NonNull final Player player, @NonNull final String voucherId) {
		return this.managerContent.values().stream().filter(redeem -> redeem.getUser().equals(player.getUniqueId()) && redeem.getVoucherId().equalsIgnoreCase(voucherId)).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}

	private List<UUID> getRedeemIds(@NonNull final Player player) {
		return this.managerContent.values().stream().filter(redeem -> redeem.getUser().equals(player.getUniqueId())).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}

	private List<UUID> getRedeemIds(@NonNull final String voucherId) {
		return this.managerContent.values().stream().filter(redeem -> redeem.getVoucherId().equalsIgnoreCase(voucherId)).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}


	@Override
	public void load() {
		clear();

		Vouchers.getDataManager().getVoucherRedeems((error, all) -> {
			if (error == null)
				all.forEach(this::add);
		});
	}
}
