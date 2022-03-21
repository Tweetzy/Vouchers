package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date Created: March 17 2022
 * Time Created: 4:37 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuStringListEdit extends View {

	private final Voucher voucher;

	public MenuStringListEdit(@NonNull final Voucher voucher) {
		super("&b" + voucher.getId() + " &8> &7Description");
		setRows(4);
		this.voucher = voucher;
		draw();
	}

	private void draw() {
		reset();

		final List<String> itemsToFill = this.voucher.getDescription().getSource().stream().skip((page - 1) * (long) 27).limit(27).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(this.voucher.getDescription().size() / (double) 27));

		generateNavigation();
		setOnPage(e -> draw());

		setButton((9 * 4) - 9, ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back").make(), click -> click.manager.showGUI(click.player, new MenuVoucherEdit(this.voucher)));
		setButton((9 * 4) - 4, ItemCreator.of(CompMaterial.SLIME_BALL, "&E&lAdd Line", "", "&dClick &7to add a new line").make(), click -> {
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_DESC_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_DESC_SUBTITLE.getString()) {

				@Override
				public boolean onResult(String line) {
					if (line == null || line.length() < 1)
						return false;

					MenuStringListEdit.this.voucher.getDescription().addWeak(line);
					click.manager.showGUI(click.player, new MenuStringListEdit(MenuStringListEdit.this.voucher));
					return true;
				}
			};
		});

		int slot = 0;
		for (String line : itemsToFill) {
			setButton(slot++, ItemCreator.of(CompMaterial.PAPER).name(line).lore("&dPress Drop &7to delete this line", "&7Drop is usually Q By default").make(), click -> {
				if (click.player.hasPermission("vouchers.admin")) {
					this.voucher.getDescription().removeWeak(line);
					click.manager.showGUI(click.player, new MenuStringListEdit(this.voucher));
				}
			});
		}
	}
}
