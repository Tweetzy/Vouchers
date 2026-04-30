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

package ca.tweetzy.vouchers.api;

import ca.tweetzy.vouchers.model.manager.CategoryManager;
import ca.tweetzy.vouchers.model.manager.CooldownManager;
import ca.tweetzy.vouchers.model.manager.RedeemManager;
import ca.tweetzy.vouchers.model.manager.VoucherManager;

public interface VouchersAPI {

	VoucherManager getVoucherManager();

	RedeemManager getRedeemManager();

	CooldownManager getCooldownManager();

	CategoryManager getCategoryManager();
}
