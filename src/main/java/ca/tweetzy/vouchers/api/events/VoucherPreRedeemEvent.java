/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package ca.tweetzy.vouchers.api.events;

import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired after validation (permission, limits, cooldown) and before rewards are granted or the selection GUI opens.
 * Cancel to block the redeem.
 */
@Getter
public final class VoucherPreRedeemEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	private final Player player;
	private final Voucher voucher;
	private final String[] arguments;

	public VoucherPreRedeemEvent(@NotNull Player player, @NotNull Voucher voucher, String[] arguments) {
		this.player = player;
		this.voucher = voucher;
		this.arguments = arguments != null ? arguments : new String[0];
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
