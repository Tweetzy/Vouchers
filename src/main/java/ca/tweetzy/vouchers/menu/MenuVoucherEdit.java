package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.button.ButtonMenu;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Localization;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 6:40 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuVoucherEdit extends Menu {

	private final Voucher voucher;

	private final Button iconButton;
	private final Button displayNameButton;
	private final Button descriptionButton;
	private final Button settingsButton;

	public MenuVoucherEdit(@NonNull final Voucher voucher) {
		this.voucher = voucher;
		setTitle("&b" + voucher.getId() + " &8> &7Edit");
		setSize(9 * 5);

		this.iconButton = new ButtonMenu(new MenuMaterialSelector(this.voucher), ItemCreator.of(voucher.getIcon(), "&e&lVoucher Material", "", "&dClick &7to edit voucher item"));
		this.displayNameButton = Button.makeSimple(ItemCreator.of(CompMaterial.NAME_TAG, "&e&lVoucher Name", "", "&eCurrent&f: " + voucher.getDisplayName(), "", "&dClick &7to change name"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_NAME_TITLE, Localization.VoucherEdit.ENTER_NAME_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherEdit.this.voucher.setDisplayName(name);
				saveReopen(player);
				return true;
			}
		});

		final List<String> descLore = new ArrayList<>();
		descLore.add("");
		descLore.addAll(this.voucher.getDescription());
		descLore.add("");
		descLore.add("&dClick &7to edit voucher item");

		this.descriptionButton = new ButtonMenu(new MenuStringListEdit(this.voucher), ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&e&lVoucher Description").lore(descLore));
		this.settingsButton = new ButtonMenu(new MenuVoucherSettings(this.voucher), ItemCreator.of(CompMaterial.REPEATER, "&e&lVoucher Settings", "", "&dClick &7to edit voucher settings"));

	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == 9 + 2)
			return this.iconButton.getItem();

		if (slot == 9 + 4)
			return this.displayNameButton.getItem();

		if (slot == 9 + 6)
			return this.descriptionButton.getItem();

		if (slot == 9 * 3 + 3)
			return this.settingsButton.getItem();

		return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make();
	}

	private void saveReopen(@NonNull final Player player) {
		Vouchers.getVoucherManager().getVoucherHolder().save();
		newInstance().displayTo(player);
	}

	@Override
	public Menu newInstance() {
		return new MenuVoucherEdit(this.voucher);
	}
}
