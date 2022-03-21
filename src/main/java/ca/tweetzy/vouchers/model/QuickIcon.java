package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.vouchers.settings.Settings;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

/**
 * Date Created: March 17 2022
 * Time Created: 3:48 p.m.
 *
 * @author Kiran Hart
 */
@UtilityClass
public final class QuickIcon {

	public ItemStack backButton() {
		return ItemCreator.of(Settings.GUI_BACK_BTN_ITEM.getMaterial()).name(Settings.GUI_BACK_BTN_NAME.getString()).lore(Settings.GUI_BACK_BTN_LORE.getStringList()).make();
	}

	public ItemStack prevButton() {
		return ItemCreator.of(Settings.GUI_PREV_BTN_ITEM.getMaterial()).name(Settings.GUI_PREV_BTN_NAME.getString()).lore(Settings.GUI_PREV_BTN_LORE.getStringList()).make();
	}

	public ItemStack nextButton() {
		return ItemCreator.of(Settings.GUI_NEXT_BTN_ITEM.getMaterial()).name(Settings.GUI_NEXT_BTN_NAME.getString()).lore(Settings.GUI_NEXT_BTN_LORE.getStringList()).make();
	}
}
