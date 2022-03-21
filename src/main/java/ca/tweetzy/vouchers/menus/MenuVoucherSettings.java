package ca.tweetzy.vouchers.menus;

import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.util.ItemUtil;
import ca.tweetzy.vouchers.api.RewardMode;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.menus.template.SoundSelectorMenu;
import ca.tweetzy.vouchers.menus.template.View;
import ca.tweetzy.vouchers.model.BoolWord;
import ca.tweetzy.vouchers.settings.Locale;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Date Created: March 20 2022
 * Time Created: 11:32 p.m.
 *
 * @author Kiran Hart
 */
public final class MenuVoucherSettings extends View {

	private final Voucher voucher;

	public MenuVoucherSettings(@NonNull final Voucher voucher) {
		super("&b" + voucher.getId() + " &8> &7Settings");
		setRows(6);
		this.voucher = voucher;
		draw();
	}

	private void draw() {
		reset();

		// sound
		setButton(9 * 2 + 4, ItemCreator.of(CompMaterial.MUSIC_DISC_CHIRP, "&e&lVoucher Sound", "", "&7Current&f: &e" + ItemUtil.bountifyCapitalized(voucher.getSettings().getSound()), "", "&dClick &7to edit voucher sound").make(), click -> {
			click.manager.showGUI(click.player, new SoundSelectorMenu(selected -> {
				this.voucher.getSettings().setSound(selected);
				click.manager.showGUI(click.player, new MenuVoucherSettings(this.voucher));
			}));
		});

		// cooldown
		setButton(9 * 3 + 4, ItemCreator.of(CompMaterial.CLOCK, "&e&lCooldown", "", "&eCurrent&f: " + voucher.getSettings().getCooldown(), "", "&dClick &7to change cooldown", "&7You can set the cool down to -1 to disable").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_COOLDOWN_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_COOLDOWN_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (!NumberUtils.isNumber(name))
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setCooldown(Integer.parseInt(name));
					draw();
					return true;
				}
			};
		});

		// reward mode
		final List<String> rewardModeLore = new ArrayList<>();
		rewardModeLore.add("");
		for (RewardMode value : RewardMode.values()) {
			if (this.voucher.getSettings().getRewardMode() == value)
				rewardModeLore.add("&e→ " + ItemUtil.bountifyCapitalized(value));
			else
				rewardModeLore.add("&7" + ItemUtil.bountifyCapitalized(value));

		}

		rewardModeLore.add("");
		rewardModeLore.add("&dClick &7to go to next");

		setButton(9 + 4, ItemCreator.of(CompMaterial.REPEATER).name("&e&lReward Mode").lore(rewardModeLore).make(), click -> {
			this.voucher.getSettings().setRewardMode(this.voucher.getSettings().getRewardMode().next());
			draw();
		});

		// glow button
		setButton(9 + 1, ItemCreator.of(this.voucher.getSettings().isGlowing() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lGlowing", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().isGlowing()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setGlowing(!this.voucher.getSettings().isGlowing());
			draw();
		});

		// ask confirm
		setButton(9 + 2, ItemCreator.of(this.voucher.getSettings().askConfirm() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lAsk Confirm", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().askConfirm()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setAskConfirm(!this.voucher.getSettings().askConfirm());
			draw();
		});

		// remove on use
		setButton(9 + 6, ItemCreator.of(this.voucher.getSettings().removeOnUse() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lRemove On Use", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().removeOnUse()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setRemoveOnUse(!this.voucher.getSettings().removeOnUse());
			draw();
		});

		// require perm to use
		setButton(9 + 7, ItemCreator.of(this.voucher.getSettings().requiresUsePermission() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lRequire Use Permission", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().requiresUsePermission()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setRequiresUsePermission(!this.voucher.getSettings().requiresUsePermission());
			draw();
		});

		// broadcast
		setButton(9 * 2 + 1, ItemCreator.of(this.voucher.getSettings().broadcastRedeem() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lBroadcast Redeem", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().broadcastRedeem()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setBroadcastRedeem(!this.voucher.getSettings().broadcastRedeem());
			draw();
		});

		// send title
		setButton(9 * 2 + 2, ItemCreator.of(this.voucher.getSettings().sendTitle() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Title", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendTitle()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setSendTitle(!this.voucher.getSettings().sendTitle());
			draw();
		});

		// send subtitle
		setButton(9 * 2 + 6, ItemCreator.of(this.voucher.getSettings().sendSubtitle() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Subtitle", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendSubtitle()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setSendSubtitle(!this.voucher.getSettings().sendSubtitle());
			draw();
		});

		// action bar
		setButton(9 * 2 + 7, ItemCreator.of(this.voucher.getSettings().sendActionBar() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Actionbar", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendActionBar()), "", "&dClick &7to toggle value").make(), click -> {
			this.voucher.getSettings().setSendActionBar(!this.voucher.getSettings().sendActionBar());
			draw();
		});

		// broadcast message
		setButton(9 * 4 + 1, ItemCreator.of(CompMaterial.PAPER, "&e&lBroadcast Message", "", "&eCurrent&f: " + voucher.getSettings().getBroadcastMessage(), "", "&dClick &7to change broadcast message").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_BROADCAST_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_BROADCAST_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setBroadcastMessage(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));
					return true;
				}
			};
		});

		// chat message
		setButton(9 * 4 + 2, ItemCreator.of(CompMaterial.PAPER, "&e&lChat Message", "", "&eCurrent&f: " + voucher.getSettings().getRedeemMessage(), "", "&dClick &7to change redeem message").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_CHAT_MGG_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_CHAT_MSG_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setRedeemMessage(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));
					return true;
				}
			};
		});

		// permission
		setButton(9 * 4 + 4, ItemCreator.of(CompMaterial.PAPER, "&e&LPermission", "", "&eCurrent&f: " + voucher.getSettings().getPermission(), "", "&dClick &7to change permission").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_PERM_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_PERM_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setPermission(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));
					return true;
				}
			};
		});

		// title message
		setButton(9 * 4 + 5, ItemCreator.of(CompMaterial.PAPER, "&e&lTitle", "", "&eCurrent&f: " + voucher.getSettings().getTitle(), "", "&dClick &7to change title").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_TITLE_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_TITLE_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setTitle(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));
					return true;
				}
			};
		});

		// subtitle message
		setButton(9 * 4 + 6, ItemCreator.of(CompMaterial.PAPER, "&e&lSubtitle", "", "&eCurrent&f: " + voucher.getSettings().getSubtitle(), "", "&dClick &7to change subtitle").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_SUBTITLE_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_SUBTITLE_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setSubtitle(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));

					return true;
				}
			};
		});

		// actionbar message
		setButton(9 * 4 + 7, ItemCreator.of(CompMaterial.PAPER, "&e&LAction Bar", "", "&eCurrent&f: " + voucher.getSettings().getActionBar(), "", "&dClick &7to change title").make(), click -> {
			click.gui.exit();
			new TitleInput(click.player, Locale.VOUCHER_EDIT_ENTER_ACTIONBAR_TITLE.getString(), Locale.VOUCHER_EDIT_ENTER_ACTIONBAR_SUBTITLE.getString()) {
				@Override
				public boolean onResult(String name) {
					if (name == null || name.length() < 1)
						return false;

					MenuVoucherSettings.this.voucher.getSettings().setActionbar(name);
					click.manager.showGUI(click.player, new MenuVoucherSettings(MenuVoucherSettings.this.voucher));
					return true;
				}
			};
		});

		setButton(9 * 6 - 9, ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back").make(), click -> click.manager.showGUI(click.player, new MenuVoucherEdit(this.voucher)));
	}
}
