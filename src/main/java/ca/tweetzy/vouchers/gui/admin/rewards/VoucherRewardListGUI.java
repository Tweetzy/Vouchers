package ca.tweetzy.vouchers.gui.admin.rewards;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.gui.VoucherUpdatingPagedGUI;
import ca.tweetzy.vouchers.gui.admin.messages.VoucherMessageTypeGUI;
import ca.tweetzy.vouchers.gui.admin.settings.VoucherOverviewGUI;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.model.input.UserInput;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class VoucherRewardListGUI extends VoucherUpdatingPagedGUI<Reward> {

	private final Voucher voucher;

	public VoucherRewardListGUI(@NonNull Player player, @NonNull final Voucher voucher) {
		super(new VoucherOverviewGUI(player, voucher), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Edit Rewards", 6, 20, new ArrayList<>());
		this.voucher = voucher;
		setOnOpen(open -> startTask());
		applyClose();
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void prePopulate() {
		this.items = new ArrayList<>(Vouchers.getVoucherManger().get(this.voucher.getId()).getRewards());
	}

	@Override
	protected void drawFixed() {
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:77DD77>&lAdd Reward</GRADIENT:C1E1C1>")
				.lore(
						"&8Used to add a new reward",
						"&7You can add two different types of rewards",
						"&7either a &eItem &7or &eCommand &7reward.",
						"",
						"&e&lClick",
						"&7To create a new command reward",
						"",
						"&e&lDrag N' Drop",
						"&7An item onto this button to create an item reward"
				)
				.make(), click -> {

			final ItemStack cursor = click.cursor;
			if (cursor != null && cursor.getType() != CompMaterial.AIR.parseMaterial()) {
				this.voucher.getRewards().add(new ItemReward(cursor, 100, 0, new ArrayList<>()));
				saveAndReOpen(click.player, false);
				draw();
			} else {
				cancelTask();
				UserInput.get(click.player, "voucher", "command", result -> {
					this.voucher.getRewards().add(new CommandReward(ChatColor.stripColor(result), 100, 0, new ArrayList<>()));
					saveAndReOpen(click.player);
				}, null, () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), validate -> !validate.isEmpty());

			}
		});

		// new reward
		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(Reward reward) {

		QuickItem item = QuickItem.of(CompMaterial.PAPER);
		List<String> lore = new ArrayList<>();

		if (reward instanceof final ItemReward itemReward) {
			item = QuickItem.of(itemReward.getItem());
		}

		if (reward instanceof final CommandReward commandReward) {
			item.name("<GRADIENT:B3EBF2>&LCommand Reward</GRADIENT:AEC6CF>");
			lore.addAll(List.of(
					"&8This is a command reward.",
					"",
					"&b&LCommand&F:",
					"&f/&e%s".formatted(commandReward.getCommand())
			));
		}

		lore.addAll(List.of(
				"",
				"&7Chance&F: &a%s".formatted(reward.getChance()),
				"&7Delay&F: &e%s".formatted(TimeConverter.convertSecondsToHumanReadable(reward.getDelay())),
				"",
				"&7Total Messages&f: &b%s".formatted(reward.getMessages().size()),
				"",
				"&e&lLeft Click",
				"&7To adjust the reward chance",
				"",
				"&e&lRight Click",
				"&7To adjust the reward delay",
				"",
				"&e&lShift+Left Click",
				"&7To &aadd&7/&eedit&7/&cremove &7reward messages",
				"",
				"&e&lDrop Key",
				"&7To &cremove &7this reward from the voucher"
		));

		return item.lore(lore).make();
	}

	@Override
	protected void onClick(Reward reward, GuiClickEvent click) {
		switch (click.clickType) {
			case LEFT -> {
				cancelTask();
				UserInput.get(click.player, "reward edit", "enter chance in chat", result -> {
					reward.setChance(result < 0 ? 1 : result > 100 ? 100 : result);
					saveAndReOpen(click.player);
				}, (input) -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", input)), () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), MathUtil::isDouble, Double::parseDouble);
			}

			case RIGHT -> {
				cancelTask();
				UserInput.get(click.player, "reward edit", "enter delay in chat", result -> {
					reward.setDelay(result);
					saveAndReOpen(click.player);
				}, (input) -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", input)), () -> click.manager.showGUI(click.player, VoucherRewardListGUI.this), MathUtil::isInt, Integer::parseInt);
			}

			case SHIFT_LEFT -> {
				cancelTask();
				click.manager.showGUI(click.player, new VoucherMessageTypeGUI(click.player, this.voucher, reward.getMessages(), true));
			}
		}
	}

	private void saveAndReOpen(@NonNull final Player player) {
		saveAndReOpen(player, true);
	}

	private void saveAndReOpen(@NonNull final Player player, boolean open) {
		this.voucher.sync(result -> {
			if (result == SynchronizeResult.FAILURE)
				Common.tell(this.player, "&cSomething went wrong while saving the voucher.");

			if (open)
				Vouchers.getGuiManager().showGUI(player, new VoucherRewardListGUI(player, this.voucher));
		});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
