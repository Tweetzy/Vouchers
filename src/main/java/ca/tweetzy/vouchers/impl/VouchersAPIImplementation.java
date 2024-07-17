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

package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.VouchersAPI;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.redeem.Redeem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public final class VouchersAPIImplementation implements VouchersAPI {

	@Override
	public List<Voucher> getAllVouchers() {
		return List.copyOf(Vouchers.getVoucherManager().getManagerContent().values());
	}

	@Override
	public Voucher findVoucher(@NonNull String key) {
		return Vouchers.getVoucherManager().find(key);
	}

	@Override
	public void addVoucher(@NonNull Voucher voucher) {
		Vouchers.getVoucherManager().add(voucher);
	}

	@Override
	public void removeVoucher(@NonNull String voucher) {
		Vouchers.getVoucherManager().remove(voucher);
	}

	@Override
	public boolean isVoucher(@NonNull ItemStack item) {
		return Vouchers.getVoucherManager().isVoucher(item);
	}

	@Override
	public List<Redeem> getAllRedeems() {
		return List.copyOf(Vouchers.getRedeemManager().getManagerContent().values());
	}

	@Override
	public Redeem findRedeem(@NonNull UUID uuid) {
		return Vouchers.getRedeemManager().find(uuid);
	}

	@Override
	public void addRedeem(@NonNull Redeem redeem) {
		Vouchers.getRedeemManager().add(redeem);
	}

	@Override
	public void removeRedeem(@NonNull UUID uuid) {
		Vouchers.getRedeemManager().remove(uuid);
	}

	@Override
	public int getTotalRedeems(@NonNull UUID playerUUID, @NonNull String voucherId) {
		return Vouchers.getRedeemManager().getTotalRedeems(playerUUID, voucherId);
	}

	@Override
	public int getTotalRedeems(@NonNull Player player, @NonNull Voucher voucher) {
		return Vouchers.getRedeemManager().getTotalRedeems(player, voucher);
	}

	@Override
	public boolean isAtRedeemLimit(@NonNull Player player, @NonNull Voucher voucher) {
		return Vouchers.getRedeemManager().isAtRedeemLimit(player, voucher);
	}

	@Override
	public void redeemVoucher(@NonNull Player player, @NonNull Voucher voucher, boolean ignoreRedeemLimit, boolean ignoreCooldown, List<String> args) {
		Vouchers.getRedeemManager().redeemVoucher(player, voucher, ignoreRedeemLimit, ignoreCooldown, args);
	}

	@Override
	public void redeemVoucher(@NonNull Player player, @NonNull Voucher voucher, boolean ignoreRedeemLimit, boolean ignoreCooldown) {
		Vouchers.getRedeemManager().redeemVoucher(player, voucher, ignoreRedeemLimit, ignoreCooldown);
	}

	@Override
	public void registerRedeemIfApplicable(@NonNull Player player, @NonNull Voucher voucher) {
		Vouchers.getRedeemManager().registerRedeemIfApplicable(player, voucher);
	}

	@Override
	public void addPlayerToCooldown(@NonNull UUID player, @NonNull Voucher voucher) {
		Vouchers.getCooldownManager().addPlayerToCooldown(player, voucher);
	}

	@Override
	public boolean isPlayerInCooldown(@NonNull UUID player) {
		return Vouchers.getCooldownManager().isPlayerInCooldown(player);
	}

	@Override
	public boolean isPlayerInCooldownForVoucher(@NonNull UUID player, @NonNull Voucher voucher) {
		return Vouchers.getCooldownManager().isPlayerInCooldownForVoucher(player, voucher);
	}

	@Override
	public long getCooldownTime(@NonNull UUID player, @NonNull Voucher voucher) {
		return Vouchers.getCooldownManager().getCooldownTime(player, voucher);
	}
}
