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

import ca.tweetzy.vouchers.api.voucher.redeem.Redeem;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
public final class VoucherRedeem implements Redeem {

	private final UUID id;
	private final UUID user;
	private final String voucherId;
	private final long time;

	@Override
	public @NotNull UUID getId() {
		return this.id;
	}

	@Override
	public UUID getUser() {
		return this.user;
	}

	@Override
	public String getVoucherId() {
		return this.voucherId.toLowerCase();
	}

	@Override
	public long getTime() {
		return this.time;
	}
}
