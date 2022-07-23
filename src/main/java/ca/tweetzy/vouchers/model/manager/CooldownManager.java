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

import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager {

	private final Map<UUID, HashMap<String, Long>> cooldowns = new ConcurrentHashMap<>();

	public void addPlayerToCooldown(@NonNull final UUID player, @NonNull final Voucher voucher) {
		HashMap<String, Long> voucherCooldowns = new HashMap<>();

		if (this.cooldowns.containsKey(player)) {
			voucherCooldowns = this.cooldowns.get(player);
		}

		// add them
		voucherCooldowns.put(voucher.getId(), System.currentTimeMillis() + (voucher.getOptions().getCooldown() * 1000L));
		this.cooldowns.put(player, voucherCooldowns);
	}

	public boolean isPlayerInCooldown(@NonNull final UUID player) {
		return this.cooldowns.containsKey(player);
	}

	public boolean isPlayerInCooldownForVoucher(@NonNull final UUID player, @NonNull final Voucher voucher) {
		return isPlayerInCooldown(player) && this.cooldowns.get(player).containsKey(voucher.getId());
	}

	public long getCooldownTime(@NonNull final UUID player, @NonNull final Voucher voucher) {
		if (!isPlayerInCooldownForVoucher(player, voucher)) return 0L;
		return this.cooldowns.get(player).get(voucher.getId());
	}
}
