package ca.tweetzy.vouchers.menus.template;

import ca.tweetzy.tweety.gui.Gui;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.model.QuickIcon;
import lombok.NonNull;

/**
 * Date Created: February 28 2022
 * Time Created: 1:36 a.m.
 *
 * @author Kiran Hart
 */
public abstract class View extends Gui {

	public View(@NonNull final String title) {
		setTitle(Common.colorize(title));
		setDefaultItem(ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make());
		generateNavigation();
	}

	protected void generateNavigation() {
		setPrevPage(QuickIcon.prevButton());
		setNextPage(QuickIcon.nextButton());
	}
}
