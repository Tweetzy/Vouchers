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

package ca.tweetzy.vouchers.listeners;

import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.user.VoucherConfirmationGUI;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.model.manager.CooldownManager;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import org.bukkit.entity.Player;
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
		ItemStack item = event.getItem();

		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}

		// prevent if sneaking
		if (Settings.PREVENT_REDEEM_WHILE_SNEAKING.getBoolean() && player.isSneaking())
			return;

		// not even a voucher
		if (!Vouchers.getVoucherManager().isVoucher(item)) return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			final Voucher voucher = Vouchers.getVoucherManager().get(NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:Vouchers")));
			final String voucherArgsRaw = NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:VouchersArgs"));

			final List<String> voucherArgs = voucherArgsRaw == null ? Collections.emptyList() : voucherArgsRaw.split(" ").length == 0 ? Collections.emptyList() : List.of(voucherArgsRaw.split(" "));

			// invalid / deleted voucher
			if (voucher == null) return;

			// check permission
			if (voucher.getSettings().usePermission() && !player.hasPermission(voucher.getSettings().getPermission())) {
				Common.tell(player, TranslationManager.string(Translations.NOT_ALLOWED_TO_USE));
				return;
			}

			// check max uses
			if(Vouchers.getRedeemManager().isAtRedeemLimit(player, voucher)) {
				Common.tell(player, TranslationManager.string(Translations.REDEEM_LIMIT_REACHED));
				return;
			}

			if (voucher.getSettings().useCooldown() && !passedCooldown(player, voucher)) {
				return;
			}

			// confirmation ask
			if (voucher.getSettings().isAskForConfirm()) {
				Vouchers.getGuiManager().showGUI(player, new VoucherConfirmationGUI(player, confirmed -> {
					if (confirmed) {
						Vouchers.newChain().sync(() -> {
							final boolean successfulUse = voucher.execute(player, voucherArgs.toArray(new String[0]));

							// remove
							if (successfulUse) {
								Vouchers.getRedeemManager().registerRedeemIfApplicable(player, voucher);

								if (voucher.getSettings().isRemoveOnUse())
									PlayerUtil.removeSpecificItemQuantityFromPlayer(player, item, 1);

								if (voucher.getSettings().useCooldown())
									Vouchers.getCooldownManager().addPlayerToCooldown(player, voucher);
							}
						}).execute();
					}

					player.closeInventory();
				}));
			} else {
				final boolean successfulUse = voucher.execute(player, voucherArgs.toArray(new String[0]));

				// remove
				if (successfulUse) {
					Vouchers.getRedeemManager().registerRedeemIfApplicable(player, voucher);

					if (voucher.getSettings().isRemoveOnUse())
						PlayerUtil.removeSpecificItemQuantityFromPlayer(player, item, 1);

					if (voucher.getSettings().useCooldown())
						Vouchers.getCooldownManager().addPlayerToCooldown(player, voucher);
				}
			}
		}
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
