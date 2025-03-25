package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.template.MaterialPickerGUI;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ItemSelectorGUI extends MaterialPickerGUI {

	public ItemSelectorGUI(Gui parent, @NonNull BiConsumer<GuiClickEvent, ItemStack> selected) {
		super(
				parent,
				"<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Item Picker",
				null,
				"<GRADIENT:B3EBF2>&lMaterial Search</GRADIENT:AEC6CF>",
				"&eEnter search keywords in chat",
				selected);
		draw();
	}

	@Override
	protected ItemStack buildSearchButton() {
		return QuickItem
				.of(CompMaterial.DARK_OAK_SIGN)
				.name("<GRADIENT:B3EBF2>&lSearch</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to search materials",
						"",
						"&e&lClick",
						"&7To search for specific materials"
				)
				.make();
	}

	@Override
	protected ItemStack buildResetButton() {
		return QuickItem
				.of(CompMaterial.LAVA_BUCKET)
				.name("<GRADIENT:c4332b>&lReset Search</GRADIENT:f2837d>")
				.lore(
						"&8Used to reset search",
						"",
						"&e&lClick",
						"&7To reset the search results"
				)
				.make();
	}

	@Override
	protected ItemStack buildIcon(@NotNull ItemStack itemStack) {
		return QuickItem
				.of(itemStack)
				.name("&e%s".formatted(ChatUtil.capitalizeFully(itemStack.getType())))
				.lore()
				.make();
	}
}
