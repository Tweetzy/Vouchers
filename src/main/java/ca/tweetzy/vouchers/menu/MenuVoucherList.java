package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.PlayerUtil;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 5:44 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuVoucherList extends MenuPagged<Voucher> {

	public MenuVoucherList() {
		super(Vouchers.getVoucherManager().getVouchers());
		setTitle(Settings.VoucherListMenu.TITLE);
	}

	@Override
	protected ItemStack backgroundItem() {
		return ItemCreator.of(Settings.VoucherListMenu.BACKGROUND_ITEM).name(" ").make();
	}

	@Override
	protected ItemStack convertToItemStack(Voucher voucher) {
		return voucher.build();
	}

	@Override
	protected void onPageClick(Player player, Voucher voucher, ClickType click) {
		if (player.hasPermission("vouchers.admin"))
			PlayerUtil.addItems(player.getInventory(), voucher.build());
	}
}
