package ca.tweetzy.vouchers.gui.admin.settings;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VoucherDescriptionGUI extends VoucherUpdatingPagedGUI<String> {

	private Voucher voucher;
	private String lastClickedDescription;

	public VoucherDescriptionGUI(@NonNull final Player player, @NonNull final Voucher voucher) {
		super(new VoucherSettingsGUI(player, voucher), player, "<GRADIENT:fc67fa>&lVouchers</GRADIENT:f4c4f3> &8» &7Edit Description", 6, 20, new ArrayList<>());
		this.voucher = voucher;

		setOnOpen(open -> startTask());
		applyClose();
		draw();
	}

	@Override
	protected void prePopulate() {
		this.voucher = Vouchers.getVoucherManger().get(this.voucher.getId());
		this.items = this.voucher.getDescription();
	}

	@Override
	protected void drawFixed() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.PINK_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:fc67fa>&LNew Line</GRADIENT:f4c4f3>")
				.lore(
						"&8Used to add a new line",
						"",
						"&e&lClick",
						"&7To insert new description line"
				)
				.make(), click -> {

			click.gui.close();
			new TitleInput(Vouchers.getInstance(), click.player, "voucher settings", "enter desc") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, VoucherDescriptionGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					voucher.getDescription().add(string);
					saveVoucher();
					click.manager.showGUI(click.player, new VoucherDescriptionGUI(click.player, voucher));
					return true;
				}
			};
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(String description) {
		return QuickItem
				.of(CompMaterial.PAPER)
				.name(description)
				.lore(
						"&8Use the following options to adjust",
						"",
						"&e&lLeft Click",
						"&7To select/swap with another selected line.",
						"",
						"&e&lDrop Key",
						"&7To &cremove &7this line from the description"
				)
				.make();
	}

	@Override
	protected void onClick(String description, GuiClickEvent clickEvent) {
		if (clickEvent.clickType == ClickType.LEFT) {
			handleSwap(description);
			saveVoucher();
		} else if (clickEvent.clickType == ClickType.DROP) {
			// Handle drop click to remove description
			this.voucher.getDescription().remove(description);
			saveVoucher();
			draw();
		}
	}

	private void saveVoucher() {
		this.voucher.sync(result -> {
			if (result == SynchronizeResult.FAILURE)
				Common.tell(this.player, "&cSomething went wrong while saving the voucher.");
		});
	}

	private void handleSwap(String description) {
		if (lastClickedDescription != null) {
			// Swap logic here
			int index1 = items.indexOf(lastClickedDescription);
			int index2 = items.indexOf(description);
			if (index1 != -1 && index2 != -1) {
				String temp = items.get(index1);
				items.set(index1, items.get(index2));
				items.set(index2, temp);
				draw(); // Redraw the GUI
			}
			lastClickedDescription = null; // Reset
		} else {
			lastClickedDescription = description; // Store the first clicked description
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
