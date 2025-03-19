package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import ca.tweetzy.vouchers.impl.StandardVoucher;
import ca.tweetzy.vouchers.model.input.UserInput;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherListGUI extends VoucherUpdatingPagedGUI<Voucher> {

	public VoucherListGUI(@NonNull final Player player) {
		super(new VouchersAdminGUI(player), player, TranslationManager.string(Translations.GUI_ADMIN_VOUCHER_LIST_TITLE), 6, 20, new ArrayList<>());

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
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.LIME_DYE).make(), click -> {
			cancelTask();

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Creation</GRADIENT:AEC6CF>", "&eEnter id for voucher in chat", result -> {
				StandardVoucher.empty(result).store(stored -> {
					if (stored != null) {
						Vouchers.getVoucherManger().add(result, stored);
						click.manager.showGUI(click.player, new VoucherListGUI(click.player));
					}

				});
			}, fail -> {
				if (Vouchers.getVoucherManger().doesVoucherWithIdExists(fail)) {
					Common.tell(click.player, TranslationManager.string(Translations.VOUCHER_EXISTS_ALREADY));
				}
			}, () -> click.manager.showGUI(click.player, VoucherListGUI.this), validate -> !validate.isEmpty(), transform -> ChatColor.stripColor(transform).replaceAll("\\s", ""));
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
