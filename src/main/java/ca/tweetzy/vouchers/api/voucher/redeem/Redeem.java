package ca.tweetzy.vouchers.api.voucher.redeem;

import ca.tweetzy.vouchers.api.sync.Identifiable;

import java.util.UUID;

public interface Redeem extends Identifiable<UUID> {

	UUID getUser();

	String getVoucherId();

	long getTime();
}