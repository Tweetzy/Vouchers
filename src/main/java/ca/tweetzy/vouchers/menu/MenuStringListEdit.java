package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.settings.Localization;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 7:01 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuStringListEdit extends MenuPagged<String> {

	private final Voucher voucher;
	private final Button addLineButton;
	private final Button backButton;

	public MenuStringListEdit(@NonNull final Voucher voucher) {
		super(voucher.getDescription());
		this.voucher = voucher;
		setTitle("&b" + voucher.getId() + " &8> &7Description");

		this.addLineButton = Button.makeSimple(ItemCreator.of(CompMaterial.SLIME_BALL, "&E&lAdd Line", "", "&dClick &7to add a new line"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_DESC_TITLE, Localization.VoucherEdit.ENTER_DESC_SUBTITLE) {

			@Override
			public boolean onResult(String line) {
				if (line == null || line.length() < 1)
					return false;



				MenuStringListEdit.this.voucher.getDescription().add(line);
				saveReopen(player);
				return true;
			}
		});

		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherEdit(this.voucher).displayTo(player));
	}

	@Override
	protected List<Button> getButtonsToAutoRegister() {
		return Arrays.asList(this.addLineButton, this.backButton);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == getBottomCenterSlot())
			return this.addLineButton.getItem();
		if (slot == getSize() - 9)
			return this.backButton.getItem();

		return super.getItemAt(slot);
	}

	@Override
	protected ItemStack convertToItemStack(String line) {
		return ItemCreator
				.of(CompMaterial.PAPER)
				.name(line)
				.lore("&dPress Drop &7to delete this line")
				.make();
	}

	@Override
	protected void onPageClick(Player player, String s, ClickType clickType) {
		if (clickType == ClickType.DROP) {
			if (!this.voucher.getDescription().contains(s)) return;

			this.voucher.getDescription().remove(s);
			saveReopen(player);
		}
	}

	private void saveReopen(@NonNull final Player player) {
		Vouchers.getVoucherManager().getVoucherHolder().save();
		newInstance().displayTo(player);
	}

	@Override
	public Menu newInstance() {
		return new MenuStringListEdit(this.voucher);
	}
}
