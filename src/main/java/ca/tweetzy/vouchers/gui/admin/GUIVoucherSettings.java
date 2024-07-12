/*
 * Vouchers
 * Copyright 2022 Kiran Hart
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

package ca.tweetzy.vouchers.gui.admin;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.template.SoundPickerGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import ca.tweetzy.vouchers.gui.admin.message.GUIMessagesList;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public final class GUIVoucherSettings extends VouchersBaseGUI {

	private final Voucher voucher;

	public GUIVoucherSettings(@NonNull final Player player, @NonNull final Voucher voucher) {
		super(new GUIVoucherEdit(player, voucher), player, "&bVouchers &8> &7" + voucher.getId() + " &8> &7Options", 6);
		this.voucher = voucher;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void draw() {

		setButton(1, 1, QuickItem.of(CompMaterial.ANVIL).name("&b&lMax Uses").lore(
				"&8The maximum uses per player",
				"&7If you want users you be able to redeem",
				"&7this voucher as many times as they want",
				"&7set this to -1.",
				"",
				"&7Current&f: &b" + this.voucher.getOptions().getMaxUses(),
				"",
				"&b&lClick &8» &7To edit max uses"
		).make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&lVoucher Edit", "&fEnter new maximum uses") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIVoucherSettings.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.toLowerCase());

				if (!NumberUtils.isNumber(string)) {
					Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, string));
					return false;
				}

				final int rate = Integer.parseInt(string);

				GUIVoucherSettings.this.voucher.getOptions().setMaxUses(rate);
				syncReopen(click);
				return true;
			}
		});

		setButton(2, 2, QuickItem
				.of(CompMaterial.JUKEBOX)
				.name("&b&lVoucher Sound")
				.lore(
						"",
						"&7Current&f: " + ChatUtil.capitalizeFully(this.voucher.getOptions().getSound()),
						"",
						"&b&lClick &8» &7To change with picker"
				)
				.make(), click -> {

			if (click.clickType == ClickType.LEFT)
				click.manager.showGUI(click.player, new SoundPickerGUI(this, "&bVouchers &8> &7Select Sound", null, (event, selected) -> {
					this.voucher.getOptions().setSound(selected);
					this.voucher.sync(true);
					click.manager.showGUI(click.player, new GUIVoucherSettings(click.player, this.voucher));
				}));
		});

		setButton(2, 4, QuickItem
				.of(this.voucher.getOptions().isPlayingSound() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("&b&lPlay Sound")
				.lore(
						"",
						"&7Current&f: " + (this.voucher.getOptions().isPlayingSound() ? "&aTrue" : "&cFalse"),
						"",
						"&b&lClick &8» &7To toggle voucher sound"
				)
				.make(), click -> {
			this.voucher.getOptions().setPlayingSound(!this.voucher.getOptions().isPlayingSound());
			syncReopen(click);
		});

		setButton(1, 3, QuickItem.of(CompMaterial.PACKED_ICE).name("&b&lCooldown").lore(
				"&7In seconds, how long do you want the player",
				"&7to wait before they can redeem this voucher,",
				"&7after they already did. Set to -1 to disable.",
				"",
				"&7Current&f: &b" + this.voucher.getOptions().getCooldown(),
				"",
				"&b&lClick &8» &7To edit use cooldown"
		).make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&lVoucher Edit", "&fEnter new cooldown") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIVoucherSettings.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.toLowerCase());

				if (!NumberUtils.isNumber(string)) {
					Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, string));
					return false;
				}

				final int rate = Integer.parseInt(string);

				GUIVoucherSettings.this.voucher.getOptions().setCooldown(rate);
				syncReopen(click);
				return true;
			}
		});

		setButton(1, 5, QuickItem.of(this.voucher.getOptions().isGlowing() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("&b&lGlowing").lore(
						"&7Determines whether the voucher will have a glow",
						"&7effect applied (like an enchanted item)",
						"",
						"&7Current&f: " + (this.voucher.getOptions().isGlowing() ? "&aTrue" : "&cFalse"),
						"",
						"&b&lClick &8» &7To toggle glow"
				).make(), click -> {
			this.voucher.getOptions().setGlowing(!this.voucher.getOptions().isGlowing());
			syncReopen(click);
		});

		setButton(1, 7, QuickItem.of(this.voucher.getOptions().isAskConfirm() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("&b&lAsk Confirmation").lore(
						"&7If true, vouchers will ask the player to confirm",
						"&7before the voucher is redeemed.",
						"",
						"&7Current&f: " + (this.voucher.getOptions().isAskConfirm() ? "&aTrue" : "&cFalse"),
						"",
						"&b&lClick &8» &7To toggle redeem confirmation"
				).make(), click -> {
			this.voucher.getOptions().setAskConfirm(!this.voucher.getOptions().isAskConfirm());
			syncReopen(click);
		});

		setButton(3, 1, QuickItem.of(this.voucher.getOptions().isRemoveOnUse() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("&b&lRemove on Use").lore(
						"&7If true, vouchers will take the voucher",
						"&7from the player after it's redeemed.",
						"",
						"&7Current&f: " + (this.voucher.getOptions().isRemoveOnUse() ? "&aTrue" : "&cFalse"),
						"",
						"&b&lClick &8» &7To toggle remove on use"
				).make(), click -> {
			this.voucher.getOptions().setRemoveOnUse(!this.voucher.getOptions().isRemoveOnUse());
			syncReopen(click);
		});

		setButton(3, 3, QuickItem.of(this.voucher.getOptions().isRequiresPermission() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("&b&lRequires Permission").lore(
						"&7If true, vouchers will check if the player",
						"&7has permission to redeem the voucher.",
						"",
						"&7Current&f: " + (this.voucher.getOptions().isRequiresPermission() ? "&aTrue" : "&cFalse"),
						"",
						"&b&lClick &8» &7To toggle require permission"
				).make(), click -> {
			this.voucher.getOptions().setRequiresPermission(!this.voucher.getOptions().isRequiresPermission());
			syncReopen(click);
		});

		setButton(3, 5, QuickItem.of(CompMaterial.PAPER).name("&b&lPermission").lore(
				"&7The permission required to use the voucher",
				"",
				"&7Current&f: &b" + this.voucher.getOptions().getPermission(),
				"",
				"&b&lClick &8» &7To edit permission"
		).make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&B&lVoucher Edit", "&fEnter new voucher permission") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIVoucherSettings.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string.toLowerCase());

				GUIVoucherSettings.this.voucher.getOptions().setPermission(string);
				syncReopen(click);
				return true;
			}
		});

		setButton(3, 7, QuickItem.of(CompMaterial.BOOK).name("&b&lMessages").lore(
				"&7You can add/edit/remove the following here:",
				"",
				"&7- Titles",
				"&7- Subtitles",
				"&7- Action Bars",
				"&7- Chat Messages",
				"&7- Broadcast Messages",
				"",
				"&b&lClick &8» &7To edit messages"
		).make(), click -> click.manager.showGUI(click.player, new GUIMessagesList(click.player, this.voucher)));

		applyBackExit();
	}

	private void syncReopen(@NonNull final GuiClickEvent event) {
		this.voucher.sync(true);
		event.manager.showGUI(event.player, new GUIVoucherSettings(event.player, this.voucher));
	}
}
