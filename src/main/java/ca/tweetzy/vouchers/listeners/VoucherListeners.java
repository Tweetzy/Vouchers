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

import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class VoucherListeners implements Listener {

	private final List<UUID> blockedFromDrop = new ArrayList<>();

//	@EventHandler
//	public void onVoucherRedeem(final PlayerInteractEvent event) {
//		final Player player = event.getPlayer();
//		ItemStack item = event.getItem();
//		EquipmentSlot hand = event.getHand();
//
//		// prevent if sneaking
//		if (Settings.PREVENT_REDEEM_WHILE_SNEAKING.getBoolean() && player.isSneaking())
//			return;
//
//		if (item == null) item = event.getPlayer().getInventory().getItemInOffHand();
//
//		// not even a voucher
//		if (!Vouchers.getVoucherManager().isVoucher(item)) return;
//
//		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//
//			final Voucher voucher = Vouchers.getVoucherManager().find(NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:Vouchers")));
//			final String voucherArgsRaw = NBT.get(item, nbt -> (String) nbt.getString("Tweetzy:VouchersArgs"));
//
//			final List<String> voucherArgs = voucherArgsRaw == null ? null : voucherArgsRaw.split(" ").length == 0 ? null : List.of(voucherArgsRaw.split(" "));
//
//			// invalid / deleted voucher
//			if (voucher == null) return;
//
//			event.setUseItemInHand(Event.Result.DENY);
//			voucher.setVoucherHand(hand); // Set the hand the voucher was redeemed in
//
//			if (!this.blockedFromDrop.contains(player.getUniqueId()))
//				this.blockedFromDrop.add(player.getUniqueId());
//
//			if (voucher.getOptions().isAskConfirm()) {
//				Vouchers.getGuiManager().showGUI(player, new GUIConfirm(player, confirmed -> {
//					if (confirmed) {
//						if (voucherArgs == null)
//							Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
//						else Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false, voucherArgs);
//					} else {
//						Bukkit.getPluginManager().callEvent(new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.FAIL_CANCELED_CONFIRM));
//					}
//
//					player.closeInventory();
//					this.blockedFromDrop.remove(player.getUniqueId());
//				}, fail -> {
//					this.blockedFromDrop.remove(player.getUniqueId());
//					Bukkit.getPluginManager().callEvent(new VoucherRedeemEvent(player, voucher, VoucherRedeemResult.FAIL_CANCELED_CONFIRM));
//				}));
//			} else {
//				if (voucherArgs == null)
//					Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false);
//				else Vouchers.getRedeemManager().redeemVoucher(player, voucher, false, false, voucherArgs);
//
//				this.blockedFromDrop.remove(player.getUniqueId());
//			}
//		}
//	}
//
//	@EventHandler
//	public void onVoucherSlotChange(final PlayerItemHeldEvent event) {
//		final Player player = event.getPlayer();
//		if (!this.blockedFromDrop.contains(player.getUniqueId())) return;
//
//		event.setCancelled(true);
//	}
//
//	@EventHandler
//	public void onVoucherDropAttempt(final PlayerDropItemEvent event) {
//		final Player player = event.getPlayer();
//		if (!this.blockedFromDrop.contains(player.getUniqueId())) return;
//
//		final ItemStack item = event.getItemDrop().getItemStack();
//		if (item == null) return;
//
//		if (Vouchers.getVoucherManager().isVoucher(item))
//			event.setCancelled(true);
//	}
//
//	@EventHandler
//	public void onHandSwapWithVoucher(final PlayerSwapHandItemsEvent event) {
//		final ItemStack itemMain = event.getMainHandItem();
//		final ItemStack itemOff = event.getOffHandItem();
//
//		if ((itemMain != null && Vouchers.getVoucherManager().isVoucher(itemMain)) || (itemOff != null && Vouchers.getVoucherManager().isVoucher(itemOff))) {
//			event.setCancelled(true);
//		}
//	}
//
//	@EventHandler
//	public void onRenameAttempt(final PrepareAnvilEvent event) {
//		final ItemStack item = event.getResult();
//
//		if (item == null) return;
//		if (!Vouchers.getVoucherManager().isVoucher(item)) return;
//
//		event.setResult(CompMaterial.AIR.parseItem());
//	}
}
