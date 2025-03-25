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
