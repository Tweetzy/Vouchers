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

package ca.tweetzy.vouchers.gui.user;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class GUIConfirm extends VouchersBaseGUI {

	private final Consumer<Boolean> onClick;

	public GUIConfirm(@NonNull final Player player, @NonNull final Consumer<Boolean> onClick, @NonNull final Consumer<Player> onExit) {
		super(null, player, TranslationManager.string(Translations.GUI_CONFIRM_TITLE), 3);
		this.onClick = onClick;
		setOnClose(close -> onExit.accept(close.player));
		draw();
	}

	@Override
	protected void draw() {

		// 9 10 11 12 13 14 15 16 17

		for (int i = 10; i <= 12; i++)
			setButton(i, QuickItem
					.of(CompMaterial.RED_STAINED_GLASS_PANE)
					.name(TranslationManager.string(Translations.GUI_CONFIRM_ITEM_NO_NAME))
					.lore(TranslationManager.list(Translations.GUI_CONFIRM_ITEM_NO_LORE))
					.make(), click -> this.onClick.accept(false));

		for (int i = 14; i <= 16; i++)
			setButton(i, QuickItem
					.of(CompMaterial.LIME_STAINED_GLASS_PANE)
					.name(TranslationManager.string(Translations.GUI_CONFIRM_ITEM_YES_NAME))
					.lore(TranslationManager.list(Translations.GUI_CONFIRM_ITEM_YES_LORE))
					.make(), click -> this.onClick.accept(true));

	}
}
