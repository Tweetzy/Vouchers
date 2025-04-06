/*
 * Vouchers
 * Copyright 2022-2025 Kiran Hart
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

import ca.tweetzy.vouchers.api.CooldownDataType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class CooldownManager {

	private final CooldownDataType COOLDOWN_TYPE = new CooldownDataType();
	private final NamespacedKey COOLDOWN_KEY;

	public CooldownManager(JavaPlugin plugin) {
		COOLDOWN_KEY = new NamespacedKey(plugin, "VouchersCooldowns");
	}

	public void addPlayerToCooldown(@NonNull final Player player, @NonNull final Voucher voucher) {
		HashMap<String, Long> voucherCooldowns = new HashMap<>();

		if (player.getPersistentDataContainer().has(COOLDOWN_KEY, COOLDOWN_TYPE)) {
			voucherCooldowns = player.getPersistentDataContainer().get(COOLDOWN_KEY, COOLDOWN_TYPE);
		}

		voucherCooldowns.put(voucher.getId(), System.currentTimeMillis() + (voucher.getSettings().getCooldown() * 1000L));
		player.getPersistentDataContainer().set(COOLDOWN_KEY, COOLDOWN_TYPE, voucherCooldowns);
	}


	public boolean isPlayerInCooldown(@NonNull final Player player) {
		return player.getPersistentDataContainer().has(COOLDOWN_KEY, COOLDOWN_TYPE);
	}

	public boolean isPlayerInCooldownForVoucher(@NonNull final Player player, @NonNull final Voucher voucher) {
		HashMap<String, Long> voucherCooldowns = player.getPersistentDataContainer().get(COOLDOWN_KEY, COOLDOWN_TYPE);
		if (voucherCooldowns == null) return false;

		return isPlayerInCooldown(player) && voucherCooldowns.containsKey(voucher.getId());
	}

	public long getCooldownTime(@NonNull final Player player, @NonNull final Voucher voucher) {
		if (!isPlayerInCooldownForVoucher(player, voucher)) return 0L;
		final HashMap<String, Long> voucherCooldowns = player.getPersistentDataContainer().get(COOLDOWN_KEY, COOLDOWN_TYPE);

		if (voucherCooldowns == null) return 0L;
		return voucherCooldowns.get(voucher.getId());
	}
}