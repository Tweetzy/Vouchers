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

package ca.tweetzy.vouchers.listeners;

import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.events.VoucherPostRedeemEvent;
import ca.tweetzy.vouchers.api.events.VoucherPreRedeemEvent;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.gui.user.VoucherConfirmationGUI;
import ca.tweetzy.vouchers.gui.user.VoucherRewardSelectionGUI;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public final class VoucherListeners implements Listener {

	@EventHandler
	public void onVoucherRedeem(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item = event.getItem();

		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}

		if (Settings.PREVENT_REDEEM_WHILE_SNEAKING.getBoolean() && player.isSneaking())
			return;

		if (!Vouchers.getVoucherManager().isVoucher(item)) return;

		final Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) {
			return;
		}

		final Voucher voucher = Vouchers.getVoucherManager().get(NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:Vouchers")));
		final String voucherArgsRaw = NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:VouchersArgs"));

		final List<String> voucherArgs = voucherArgsRaw == null ? Collections.emptyList() : voucherArgsRaw.split(" ").length == 0 ? Collections.emptyList() : List.of(voucherArgsRaw.split(" "));
		final String[] argsArray = voucherArgs.toArray(new String[0]);

		if (voucher == null) {
			blockVoucherInteract(event);
			return;
		}

		if (voucher.getSettings().usePermission() && !player.hasPermission(voucher.getSettings().getPermission())) {
			blockVoucherInteract(event);
			Common.tell(player, TranslationManager.string(Translations.NOT_ALLOWED_TO_USE));
			return;
		}

		if (Vouchers.getRedeemManager().isAtRedeemLimit(player, voucher)) {
			blockVoucherInteract(event);
			Common.tell(player, TranslationManager.string(Translations.REDEEM_LIMIT_REACHED));
			return;
		}

		if (voucher.getSettings().useCooldown() && !passedCooldown(player, voucher)) {
			blockVoucherInteract(event);
			return;
		}

		blockVoucherInteract(event);

		if (voucher.getSettings().isAskForConfirm()) {
			Vouchers.getGuiManager().showGUI(player, new VoucherConfirmationGUI(player, confirmed -> {
				if (confirmed) {
					Vouchers.newChain().sync(() -> runRedeemFlow(player, voucher, voucherArgsRaw, argsArray)).execute();
				}
				player.closeInventory();
			}));
		} else {
			runRedeemFlow(player, voucher, voucherArgsRaw, argsArray);
		}
	}

	private static void runRedeemFlow(final Player player, final Voucher voucher, final String voucherArgsRaw, final String[] argsArray) {
		final VoucherPreRedeemEvent pre = new VoucherPreRedeemEvent(player, voucher, argsArray);
		Bukkit.getPluginManager().callEvent(pre);
		if (pre.isCancelled()) return;

		if (voucher.getSettings().getRewardMode() == RewardMode.SELECTION) {
			voucher.execute(player, argsArray);
			Vouchers.getGuiManager().showGUI(player, new VoucherRewardSelectionGUI(player, voucher, argsArray, voucherArgsRaw,
					() -> applyPostRedeemEffects(player, voucher, argsArray, voucherArgsRaw)));
			return;
		}

		final boolean successfulUse = voucher.execute(player, argsArray);
		if (successfulUse) {
			applyPostRedeemEffects(player, voucher, argsArray, voucherArgsRaw);
		}
	}

	private static void applyPostRedeemEffects(final Player player, final Voucher voucher, final String[] argsArray, final String voucherArgsRaw) {
		final ItemStack stackForRemove = voucher.getSettings().isRemoveOnUse()
				? Vouchers.getVoucherManager().findMatchingVoucherStack(player, voucher.getId(), voucherArgsRaw)
				: null;

		if (voucher.getSettings().isRemoveOnUse() && stackForRemove == null) {
			Common.tell(player, TranslationManager.string(Translations.VOUCHER_ITEM_NOT_FOUND));
			return;
		}

		Vouchers.getRedeemManager().registerRedeemIfApplicable(player, voucher);

		if (voucher.getSettings().isRemoveOnUse() && stackForRemove != null) {
			PlayerUtil.removeSpecificItemQuantityFromPlayer(player, stackForRemove, 1);
		}

		if (voucher.getSettings().useCooldown()) {
			Vouchers.getCooldownManager().addPlayerToCooldown(player, voucher);
		}

		Bukkit.getPluginManager().callEvent(new VoucherPostRedeemEvent(player, voucher, argsArray));
	}

	private static void blockVoucherInteract(final PlayerInteractEvent event) {
		event.setCancelled(true);
		event.setUseItemInHand(Event.Result.DENY);
		event.setUseInteractedBlock(Event.Result.DENY);
	}

	private boolean passedCooldown(Player player, Voucher voucher) {
		if (!voucher.getSettings().useCooldown()) return true;

		if (Vouchers.getCooldownManager().isPlayerInCooldown(player) && Vouchers.getCooldownManager().isPlayerInCooldownForVoucher(player, voucher)) {
			long cooldownTime = Vouchers.getCooldownManager().getCooldownTime(player, voucher);

			if (System.currentTimeMillis() < cooldownTime) {

				final String time = TimeConverter.convertSecondsToHumanReadable((cooldownTime - System.currentTimeMillis()) / 1000L);
				Common.tell(player, TranslationManager.string(Translations.WAIT_FOR_COOLDOWN, "cooldown_time", time));
				return false;
			}
		}

		return true;
	}
}
