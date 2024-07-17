package ca.tweetzy.vouchers.gui.admin.category;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.gui.admin.GUIVouchersAdmin;
import ca.tweetzy.vouchers.impl.VoucherCategory;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public final class GUICategoryList extends VouchersPagedGUI<Category> {

	public GUICategoryList(Gui parent, @NonNull Player player) {
		super(parent, player, "&bVouchers &8> &7Listing Categories", 6, new ArrayList<>(Vouchers.getCategoryManager().getValues()));
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items.sort(Comparator.comparing(Category::getId));
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
				.name("&a&lNew Category")
				.lore("&b&lClick &8» &7To create new category")
				.make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lVouchers", "&fEnter an id for the category into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, new GUICategoryList(new GUIVouchersAdmin(click.player), click.player));
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.toLowerCase());
				if (Vouchers.getCategoryManager().find(string) != null) {
					Common.tell(click.player, TranslationManager.string(Translations.CATEGORY_EXISTS_ALREADY));

					return false;
				}

				final Category category = new VoucherCategory(string, "&e" + string, "&7Default category description", CompMaterial.CHEST.parseItem(), new HashSet<>());

				category.store(created -> {
					if (created != null) {
						click.manager.showGUI(click.player, new GUICategoryList(new GUIVouchersAdmin(click.player), click.player));
					}
				});

				return true;
			}
		});
	}

	@Override
	protected ItemStack makeDisplayItem(Category category) {
		return QuickItem
				.of(category.getItem())
				.name(category.getName())
				.lore("&7ID&f: &e" + category.getId())
				.lore(category.getDescription())
				.lore(
						"",
						"&b&lLeft Click &8» &7To edit category",
						"&C&lRight Click &8» &4To delete category"
				).make();
	}

	@Override
	protected void onClick(Category category, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT)
			click.manager.showGUI(click.player, new GUICategoryEdit(click.player, category));

		if (click.clickType == ClickType.RIGHT)
			category.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					click.manager.showGUI(click.player, new GUICategoryList(new GUIVouchersAdmin(click.player), click.player));
			});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
