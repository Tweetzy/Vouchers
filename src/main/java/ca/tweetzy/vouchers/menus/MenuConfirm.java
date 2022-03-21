package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompSound;
import ca.tweetzy.tweety.util.MathUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Date Created: March 17 2022
 * Time Created: 4:13 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuConfirm extends View {

	private final Voucher voucher;
	private final ItemStack voucherItem;

	public MenuConfirm(@Nonnull final Voucher voucher, @Nonnull final ItemStack voucherItem) {
		super(Settings.CONFIRM_MENU_TITLE.getString());
		setRows(MathUtil.atLeast(1, Settings.CONFIRM_MENU_ROWS.getInt()));
		setDefaultItem(ItemCreator.of(Settings.CONFIRM_MENU_BACKGROUND.getMaterial()).name(" ").make());
		setDefaultSound(CompSound.valueOf(voucher.getSettings().getSound().getSound().name().toUpperCase()));
		this.voucher = voucher;
		this.voucherItem = voucherItem;

		draw();
	}

	private void draw() {
		reset();

		for (int i : Settings.CONFIRM_MENU_ITEMS_CANCEL_SLOTS.getIntegerList()) {
			setButton(i, ItemCreator
					.of(Settings.CONFIRM_MENU_ITEMS_CANCEL_MATERIAL.getMaterial())
					.name(Settings.CONFIRM_MENU_ITEMS_CANCEL_NAME.getString())
					.lore(Settings.CONFIRM_MENU_ITEMS_CANCEL_LORE.getStringList()).make(), click -> click.player.closeInventory());
		}

		for (int i : Settings.CONFIRM_MENU_ITEMS_CONFIRM_SLOTS.getIntegerList()) {
			setButton(i, ItemCreator
					.of(Settings.CONFIRM_MENU_ITEMS_CONFIRM_MATERIAL.getMaterial())
					.name(Settings.CONFIRM_MENU_ITEMS_CONFIRM_NAME.getString())
					.lore(Settings.CONFIRM_MENU_ITEMS_CONFIRM_LORE.getStringList()).make(), click -> {

				Vouchers.getVoucherManager().executeVoucher(click.player, this.voucher, this.voucherItem);
				click.player.closeInventory();
			});
		}
	}
}
