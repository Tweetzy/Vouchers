/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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

package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.flight.collection.ProbabilityCollection;
import ca.tweetzy.flight.comp.Titles;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.ItemUtil;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.events.VoucherRedeemEvent;
import ca.tweetzy.vouchers.api.events.VoucherRedeemResult;
import ca.tweetzy.vouchers.api.voucher.*;
import ca.tweetzy.vouchers.gui.GUIRewardSelection;
import ca.tweetzy.vouchers.impl.VoucherRedeem;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.QuickReplace;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RedeemManager extends Manager<UUID, Redeem> {

	@Override
	public List<Redeem> getAll() {
		return List.copyOf(this.contents.values());
	}

	@Override
	public Redeem find(@NonNull UUID uuid) {
		return this.contents.getOrDefault(uuid, null);
	}

	@Override
	public void add(@NonNull Redeem redeem) {
		this.contents.put(redeem.getId(), redeem);
	}

	@Override
	public void remove(@NonNull UUID uuid) {
		this.contents.remove(uuid);
	}

	public int getTotalRedeems(@NonNull final UUID playerUUID, @NonNull final String voucherId) {
		return (int) this.contents.values().stream().filter(redeem -> redeem.getUser().equals(playerUUID) && redeem.getVoucherId().equalsIgnoreCase(voucherId)).count();
	}

	public int getTotalRedeems(@NonNull final Player player, @NonNull final Voucher voucher) {
		return getTotalRedeems(player.getUniqueId(), voucher.getId());
	}

	public boolean isAtRedeemLimit(@NonNull final Player player, @NonNull final Voucher voucher) {
		int maxVoucherUses = voucher.getOptions().getMaxUses();
		if (maxVoucherUses <= -1) return false;

		return getTotalRedeems(player, voucher) >= maxVoucherUses;
	}

	public void redeemVoucher(@NonNull final Player player, @NonNull final Voucher voucher, final boolean ignoreRedeemLimit, final boolean ignoreCooldown) {
		redeemVoucher(player, voucher, ignoreRedeemLimit, ignoreCooldown, Collections.emptyList());
	}

	public void redeemVoucher(@NonNull final Player player, @NonNull final Voucher voucher, final boolean ignoreRedeemLimit, final boolean ignoreCooldown, List<String> args) {
		// check permission
		if ((voucher.getOptions().isRequiresPermission() && !player.hasPermission(voucher.getOptions().getPermission())) || (Settings.HAVING_VOUCHER_PERM_BLOCKS_USAGE.getBoolean() && player.hasPermission(voucher.getOptions().getPermission()))) {
			Common.tell(player, TranslationManager.string(Translations.NOT_ALLOWED_TO_USE));
			Bukkit.getPluginManager().callEvent(new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.FAIL_NO_PERMISSION));
			return;
		}

		if (isAtRedeemLimit(player, voucher) && !ignoreRedeemLimit) {
			Common.tell(player, TranslationManager.string(Translations.REDEEM_LIMIT_REACHED));
			Bukkit.getPluginManager().callEvent(new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.FAIL_AT_MAX_USES));
			return;
		}

		// check cooldown
		if (!ignoreCooldown)
			if (Vouchers.getCooldownManager().isPlayerInCooldown(player.getUniqueId()) && Vouchers.getCooldownManager().isPlayerInCooldownForVoucher(player.getUniqueId(), voucher)) {
				long cooldownTime = Vouchers.getCooldownManager().getCooldownTime(player.getUniqueId(), voucher);

				if (System.currentTimeMillis() < cooldownTime) {
					Common.tell(player, TranslationManager.string(Translations.WAIT_FOR_COOLDOWN, "cooldown_time", String.format("%,.2f", (cooldownTime - System.currentTimeMillis()) / 1000F)));
					Bukkit.getPluginManager().callEvent(new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.FAIL_HAS_COOLDOWN));
					return;
				}
			}

		final VoucherRedeemEvent voucherRedeemEvent = new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.SUCCESS);
		Bukkit.getPluginManager().callEvent(voucherRedeemEvent);

		if (voucherRedeemEvent.isCancelled()) return;

		// play sound
		if (voucher.getOptions().isPlayingSound())
			voucher.getOptions().getSound().play(player);

		// collect titles
		if (!voucher.getOptions().getMessages().isEmpty()) {
			final Message titleMessage = voucher.getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.TITLE).findFirst().orElse(null);
			final Message subtitleMessage = voucher.getOptions().getMessages().stream().filter(msg -> msg.getMessageType() == MessageType.SUBTITLE).findFirst().orElse(null);

			int fadeIn = 20;
			int fadeOut = 20;
			int stay = 20;

			if (titleMessage != null) {
				fadeIn = titleMessage.getFadeInDuration();
				fadeOut = titleMessage.getFadeOutDuration();
				stay = titleMessage.getStayDuration();
			}

			if (subtitleMessage != null) {
				fadeIn = Math.max(subtitleMessage.getFadeInDuration(), fadeIn);
				fadeOut = Math.max(subtitleMessage.getFadeOutDuration(), fadeOut);
				stay = Math.max(subtitleMessage.getStayDuration(), stay);
			}

			if (!(titleMessage == null && subtitleMessage == null)) {
				Titles.sendTitle(
						player,
						fadeIn,
						stay,
						fadeOut,
						Common.colorize(titleMessage != null ? java.text.MessageFormat.format(titleMessage.getColouredAndReplaced(player, voucher), args.toArray()) : ""),
						Common.colorize(subtitleMessage != null ? java.text.MessageFormat.format(subtitleMessage.getColouredAndReplaced(player, voucher), args.toArray()) : "")
				);
			}

			// the other message types
			voucher.getOptions().getMessages().stream().filter(msg -> msg.getMessageType() != MessageType.TITLE && msg.getMessageType() != MessageType.SUBTITLE).collect(Collectors.toList()).forEach(msg -> {
				msg.send(player, voucher, args);
			});
		}

		// rewards
		switch (voucher.getRewardMode()) {
			case AUTOMATIC -> {
				// automatic means it will give them every reward added to the voucher

				if (Settings.SHOW_VOUCHER_REWARD_INFO.getBoolean()) {
					showRewardHeader(player);
					voucher.getRewards().forEach(reward -> {
						boolean wasGiven = reward.execute(player, false, args);
						if (wasGiven)
							showActualRewardGiven(player, reward, args);
					});
					showRewardFooter(player);
				} else {
					voucher.getRewards().forEach(reward -> reward.execute(player, false, args));
				}


				takeHand(player, voucher);
				if (!ignoreCooldown)
					Vouchers.getCooldownManager().addPlayerToCooldown(player.getUniqueId(), voucher);
				registerRedeemIfApplicable(player, voucher);
			}
			case REWARD_SELECT -> Vouchers.getGuiManager().showGUI(player, new GUIRewardSelection(voucher, args, selected -> {
				boolean given = selected.execute(player, Settings.REWARD_PICK_IS_GUARANTEED.getBoolean(), args);

				if (Settings.SHOW_VOUCHER_REWARD_INFO.getBoolean()) {
					showRewardHeader(player);
					if (given)
						showActualRewardGiven(player, selected, args);
					showRewardFooter(player);
				}

				takeHand(player, voucher);
				player.closeInventory();

				if (!ignoreCooldown)
					Vouchers.getCooldownManager().addPlayerToCooldown(player.getUniqueId(), voucher);
				registerRedeemIfApplicable(player, voucher);
			}));
			case RANDOM -> {
				final ProbabilityCollection<Reward> rewardProbabilityCollection = new ProbabilityCollection<>();
				voucher.getRewards().forEach(reward -> rewardProbabilityCollection.add(reward, (int) reward.getChance()));

				Reward selectedReward = rewardProbabilityCollection.get();
				selectedReward.execute(player, true, args);
				if (Settings.SHOW_VOUCHER_REWARD_INFO.getBoolean())
					showRewardInfo(player, selectedReward, args);

				takeHand(player, voucher);
				if (!ignoreCooldown)
					Vouchers.getCooldownManager().addPlayerToCooldown(player.getUniqueId(), voucher);


				registerRedeemIfApplicable(player, voucher);
			}
		}
	}

	private void showRewardInfo(@NonNull final Player player, @NonNull final Reward reward, List<String> args) {
		showRewardHeader(player);
		showActualRewardGiven(player, reward, args);
		showRewardFooter(player);
	}

	private void showRewardHeader(@NonNull final Player player) {
		Common.tellNoPrefix(player, TranslationManager.list(Translations.VOUCHER_REWARD_INFO_HEADER).toArray(new String[0]));
	}

	private void showActualRewardGiven(@NonNull final Player player, @NonNull final Reward reward, List<String> args) {
		if (reward instanceof final ItemReward itemReward)
			TranslationManager.list(Translations.VOUCHER_REWARD_INFO_ITEM, "item_quantity", itemReward.getItem().getAmount(), "item_name", ItemUtil.getItemName(itemReward.getItem())).forEach(line -> Common.tellNoPrefix(player, line));

		if (reward instanceof final CommandReward commandReward) {
			if (commandReward.getClaimMessage().isEmpty())
				TranslationManager.list(Translations.VOUCHER_REWARD_INFO_COMMAND, "reward_command", MessageFormat.format(commandReward.getCommand().replace("%player%", player.getName()), args.toArray())).forEach(line -> Common.tellNoPrefix(player, line));
			else
				Common.tellNoPrefix(player, QuickReplace.getColouredAndReplaced(player, commandReward.getClaimMessage(), null));

		}
	}

	private void showRewardFooter(@NonNull final Player player) {
		Common.tellNoPrefix(player, TranslationManager.list(Translations.VOUCHER_REWARD_INFO_FOOTER).toArray(new String[0]));
	}

	public void registerRedeemIfApplicable(@NonNull final Player player, @NonNull final Voucher voucher) {
		Vouchers.getDataManager().createVoucherRedeem(new VoucherRedeem(UUID.randomUUID(), player.getUniqueId(), voucher.getId(), System.currentTimeMillis()), (error, createdRedeem) -> {
			if (error == null)
				this.add(createdRedeem);
			else
				error.printStackTrace();
		});
	}

	public void deleteRedeems(@NonNull final Player player, @NonNull final String voucherId) {
		Vouchers.getDataManager().deleteRedeems(player.getUniqueId(), voucherId, (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(player, voucherId).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems(@NonNull final Player player) {
		Vouchers.getDataManager().deleteAllRedeems(player.getUniqueId(), (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(player).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems(@NonNull final String voucherId) {
		Vouchers.getDataManager().deleteAllRedeems(voucherId, (error, deleted) -> {
			if (error == null && deleted) {
				getRedeemIds(voucherId).forEach(this::remove);
			}
		});
	}

	public void deleteAllRedeems() {
		Vouchers.getDataManager().deleteAllRedeems((error, deleted) -> {
			if (error == null && deleted) {
				this.contents.clear();
			}
		});
	}


	private List<UUID> getRedeemIds(@NonNull final Player player, @NonNull final String voucherId) {
		return this.contents.values().stream().filter(redeem -> redeem.getUser().equals(player.getUniqueId()) && redeem.getVoucherId().equalsIgnoreCase(voucherId)).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}

	private List<UUID> getRedeemIds(@NonNull final Player player) {
		return this.contents.values().stream().filter(redeem -> redeem.getUser().equals(player.getUniqueId())).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}

	private List<UUID> getRedeemIds(@NonNull final String voucherId) {
		return this.contents.values().stream().filter(redeem -> redeem.getVoucherId().equalsIgnoreCase(voucherId)).toList().stream().map(Redeem::getId).collect(Collectors.toList());
	}

	private void takeHand(@NonNull final Player player, @NonNull final Voucher voucher) {
		if (voucher.getOptions().isRemoveOnUse()) {
			if (PlayerUtil.getHand(player).getAmount() >= 2) {
				PlayerUtil.getHand(player).setAmount(PlayerUtil.getHand(player).getAmount() - 1);
			} else {
				player.getInventory().setItemInMainHand(CompMaterial.AIR.parseItem());
			}

			player.updateInventory();
		}
	}

	@Override
	public void load() {
		this.contents.clear();

		Vouchers.getDataManager().getVoucherRedeems((error, all) -> {
			if (error == null)
				all.forEach(this::add);
		});
	}
}
