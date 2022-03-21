package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.menus.template.MaterialSelectorMenu;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Date Created: March 17 2022
 * Time Created: 3:57 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuVoucherEdit extends View {

	private final Voucher voucher;


	public MenuVoucherEdit(@NonNull final Voucher voucher) {
		super("&b" + voucher.getId() + " &8> &7Edit");
		this.voucher = voucher;
		setRows(5);
		draw();
	}

	private void draw() {
		reset();

		setButton(9 * 3 + 3, ItemCreator.of(CompMaterial.REPEATER, "&e&lVoucher Settings", "", "&dClick &7to edit voucher settings").make(), click -> click.manager.showGUI(click.player, new MenuVoucherSettings(this.voucher)));
		setButton(9 * 3 + 5, ItemCreator.of(CompMaterial.DIAMOND, "&e&lVoucher Rewards", "", "&dClick &7to edit voucher rewards").make(), click -> click.manager.showGUI(click.player, new MenuVoucherRewards(this.voucher)));

		setButton(11, ItemCreator.of(this.voucher.getIcon(), "&e&lVoucher Material", "", "&dClick &7to edit voucher item").make(), click -> {
			Vouchers.getGuiManager().showGUI(click.player, new MaterialSelectorMenu(selected -> {
				if (selected != null) {
					this.voucher.setIcon(selected);
					click.manager.showGUI(click.player, new MenuVoucherEdit(this.voucher));
				}
			}));
		});

		setButton(9 + 4, ItemCreator.of(CompMaterial.NAME_TAG, "&e&lVoucher Name", "", "&eCurrent&f: " + this.voucher.getDisplayName(), "", "&dClick &7to change name").make(), click -> {
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_NAME_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_NAME_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherEdit.this.voucher.setDisplayName(name);
					click.manager.showGUI(click.player, new MenuVoucherEdit(MenuVoucherEdit.this.voucher));
					return true;
				}
			};
		});

		final List<String> descLore = new ArrayList<>();
		descLore.add("");
		descLore.addAll(this.voucher.getDescription().getSource());
		descLore.add("");
		descLore.add("&dClick &7to edit voucher item");

		setButton(9 + 6, ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&e&lVoucher Description").lore(descLore).make(), click -> click.manager.showGUI(click.player, new MenuStringListEdit(this.voucher)));
	}
}
