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

package ca.tweetzy.vouchers.api.events;

import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public final class VoucherRedeemEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Voucher voucher;
	private final VoucherRedeemResult redeemResult;

	private boolean cancelled;

	public VoucherRedeemEvent(@NonNull final Player player, @NonNull final Voucher voucher, @NonNull final VoucherRedeemResult redeemResult) {
		this.player = player;
		this.voucher = voucher;
		this.redeemResult = redeemResult;

	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
