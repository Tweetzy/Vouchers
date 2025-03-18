package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import ca.tweetzy.vouchers.impl.StandardVoucher;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherListGUI extends VoucherUpdatingPagedGUI<Voucher> {

	public VoucherListGUI(@NonNull final Player player) {
		super(new VouchersAdminGUI(player), player, TranslationManager.string(Translations.GUI_CONFIRM_TITLE), 6, 20, new ArrayList<>());

		setOnOpen(open -> startTask());
		applyClose();
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items = new ArrayList<>(Vouchers.getVoucherManger().getValues());
	}

	@Override
	protected void drawFixed() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.PINK_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.LIME_DYE).make(), click -> {
			cancelTask();
			new TitleInput(Vouchers.getInstance(), click.player, "&eVoucher Creation", "&7Enter voucher id") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, new VoucherListGUI(click.player));
				}

				@Override
				public boolean onResult(String string) {
					final String formattedId = ChatColor.stripColor(string).toLowerCase();

					if (Vouchers.getVoucherManger().doesVoucherWithIdExists(formattedId)) return false;
					StandardVoucher.empty(formattedId).store(stored -> {
						if (stored != null) {
							Vouchers.getVoucherManger().add(formattedId, stored);
							click.manager.showGUI(click.player, new VoucherListGUI(click.player));
						}

					});

					return true;
				}
			};
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(Voucher voucher) {
		return QuickItem
				.of(voucher.getItem())
				.name(voucher.getDisplayName())
				.lore(voucher.getDescription())
				.glow(voucher.getSettings().useGlow())
				.make();
	}

	@Override
	protected void onClick(Voucher voucher, GuiClickEvent click) {
		cancelTask();
		click.manager.showGUI(click.player, new VoucherOverviewGUI(click.player, voucher));
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
