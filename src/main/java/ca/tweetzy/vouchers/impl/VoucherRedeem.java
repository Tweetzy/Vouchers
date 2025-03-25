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