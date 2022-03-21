package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.util.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Settings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date Created: March 17 2022
 * Time Created: 3:46 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuVoucherList extends View {

	public MenuVoucherList() {
		super(Settings.VOUCHER_LIST_MENU_TITLE.getString());
		setDefaultItem(ItemCreator.of(Settings.VOUCHER_LIST_MENU_BACKGROUND.getMaterial()).name(" ").make());
		draw();
	}

	private void draw() {
		reset();

		final List<Voucher> itemsToFill = Vouchers.getVoucherManager().getVouchers().stream().skip((page - 1) * (long) 45).limit(45).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(Vouchers.getVoucherManager().getVouchers().size() / (double) 45));

		generateNavigation();
		setOnPage(e -> draw());

		int slot = 0;
		for (Voucher voucher : itemsToFill) {
			setButton(slot++, voucher.build(), click -> {
				if (click.player.hasPermission("vouchers.admin"))
					PlayerUtil.addItems(click.player.getInventory(), voucher.build());
			});
		}
	}
}
