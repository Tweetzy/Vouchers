package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.ItemUtil;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.model.InventorySafeMaterials;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 6:38 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuMaterialSelector extends MenuPagged<CompMaterial> {

	private final Voucher voucher;
	private final Button backButton;


	public MenuMaterialSelector(@NonNull final Voucher voucher) {
		super(InventorySafeMaterials.get());
		setTitle("&eSelect Material");
		this.voucher = voucher;
		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherEdit(this.voucher).displayTo(player));

	}

	@Override
	protected List<Button> getButtonsToAutoRegister() {
		return Collections.singletonList(this.backButton);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == getSize() - 9)
			return this.backButton.getItem();

		return super.getItemAt(slot);
	}

	@Override
	protected ItemStack convertToItemStack(CompMaterial compMaterial) {
		return ItemCreator.of(compMaterial).name("&e&l" + ItemUtil.bountifyCapitalized(compMaterial)).lore("&dClick &7to select this material").make();
	}

	@Override
	protected void onPageClick(Player player, CompMaterial compMaterial, ClickType clickType) {
		this.voucher.setIcon(compMaterial);
		Common.runAsync(() -> Vouchers.getVoucherManager().getVoucherHolder().save());
		new MenuVoucherEdit(this.voucher).displayTo(player);
	}

	@Override
	public Menu newInstance() {
		return new MenuMaterialSelector(this.voucher);
	}
}
