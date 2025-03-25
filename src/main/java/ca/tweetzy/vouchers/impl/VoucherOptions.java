package ca.tweetzy.vouchers.impl;

import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public final class VoucherOptions implements VoucherSettings {

	private boolean glow;
	private boolean usePermission;
	private String permission;
	private boolean useSound;
	private CompSound sound;
	private boolean removeOnUse;
	private boolean askForConfirm;
	private int maximumUses;
	private boolean useCooldown;
	private long cooldown;
	private RewardMode rewardMode;
	private int maximumRewards;

	@Override
	public boolean useGlow() {
		return this.glow;
	}

	@Override
	public void setUseGlow(boolean glow) {
		this.glow = glow;
	}

	@Override
	public boolean usePermission() {
		return this.usePermission;
	}

	@Override
	public void setUsePermission(boolean usePermission) {
		this.usePermission = usePermission;
	}

	@Override
	public String getPermission() {
		return this.permission;
	}

	@Override
	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public boolean useSound() {
		return this.useSound;
	}

	@Override
	public void setUseSound(boolean useSound) {
		this.useSound = useSound;
	}

	@Override
	public CompSound getSound() {
		return this.sound;
	}

	@Override
	public void setSound(CompSound sound) {
		this.sound = sound;
	}

	@Override
	public boolean isRemoveOnUse() {
		return this.removeOnUse;
	}

	@Override
	public void setRemoveOnUse(boolean removeOnUse) {
		this.removeOnUse = removeOnUse;
	}

	@Override
	public boolean isAskForConfirm() {
		return this.askForConfirm;
	}

	@Override
	public void setAskForConfirmation(boolean askForConfirmation) {
		this.askForConfirm = askForConfirmation;
	}

	@Override
	public int getMaximumUses() {
		return this.maximumUses;
	}

	@Override
	public void setMaximumUses(int maximumUses) {
		this.maximumUses = maximumUses;
	}

	@Override
	public boolean useCooldown() {
		return this.useCooldown;
	}

	@Override
	public void setUseCooldown(boolean useCooldown) {
		this.useCooldown = useCooldown;
	}

	@Override
	public long getCooldown() {
		return this.cooldown;
	}

	@Override
	public void setCooldown(long seconds) {
		this.cooldown = seconds;
	}

	@Override
	public RewardMode getRewardMode() {
		return this.rewardMode;
	}

	@Override
	public void setRewardMode(RewardMode rewardMode) {
		this.rewardMode = rewardMode;
	}

	@Override
	public int getMaximumRewards() {
		return this.maximumRewards;
	}

	@Override
	public void setMaximumRewards(int maxRewards) {
		this.maximumRewards = maxRewards;
	}
}
