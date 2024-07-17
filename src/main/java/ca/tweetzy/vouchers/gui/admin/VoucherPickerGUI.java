package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.nbtapi.iface.ReadWriteItemNBT;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersPagedGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class VoucherPickerGUI extends VouchersPagedGUI<Voucher> {

	private final List<String> exclude;
	private final Consumer<String> selected;

	public VoucherPickerGUI(Gui parent, @NonNull Player player, List<String> exclude, final Consumer<String> selected) {
		super(parent, player, "&bVouchers &8> &7Category &8> &7Select Voucher", 6, new ArrayList<>());
		this.exclude =exclude;
		this.selected = selected;
		draw();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();
	}

	@Override
	protected void prePopulate() {
		this.items.addAll(Vouchers.getVoucherManager().getValues().stream().filter(voucher -> !exclude.contains(voucher.getId())).toList());
		this.items.sort(Comparator.comparing(Voucher::getId));
	}

	@Override
	protected ItemStack makeDisplayItem(Voucher voucher) {

		ItemStack item = QuickItem
				.of(voucher.getItem())
				.name(voucher.getName())
				.lore(voucher.getFilteredDescription())
				.lore(
						"",
						"&b&lClick &8» &7To select this voucher"
				)
				.make();


		NBT.modify(item, ReadWriteItemNBT::clearCustomNBT);
		return item;
	}

	@Override
	protected void onClick(Voucher voucher, GuiClickEvent click) {
		selected.accept(voucher.getId());
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
