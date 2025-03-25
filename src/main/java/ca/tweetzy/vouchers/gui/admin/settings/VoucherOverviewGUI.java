package ca.tweetzy.vouchers.gui.admin.settings;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.profiles.builder.XSkull;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.gui.admin.VoucherListGUI;
import ca.tweetzy.vouchers.gui.admin.messages.VoucherMessageTypeGUI;
import ca.tweetzy.vouchers.gui.admin.rewards.VoucherRewardListGUI;
import ca.tweetzy.vouchers.model.VoucherHelper;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class VoucherOverviewGUI extends VouchersBaseGUI {

	private final Voucher voucher;

	public VoucherOverviewGUI(@NonNull Player player, @NonNull final Voucher voucher) {
		super(new VoucherListGUI(player), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Editing", 6);
		this.voucher = voucher;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void draw() {
		// border
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		// item/icon
		setButton(2, 2, QuickItem
				.of(this.voucher.getItem())
				.name("<GRADIENT:B3EBF2>&LVoucher Item</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change the voucher icon/item",
						"",
						"&e&lDrag N' Drop",
						"&7An item on this button to change icon",
						"",
						"&b&LOR",
						"",
						"&e&lRight Click",
						"&7To pick an item from a material picker"
				)
				.make(), click -> {


			final ItemStack cursor = click.cursor;
			if (cursor != null && cursor.getType() != CompMaterial.AIR.get()) {

				String item = cursor.getType() == CompMaterial.PLAYER_HEAD.get() ? VoucherHelper.getTextureUrlFromBase(XSkull.of(cursor).getProfileValue()) : null;
				if (item == null)
					item = "%s%s".formatted(cursor.getType().name(), cursor.getItemMeta() != null && cursor.getItemMeta().hasCustomModelData() ? ":" + cursor.getItemMeta().getCustomModelData() : "");

				this.voucher.setItem(item);
				voucher.sync(synchronizeResult -> draw());
			}
		});

		// OPTIONS
		setButton(2, 4, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:B3EBF2>&LSettings</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to adjust voucher settings",
						"&7You can also adjust the options in",
						"&7the voucher-files folder.",
						"",
						"&e&lLeft Click",
						"&7To adjust voucher settings"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, this.voucher)));

		setButton(2, 6, QuickItem
				.of(CompMaterial.PAPER)
				.name("<GRADIENT:B3EBF2>&LMessages</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to adjust voucher messages",
						"&7These messages are completely independent",
						"&7of the reward messages which are optional.",
						"",
						"&e&lLeft Click",
						"&7To &aadd&7/&eedit&7/&cremove &7voucher messages"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherMessageTypeGUI(click.player, this.voucher, this.voucher.getMessages(), false)));

		setButton(3, 3, QuickItem
				.of(CompMaterial.EXPERIENCE_BOTTLE)
				.name("<GRADIENT:B3EBF2>&LRewards</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to adjust voucher rewards",
						"&7There are two types of rewards you",
						"&7can add: &bItem &7rewards or &bcommand &7rewards.",
						"",
						"&e&lLeft Click",
						"&7To &aadd&7/&eedit&7/&cremove &7voucher rewards"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherRewardListGUI(click.player, this.voucher)));


		applyBackExit();
	}
}
