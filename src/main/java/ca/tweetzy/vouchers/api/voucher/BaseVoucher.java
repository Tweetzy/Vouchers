package ca.tweetzy.vouchers.api.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class BaseVoucher implements Voucher {

	@Setter
	private VoucherType voucherType;
}
