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

package ca.tweetzy.vouchers.gui;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.template.BaseGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.MessageType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.abstraction.VouchersBaseGUI;
import ca.tweetzy.vouchers.impl.VoucherMessage;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class GUIMessageType extends VouchersBaseGUI {

	private final Voucher voucher;

	public GUIMessageType(@NonNull final Voucher voucher) {
		super(new GUIMessagesList(voucher), "&bVouchers &8> &7" + voucher.getId() + " &8> &7Msg Type", 4);
		this.voucher = voucher;
		draw();
	}

	@Override
	protected void draw() {

		if (this.voucher.getOptions().getMessages().stream().anyMatch(msg -> msg.getMessageType() == MessageType.TITLE)) {
			setItem(1, 2, QuickItem.of(CompMaterial.PAPER).name("&B&lTitle").lore("&cYou already have a title message created", "&cdelete the existing one to make another.").make());
		} else {
			setButton(1, 2, QuickItem.of(CompMaterial.PAPER).name("&B&lTitle").lore("", "&b&lClick &8» &7To create title msg").make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Title", "&fEnter title into chat") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUIMessageType.this);
				}

				@Override
				public boolean onResult(String inputTitle) {
					new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Title", "&fEnter fade in and fade out duration") {

						@Override
						public void onExit(Player player) {
							click.manager.showGUI(click.player, GUIMessageType.this);
						}

						@Override
						public boolean onResult(String fadeInOut) {
							fadeInOut = ChatColor.stripColor(fadeInOut.toLowerCase());

							if (!NumberUtils.isNumber(fadeInOut)) {
								Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, fadeInOut));
								return false;
							}

							final int fadeInOutRate = Integer.parseInt(fadeInOut);

							new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Title", "&fEnter stay duration") {

								@Override
								public void onExit(Player player) {
									click.manager.showGUI(click.player, GUIMessageType.this);
								}

								@Override
								public boolean onResult(String stay) {
									stay = ChatColor.stripColor(stay.toLowerCase());

									if (!NumberUtils.isNumber(stay)) {
										Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, stay));
										return false;
									}

									final int stayRate = Integer.parseInt(stay);

									GUIMessageType.this.voucher.getOptions().getMessages().add(new VoucherMessage(MessageType.TITLE, inputTitle, fadeInOutRate, stayRate, fadeInOutRate));
									GUIMessageType.this.voucher.sync(true);

									click.manager.showGUI(click.player, new GUIMessagesList(GUIMessageType.this.voucher));
									return true;
								}
							};

							return true;
						}
					};
					return true;
				}
			});
		}

		if (this.voucher.getOptions().getMessages().stream().anyMatch(msg -> msg.getMessageType() == MessageType.SUBTITLE)) {
			setItem(1, 3, QuickItem.of(CompMaterial.MAP).name("&B&lSubtitle").lore("&cYou already have a subtitle message created", "&cdelete the existing one to make another.").make());
		} else {
			setButton(1, 3, QuickItem.of(CompMaterial.MAP).name("&B&lSubtitle").lore("", "&b&lClick &8» &7To create subtitle msg").make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Subtitle", "&fEnter subtitle into chat") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUIMessageType.this);
				}

				@Override
				public boolean onResult(String inputTitle) {
					new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Subtitle", "&fEnter fade in and fade out duration") {

						@Override
						public void onExit(Player player) {
							click.manager.showGUI(click.player, GUIMessageType.this);
						}

						@Override
						public boolean onResult(String fadeInOut) {
							fadeInOut = ChatColor.stripColor(fadeInOut.toLowerCase());

							if (!NumberUtils.isNumber(fadeInOut)) {
								Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, fadeInOut));
								return false;
							}

							final int fadeInOutRate = Integer.parseInt(fadeInOut);

							new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Subtitle", "&fEnter stay duration") {

								@Override
								public void onExit(Player player) {
									click.manager.showGUI(click.player, GUIMessageType.this);
								}

								@Override
								public boolean onResult(String stay) {
									stay = ChatColor.stripColor(stay.toLowerCase());

									if (!NumberUtils.isNumber(stay)) {
										Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, stay));
										return false;
									}

									final int stayRate = Integer.parseInt(stay);

									GUIMessageType.this.voucher.getOptions().getMessages().add(new VoucherMessage(MessageType.SUBTITLE, inputTitle, fadeInOutRate, stayRate, fadeInOutRate));
									GUIMessageType.this.voucher.sync(true);

									click.manager.showGUI(click.player, new GUIMessagesList(GUIMessageType.this.voucher));
									return true;
								}
							};

							return true;
						}
					};
					return true;
				}
			});
		}

		if (this.voucher.getOptions().getMessages().stream().anyMatch(msg -> msg.getMessageType() == MessageType.ACTION_BAR)) {
			setItem(1, 4, QuickItem.of(CompMaterial.END_ROD).name("&B&lAction Bar").lore("&cYou already have an actionbar message created", "&cdelete the existing one to make another.").make());
		} else {
			setButton(1, 4, QuickItem.of(CompMaterial.END_ROD).name("&B&lAction Bar").lore("", "&b&lClick &8» &7To create actionbar msg").make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Actionbar", "&fEnter actionbar into chat") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, GUIMessageType.this);
				}

				@Override
				public boolean onResult(String inputTitle) {
					GUIMessageType.this.voucher.getOptions().getMessages().add(new VoucherMessage(MessageType.ACTION_BAR, inputTitle, 0, 0, 0));
					GUIMessageType.this.voucher.sync(true);
					click.manager.showGUI(click.player, new GUIMessagesList(GUIMessageType.this.voucher));
					return true;
				}
			});
		}

		setButton(1, 5, QuickItem.of(CompMaterial.OBSERVER).name("&B&lBroadcast").lore("", "&b&lClick &8» &7To create broadcast msg").make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Broadcast Message", "&fEnter broadcast message into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIMessageType.this);
			}

			@Override
			public boolean onResult(String inputTitle) {
				GUIMessageType.this.voucher.getOptions().getMessages().add(new VoucherMessage(MessageType.BROADCAST, inputTitle, 0, 0, 0));
				GUIMessageType.this.voucher.sync(true);
				click.manager.showGUI(click.player, new GUIMessagesList(GUIMessageType.this.voucher));
				return true;
			}
		});

		setButton(1, 6, QuickItem.of(CompMaterial.DARK_OAK_SIGN).name("&B&lChat").lore("", "&b&lClick &8» &7To create chat msg").make(), click -> new TitleInput(Vouchers.getInstance(), click.player, "&b&lNew Chat Message", "&fEnter chat message into chat") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, GUIMessageType.this);
			}

			@Override
			public boolean onResult(String inputTitle) {
				GUIMessageType.this.voucher.getOptions().getMessages().add(new VoucherMessage(MessageType.CHAT, inputTitle, 0, 0, 0));
				GUIMessageType.this.voucher.sync(true);
				click.manager.showGUI(click.player, new GUIMessagesList(GUIMessageType.this.voucher));
				return true;
			}
		});

		applyBackExit();
	}
}
