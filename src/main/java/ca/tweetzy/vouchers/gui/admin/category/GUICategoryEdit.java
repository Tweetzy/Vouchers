package ca.tweetzy.vouchers.gui.admin.category;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.gui.template.MaterialPickerGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Category;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import ca.tweetzy.vouchers.gui.admin.GUIVoucherEdit;
import ca.tweetzy.vouchers.gui.admin.GUIVouchersAdmin;
import ca.tweetzy.vouchers.gui.admin.VoucherPickerGUI;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public final class GUICategoryEdit extends VouchersPagedGUI<String> {

	private final Category category;

	public GUICategoryEdit(@NonNull Player player, @NonNull final Category category) {
		super(new GUICategoryList(new GUIVouchersAdmin(player), player), player, "&bVouchers &8> &7Category &8> &7" + category.getId(), 6, new ArrayList<>(category.getVoucherIds()));
		this.category = category;
		draw();
	}

	@Override
	protected ItemStack makeDisplayItem(String voucherId) {
		final Voucher voucher = Vouchers.getVoucherManager().find(voucherId);
		return QuickItem
				.of(voucher.getItem())
				.name(voucher.getName())
				.lore(voucher.getFilteredDescription())
				.lore(
						"",
						"&C&lRight Click &8» &4To delete from category"
				)
				.make();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setButton(5, 4, QuickItem.of(CompMaterial.SLIME_BALL)
				.name("&a&lAdd Voucher")
				.lore("&b&lClick &8» &7To changey")
				.make(), click -> click.manager.showGUI(click.player, new VoucherPickerGUI(this, click.player, new ArrayList<>(this.category.getVoucherIds()), selected -> {

			if (this.category.getVoucherIds().contains(selected)) {
				Common.tell(click.player, TranslationManager.string(Translations.CATEGORY_CONTAINS_VOUCHER));
				return;
			}

			this.category.getVoucherIds().add(selected);
			this.category.sync(result -> {

				click.manager.showGUI(click.player, new GUICategoryEdit(click.player, this.category));
			});

		})));

		setButton(5, 7, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("&b&lCategory Name")
				.lore(
						"",
						"&7Current&f: " + this.category.getName(),
						"",
						"&b&lClick &8» &7To change display name"
				)
				.make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&LCategory Edit", "&fEnter new name into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUICategoryEdit.this);
			}

			@Override
			public boolean onResult(String string) {
				if (string.isEmpty()) return false;
				GUICategoryEdit.this.category.setName(string);
				GUICategoryEdit.this.category.sync(res -> {
					click.manager.showGUI(click.player, new GUICategoryEdit(click.player, GUICategoryEdit.this.category));
				});

				return true;
			}
		});

		setButton(5, 8, QuickItem
				.of(CompMaterial.BOOK)
				.name("&b&lCategory Description")
				.lore(
						"",
						"&7Current&f: ",
						this.category.getDescription(),
						"",
						"&b&lClick &8» &7To change description"
				)
				.make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&LCategory Edit", "&fEnter new description into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUICategoryEdit.this);
			}

			@Override
			public boolean onResult(String string) {
				if (string.isEmpty()) return false;
				GUICategoryEdit.this.category.setDescription(string);
				GUICategoryEdit.this.category.sync(res -> {
					click.manager.showGUI(click.player, new GUICategoryEdit(click.player, GUICategoryEdit.this.category));
				});

				return true;
			}
		});

		setButton(5, 6, QuickItem
				.of(this.category.getItem())
				.name("&b&lCategory Icon")
				.lore(
						"",
						"&7Current&f: " + ChatUtil.capitalizeFully(this.category.getItem().getType()),
						"",
						"&b&lLeft Click &8» &7To change with picker",
						"&b&lRight Click &8» &7To select held item"
				)
				.make(), click -> {

			if (click.clickType == ClickType.LEFT)
				click.manager.showGUI(click.player, new MaterialPickerGUI(this, "&bVouchers &8> &7Select Material", null, (event, selected) -> {
					this.category.setItem(selected);
					this.category.sync(null);
					click.manager.showGUI(click.player, new GUICategoryEdit(click.player, this.category));
				}));

			if (click.clickType == ClickType.RIGHT) {
				final ItemStack cursor = click.cursor;
				if (cursor == null || cursor.getType() == CompMaterial.AIR.parseMaterial()) return;

				this.category.setItem(cursor.clone());
				this.category.sync(null);
				click.manager.showGUI(click.player, new GUICategoryEdit(click.player, this.category));
			}
		});
	}

	@Override
	protected void onClick(String voucherId, GuiClickEvent click) {
		if (click.clickType == ClickType.RIGHT) {
			this.category.getVoucherIds().remove(voucherId);
			this.category.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					click.manager.showGUI(click.player, new GUICategoryEdit(click.player, this.category));
			});
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
