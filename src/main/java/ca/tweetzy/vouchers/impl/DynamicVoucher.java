package ca.tweetzy.vouchers.impl;

import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import ca.tweetzy.vouchers.api.voucher.VoucherType;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class DynamicVoucher extends StandardVoucher {

	@Getter
	@Setter
	private String[] arguments;

	public DynamicVoucher(
			@NonNull final String id,
			@NonNull final String item,
			@NonNull final String name,
			@NonNull final List<String> description,
			@NonNull final VoucherSettings settings,
			@NonNull final List<Message> messages,
			@NonNull final List<Reward> rewards,
			@NonNull final String[] arguments
	) {
		super(id, item, name, description, settings, messages, rewards);
		this.setVoucherType(VoucherType.DYNAMIC);
		this.arguments = arguments;
	}

}
