/*
 * Vouchers
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.gui;

import ca.tweetzy.feather.comp.enums.CompMaterial;
import ca.tweetzy.feather.gui.template.BaseGUI;
import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public final class GUIConfirm extends BaseGUI {

	private final Consumer<Boolean> onClick;

	public GUIConfirm(@NonNull final Consumer<Boolean> onClick, @NonNull final Consumer<Player> onExit) {
		super(null, Locale.GUI_CONFIRM_TITLE.getString(), 3);
		this.onClick = onClick;
		setOnClose(close -> onExit.accept(close.player));
		draw();
	}

	@Override
	protected void draw() {

		// 9 10 11 12 13 14 15 16 17

		for (int i = 10; i <= 12; i++)
			setButton(i, QuickItem.of(CompMaterial.RED_STAINED_GLASS_PANE).name(Locale.GUI_CONFIRM_ITEM_NO_NAME.getString()).lore((List<String>) Locale.GUI_CONFIRM_ITEM_NO_LORE.get()).make(), click -> this.onClick.accept(false));

		for (int i = 14; i <= 16; i++)
			setButton(i, QuickItem.of(CompMaterial.LIME_STAINED_GLASS_PANE).name(Locale.GUI_CONFIRM_ITEM_YES_NAME.getString()).lore((List<String>) Locale.GUI_CONFIRM_ITEM_YES_LORE.get()).make(), click -> this.onClick.accept(true));

	}
}
