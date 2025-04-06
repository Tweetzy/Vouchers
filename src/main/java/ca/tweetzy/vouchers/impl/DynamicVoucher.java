/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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
		this.setArgs(arguments);
	}

}
