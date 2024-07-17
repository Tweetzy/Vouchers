package ca.tweetzy.vouchers.gui.admin.category;

import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;

public final class GUICategoryList extends VouchersPagedGUI<Category> {

	public GUICategoryList(Gui parent, @NonNull Player player) {
		super(parent, player, "&bVouchers &8> &7Listing Categories", 6, new ArrayList<>());
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items.sort(Comparator.comparing(Category::getId));
	}

	@Override
	protected void drawFixed() {

	}

	@Override
	protected ItemStack makeDisplayItem(Category category) {
		return QuickItem
				.of(category.getItem())
				.name(category.getName())
				.lore(category.getDescription())
				.make();
	}

	@Override
	protected void onClick(Category category, GuiClickEvent click) {

	}
}
