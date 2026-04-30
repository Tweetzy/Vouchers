/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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

import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public final class VoucherConfirmationGUI extends VouchersBaseGUI {

	private final Consumer<Boolean> confirmed;

	public VoucherConfirmationGUI(@NonNull final Player player, @NonNull final Consumer<Boolean> confirmed) {
		super(null, player, TranslationManager.string(Translations.GUI_CONFIRM_TITLE), 3);
		setDefaultItem(QuickItem.bg(Settings.GUI_CONFIRM_BG.getString()));
		setGlobalClickDelay(200L);
		this.confirmed = confirmed;
		draw();
	}

	@Override
	protected void draw() {

		List.of(10, 11, 12).forEach(slot -> setButton(slot, QuickItem
				.of(Settings.GUI_CONFIRM_ITEMS_YES.getString())
				.name(TranslationManager.string(Translations.GUI_CONFIRM_ITEMS_YES_NAME))
				.lore(TranslationManager.list(Translations.GUI_CONFIRM_ITEMS_YES_LORE))
				.make(), click -> confirmed.accept(true)));

		List.of(14, 15, 16).forEach(slot -> setButton(slot, QuickItem
				.of(Settings.GUI_CONFIRM_ITEMS_NO.getString())
				.name(TranslationManager.string(Translations.GUI_CONFIRM_ITEMS_NO_NAME))
				.lore(TranslationManager.list(Translations.GUI_CONFIRM_ITEMS_NO_LORE))
				.make(), click -> confirmed.accept(false)));
	}
}
