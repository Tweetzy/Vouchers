/*
 * Vouchers
 * Copyright 2025 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.gui.admin.settings;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.gui.template.SoundPickerGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.model.TimeConverter;
import ca.tweetzy.vouchers.model.input.UserInput;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public final class VoucherSettingsGUI extends VouchersBaseGUI {

	private final Voucher voucher;

	public VoucherSettingsGUI(@NonNull Player player, @NonNull final Voucher voucher) {
		super(new VoucherOverviewGUI(player, voucher), player, "<GRADIENT:B3EBF2>&lVouchers</GRADIENT:AEC6CF> &8» &7Voucher Settings", 6);
		this.voucher = voucher;
		draw();
	}

	@Override
	protected void draw() {
		InventoryBorder.getBorders(6).forEach(slot -> setItem(slot, QuickItem.bg(
				QuickItem.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).glow(true).make()
		)));

		// glow
		drawGlowButton();
		// dp name
		drawNameButton();
		// desc
		drawDescButton();
		// permission
		drawPermissionButton();
		// sounds
		drawSoundsButton();
		// remove on use
		drawRemoveOnUseButton();
		// ask for confirm
		drawAskForConfirm();
		// max uses
		drawMaxUses();
		// cooldown
		drawCooldown();

		applyBackExit();
	}

	private void drawGlowButton() {
		setButton(2, 2, QuickItem
				.of(this.voucher.getSettings().useGlow() ? CompMaterial.GLOW_ITEM_FRAME : CompMaterial.ITEM_FRAME)
				.name("<GRADIENT:B3EBF2>&lVoucher Glow</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to toggle voucher glow",
						"&7(!) Some items cannot have a glow.",
						"",
						"&7Use Glow&f: %s".formatted(this.voucher.getSettings().useGlow() ? "&aTrue" : "&cFalse"),
						"",
						"&e&lClick",
						"&7To toggle the voucher glow"
				)
				.make(), click -> {

			final boolean lastValue = this.voucher.getSettings().useGlow();
			this.voucher.getSettings().setUseGlow(!lastValue);
			this.voucher.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					drawGlowButton();
				else
					this.voucher.getSettings().setUseGlow(lastValue);
			});
		});
	}

	private void drawNameButton() {
		setButton(2, 3, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:B3EBF2>&lVoucher Display Name</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change voucher display name",
						"&7This is just the actual name",
						"&7that is shown on the item.",
						"",
						"&7Current Name&f: %s".formatted(this.voucher.getDisplayName()),
						"",
						"&e&lClick",
						"&7To change voucher display name"
				)
				.make(), click -> {

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Edit</GRADIENT:AEC6CF>", "&eEnter name in chat", result -> {
				final String lastValue = voucher.getDisplayName();
				voucher.setDisplayName(result);

				voucher.sync(saveStatus -> {
					if (saveStatus == SynchronizeResult.SUCCESS)
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					else {
						voucher.setDisplayName(lastValue);
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					}
				});
			}, null, () -> click.manager.showGUI(click.player, VoucherSettingsGUI.this), validate -> !validate.isEmpty());

		});
	}

	private void drawDescButton() {
		setButton(2, 4, QuickItem
				.of(CompMaterial.WRITABLE_BOOK)
				.name("<GRADIENT:B3EBF2>&lVoucher Description</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change voucher description",
						"&7This is just the lore that is applied",
						"&7to the physical voucher item.",
						"",
						"&7Current Description&f:"

				)
				.lore(this.voucher.getDescription())
				.lore(
						"",
						"&e&lLeft Click",
						"&7To &aadd&7/&eedit&7/&cremove &7description lines"
				)
				.make(), click -> click.manager.showGUI(click.player, new VoucherDescriptionGUI(click.player, this.voucher)));
	}

	private void drawPermissionButton() {
		setButton(2, 5, QuickItem
				.of(CompMaterial.PAPER)
				.name("<GRADIENT:B3EBF2>&lVoucher Permissions</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change/enable permissions",
						"",
						"&7Use Permission&f: %s".formatted(this.voucher.getSettings().usePermission() ? "&aTrue" : "&cFalse"),
						"&7Permission&f: &b%s".formatted(this.voucher.getSettings().getPermission()),
						"",
						"&e&lLeft Click",
						"&7To change the voucher permission",
						"",
						"&e&lRight Click",
						"&7To toggle permission requirement"
				)
				.make(), click -> {

			if (click.clickType == ClickType.RIGHT) {
				final boolean lastValueUsePerm = this.voucher.getSettings().usePermission();
				this.voucher.getSettings().setUsePermission(!lastValueUsePerm);


				this.voucher.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						drawPermissionButton();
					else
						this.voucher.getSettings().setUseGlow(lastValueUsePerm);
				});

				return;
			}

			click.gui.close();

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Edit</GRADIENT:AEC6CF>", "&eEnter permission into chat", result -> {
				final String lastValuePerm = voucher.getSettings().getPermission();
				voucher.getSettings().setPermission(result);

				voucher.sync(saveStatus -> {
					if (saveStatus == SynchronizeResult.SUCCESS)
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					else {
						voucher.getSettings().setPermission(lastValuePerm);
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					}
				});

			}, null, () -> click.manager.showGUI(click.player, VoucherSettingsGUI.this), validate -> !validate.isEmpty(), transform -> ChatColor.stripColor(transform.toLowerCase()).replaceAll("\\s", ""));
		});
	}

	private void drawSoundsButton() {
		setButton(2, 6, QuickItem
				.of(CompMaterial.JUKEBOX)
				.name("<GRADIENT:B3EBF2>&lVoucher Sounds</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change/enable sounds",
						"",
						"&7Use Sound&f: %s".formatted(this.voucher.getSettings().useSound() ? "&aTrue" : "&cFalse"),
						"&7Sound&f: &b%s".formatted(ChatUtil.capitalizeFully(this.voucher.getSettings().getSound().friendlyName().replace(".", " "))),
						"",
						"&e&lLeft Click",
						"&7To change the voucher sound",
						"",
						"&e&lRight Click",
						"&7To toggle voucher use sound"
				)
				.make(), click -> {

			if (click.clickType == ClickType.RIGHT) {
				final boolean lastValue = this.voucher.getSettings().useSound();
				this.voucher.getSettings().setUseSound(!lastValue);


				this.voucher.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						drawSoundsButton();
					else
						this.voucher.getSettings().setUseSound(lastValue);
				});

				return;
			}

			CompSound lastSound = this.voucher.getSettings().getSound();
			click.manager.showGUI(click.player, new SoundPickerGUI(this, Common.colorize("<GRADIENT:B3EBF2>&LVouchers/GRADIENT:B3EBF2> &8» &7Sound Picker"), null, (unused, soundClicked) -> {
				this.voucher.getSettings().setSound(soundClicked);

				this.voucher.sync(result -> {
					if (result != SynchronizeResult.SUCCESS)
						this.voucher.getSettings().setSound(lastSound);

					click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, this.voucher));
				});

			}));
		});
	}

	private void drawRemoveOnUseButton() {
		setButton(3, 2, QuickItem
				.of(this.voucher.getSettings().isRemoveOnUse() ? CompMaterial.HOPPER : CompMaterial.HOPPER_MINECART)
				.name("<GRADIENT:B3EBF2>&lRemove On Use</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to toggle remove on use",
						"&7This just means the voucher is taken from",
						"&7the player once its redeemed",
						"",
						"&7Remove On Use&f: %s".formatted(this.voucher.getSettings().isRemoveOnUse() ? "&aTrue" : "&cFalse"),
						"",
						"&e&lClick",
						"&7To toggle the removal on use"
				)
				.make(), click -> {

			final boolean lastValue = this.voucher.getSettings().isRemoveOnUse();
			this.voucher.getSettings().setRemoveOnUse(!lastValue);
			this.voucher.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					drawRemoveOnUseButton();
				else
					this.voucher.getSettings().setRemoveOnUse(lastValue);
			});
		});
	}

	private void drawAskForConfirm() {
		setButton(3, 3, QuickItem
				.of(this.voucher.getSettings().isAskForConfirm() ? CompMaterial.ENDER_PEARL : CompMaterial.ENDER_EYE)
				.name("<GRADIENT:B3EBF2>&lAsk for Confirm</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to toggle ask for confirm",
						"&7If enabled, players will need to confirm",
						"&7whether they meant to redeem the voucher.",
						"",
						"&7Redeem Confirmation&f: %s".formatted(this.voucher.getSettings().isAskForConfirm() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick",
						"&7To toggle the redeem confirmation"
				)
				.make(), click -> {

			final boolean lastValue = this.voucher.getSettings().isAskForConfirm();
			this.voucher.getSettings().setAskForConfirmation(!lastValue);
			this.voucher.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					drawAskForConfirm();
				else
					this.voucher.getSettings().setAskForConfirmation(lastValue);
			});
		});
	}

	private void drawMaxUses() {
		setButton(3, 5, QuickItem
				.of(CompMaterial.ANVIL)
				.name("<GRADIENT:B3EBF2>&lMax Uses</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to limit max uses",
						"&7This is the hard limit on how many times",
						"&7a player can use this specific voucher.",
						"",
						"&7(!) You can use -1/0 to disable the use limit.",
						"",
						"&7Max Uses&f: %s".formatted(this.voucher.getSettings().getMaximumUses() <= 0 ? "&cDisabled &7(&e%s&7)".formatted(this.voucher.getSettings().getMaximumUses()) : "&a%s".formatted(this.voucher.getSettings().getMaximumUses())),
						"",
						"&e&lClick",
						"&7To change maximum voucher uses"
				)
				.make(), click -> {

			click.gui.close();

			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Edit</GRADIENT:AEC6CF>", "&eEnter max uses in chat", newMaxUses -> {
				final int lastValue = voucher.getSettings().getMaximumUses();

				voucher.getSettings().setMaximumUses(newMaxUses);
				voucher.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						drawMaxUses();
					else
						voucher.getSettings().setMaximumUses(lastValue);

					click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
				});

			}, fail -> Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", fail)), () -> click.manager.showGUI(click.player, VoucherSettingsGUI.this), MathUtil::isInt, Integer::parseInt);
		});
	}

	private void drawCooldown() {
		setButton(3, 6, QuickItem
				.of(CompMaterial.SNOWBALL)
				.name("<GRADIENT:B3EBF2>&lVoucher Cooldown</GRADIENT:AEC6CF>")
				.lore(
						"&8Used to change/enable cooldowns",
						"",
						"&7Use Cooldown&f: %s".formatted(this.voucher.getSettings().useCooldown() ? "&aTrue" : "&cFalse"),
						"&7Cooldown&f: &b%s".formatted(TimeConverter.convertSecondsToHumanReadable(this.voucher.getSettings().getCooldown())),
						"",
						"&e&lLeft Click",
						"&7To change the voucher cooldown",
						"",
						"&e&lRight Click",
						"&7To toggle cooldown usage"
				)
				.make(), click -> {

			if (click.clickType == ClickType.RIGHT) {
				final boolean lastValueUsePerm = this.voucher.getSettings().useCooldown();
				this.voucher.getSettings().setUseCooldown(!lastValueUsePerm);

				this.voucher.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						drawCooldown();
					else
						this.voucher.getSettings().setUseCooldown(lastValueUsePerm);
				});

				return;
			}

			click.gui.close();


			UserInput.get(click.player, "<GRADIENT:B3EBF2>&lVoucher Edit</GRADIENT:AEC6CF>", "&eEnter cooldown in chat (ex. 1 second)", input -> {
				final long lastValue = voucher.getSettings().getCooldown();
				voucher.getSettings().setCooldown(TimeConverter.convertHumanReadableTime(input) / 1000);

				voucher.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					else {
						voucher.getSettings().setCooldown(lastValue);
						click.manager.showGUI(click.player, new VoucherSettingsGUI(click.player, voucher));
					}
				});

			}, null, () -> click.manager.showGUI(click.player, VoucherSettingsGUI.this), validate -> !validate.isEmpty(), transform -> ChatColor.stripColor(transform.toLowerCase()).replaceAll("\\s", ""));
		});
	}
}
