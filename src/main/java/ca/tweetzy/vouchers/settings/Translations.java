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

package ca.tweetzy.vouchers.settings;

import ca.tweetzy.flight.settings.TranslationEntry;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.vouchers.Vouchers;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Translations extends TranslationManager {

	public Translations(@NonNull JavaPlugin plugin) {
		super(plugin);
		this.mainLanguage = Settings.LANGUAGE.getString();
	}

	public static final TranslationEntry VOUCHER_EXISTS_ALREADY = create("error.voucher already exists", "&cA voucher with that id already exists");
	public static final TranslationEntry CATEGORY_EXISTS_ALREADY = create("error.category already exists", "&cA category with that id already exists");
	public static final TranslationEntry VOUCHER_NOT_FOUND = create("error.voucher not found", "&cCannot find a voucher with the id: &4%voucher_id%");
	public static final TranslationEntry REDEEM_LIMIT_REACHED = create("error.redeem limit reached", "&cYou cannot redeem that voucher anymore!");
	public static final TranslationEntry REDEEM_HISTORY_CLEARED = create("error.redeem history cleared", "&aSuccessfully cleared the redeem history of specified player(s)");
	public static final TranslationEntry NOT_ALLOWED_TO_USE = create("error.not allowed to use", "&cYou are not allowed to use that voucher");
	public static final TranslationEntry WAIT_FOR_COOLDOWN = create("error.voucher cooldown", "&cYou can redeem that voucher in &4%cooldown_time%s");
	public static final TranslationEntry CATEGORY_CONTAINS_VOUCHER = create("error.category contains voucher", "&CThat category already contains the voucher");

	public static final TranslationEntry DROP_NEAR_PLAYER = create("info.give.dropped near player", "Voucher was dropped near player: %player_name% (full inventory)");
	public static final TranslationEntry GIVEN_TO_PLAYER = create("info.give.placed into inventory", "Voucher was placed in player: %player_name%'s inventory");

	public static TranslationEntry GUI_SHARED_ITEMS_BACK_BUTTON_NAME = create("gui.shared buttons.back button.name", "<GRADIENT:B3EBF2>&LGo Back</GRADIENT:AEC6CF>");
	public static TranslationEntry GUI_SHARED_ITEMS_BACK_BUTTON_LORE = create("gui.shared buttons.back button.lore",
			"&e&l%left_click% &7to go back"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_EXIT_BUTTON_NAME = create("gui.shared buttons.exit button.name", "<GRADIENT:B3EBF2>&LExit</GRADIENT:AEC6CF>");
	public static TranslationEntry GUI_SHARED_ITEMS_EXIT_BUTTON_LORE = create("gui.shared buttons.exit button.lore",
			"&e&l%left_click% &7to exit menu"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON_NAME = create("gui.shared buttons.previous button.name", "<GRADIENT:B3EBF2>&lPrevious Page</GRADIENT:AEC6CF>");
	public static TranslationEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON_LORE = create("gui.shared buttons.previous button.lore",
			"&e&l%left_click% &7to go back a page"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_NEXT_BUTTON_NAME = create("gui.shared buttons.next button.name", "<GRADIENT:B3EBF2>&lNext Page</GRADIENT:AEC6CF>");
	public static TranslationEntry GUI_SHARED_ITEMS_NEXT_BUTTON_LORE = create("gui.shared buttons.next button.lore",
			"&e&l%left_click% &7to go to next page"
	);

	public static final TranslationEntry GUI_ADMIN_VOUCHER_LIST_TITLE = create("gui.admin menus.voucher list.title", "%pl_name% &8> &7Listing Vouchers");
	public static final TranslationEntry GUI_CONFIRM_TITLE = create("gui.user menus.confirm.title", "%pl_name% &8> &7Confirm Redeem?");
	public static final TranslationEntry GUI_CONFIRM_ITEMS_YES_NAME = create("gui.user menus.confirm.items.yes.name", "<GRADIENT:77DD77>&lConfirm</GRADIENT:C1E1C1>");
	public static final TranslationEntry GUI_CONFIRM_ITEMS_YES_LORE = create("gui.user menus.confirm.items.yes.lore", "&7Confirms the voucher redeem");

	public static final TranslationEntry GUI_CONFIRM_ITEMS_NO_NAME = create("gui.user menus.confirm.items.no.name", "<GRADIENT:c4332b>&lCancel</GRADIENT:f2837d>");
	public static final TranslationEntry GUI_CONFIRM_ITEMS_NO_LORE = create("gui.user menus.confirm.items.no.lore", "&7Confirms the voucher redeem");

	public static void init() {
		new Translations(Vouchers.getInstance()).setup(Vouchers.getInstance());
	}
}
