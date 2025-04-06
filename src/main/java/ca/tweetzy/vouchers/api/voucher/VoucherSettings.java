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

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;

public interface VoucherSettings {

	boolean useGlow();

	void setUseGlow(final boolean glow);

	boolean usePermission();

	void setUsePermission(final boolean usePermission);

	String getPermission();

	void setPermission(final String permission);

	boolean useSound();

	void setUseSound(final boolean useSound);

	CompSound getSound();

	void setSound(final CompSound sound);

	boolean isRemoveOnUse();

	void setRemoveOnUse(final boolean removeOnUse);

	boolean isAskForConfirm();

	void setAskForConfirmation(final boolean askForConfirmation);

	int getMaximumUses();

	void setMaximumUses(final int maximumUses);

	boolean useCooldown();

	void setUseCooldown(final boolean useCooldown);

	long getCooldown();

	void setCooldown(final long seconds);

	RewardMode getRewardMode();

	void setRewardMode(final RewardMode rewardMode);

	int getMaximumRewards();

	void setMaximumRewards(final int maxRewards);
}
