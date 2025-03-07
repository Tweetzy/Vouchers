package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.flight.comp.enums.CompSound;

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

	int getCooldown();

	void setCooldown(final int seconds);
}
