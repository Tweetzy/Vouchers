package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.MathUtil;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Settings;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 4:00 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuConfirm extends Menu {

	private final Voucher voucher;
	private final ItemStack voucherItem;

	private final Button cancelButton;
	private final Button confirmButton;

	public MenuConfirm(@Nonnull final Voucher voucher, @Nonnull final ItemStack voucherItem) {
		setTitle(Settings.ConfirmMenu.TITLE);
		setSize(MathUtil.atLeast(9, Settings.ConfirmMenu.ROWS * 9));
		this.voucher = voucher;
		this.voucherItem = voucherItem;

		this.cancelButton = Button.makeSimple(Settings.ConfirmMenu.CANCEL_ITEM.build(), HumanEntity::closeInventory);
		this.confirmButton = Button.makeSimple(Settings.ConfirmMenu.CONFIRM_ITEM.build(), player -> {
			Vouchers.getVoucherManager().executeVoucher(player, this.voucher, this.voucherItem);
			player.closeInventory();
		});
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (Settings.ConfirmMenu.CANCEL_ITEM.getSlots().contains(slot))
			return this.cancelButton.getItem();

		if (Settings.ConfirmMenu.CONFIRM_ITEM.getSlots().contains(slot))
			return this.confirmButton.getItem();

		return ItemCreator.of(Settings.ConfirmMenu.BACKGROUND_ITEM).name(" ").make();
	}

	@Override
	public Menu newInstance() {
		return new MenuConfirm(this.voucher, this.voucherItem);
	}
}
