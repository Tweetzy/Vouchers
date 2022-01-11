package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.ItemUtil;
import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.RewardMode;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.model.BoolWord;
import ca.tweetzy.vouchers.settings.Localization;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 7:18 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuVoucherSettings extends Menu {

	private final Voucher voucher;

	private final Button rewardModeButton;
	private final Button glowingButton;
	private final Button askConfirmButton;
	private final Button removeOnUseButton;
	private final Button requireUsePermissionButton;

	private final Button broadcastRedeemButton;
	private final Button sendTitleButton;
	private final Button sendSubtitleButton;
	private final Button sendActionbarButton;

	private final Button broadcastMessageButton;
	private final Button chatMessageButton;
	private final Button titleMessageButton;
	private final Button subtitleMessageButton;
	private final Button actionBarMessageButton;
	private final Button permissionButton;

	private final Button backButton;

	public MenuVoucherSettings(@NonNull final Voucher voucher) {
		this.voucher = voucher;
		setTitle("&b" + voucher.getId() + " &8> &7Settings");
		setSize(9 * 6);

		this.rewardModeButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
				MenuVoucherSettings.this.voucher.getSettings().setRewardMode(MenuVoucherSettings.this.voucher.getSettings().getRewardMode().next());
				saveReopen(player);
			}

			@Override
			public ItemStack getItem() {
				final List<String> rewardModeLore = new ArrayList<>();
				rewardModeLore.add("");
				for (RewardMode value : RewardMode.values()) {
					if (MenuVoucherSettings.this.voucher.getSettings().getRewardMode() == value)
						rewardModeLore.add("&e→ " + ItemUtil.bountifyCapitalized(value));
					else
						rewardModeLore.add("&7" + ItemUtil.bountifyCapitalized(value));

				}

				rewardModeLore.add("");
				rewardModeLore.add("&dClick &7to go to next");

				return ItemCreator.of(CompMaterial.PAPER).name("&e&lReward Mode").lore(rewardModeLore).make();
			}
		};

		this.glowingButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().isGlowing() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lGlowing", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().isGlowing()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setGlowing(!this.voucher.getSettings().isGlowing());
			saveReopen(player);
		});

		this.askConfirmButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().askConfirm() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lAsk Confirm", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().askConfirm()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setAskConfirm(!this.voucher.getSettings().askConfirm());
			saveReopen(player);
		});

		this.removeOnUseButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().removeOnUse() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lRemove On Use", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().removeOnUse()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setRemoveOnUse(!this.voucher.getSettings().removeOnUse());
			saveReopen(player);
		});

		this.requireUsePermissionButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().requiresUsePermission() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lRequire Use Permission", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().requiresUsePermission()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setRequiresUsePermission(!this.voucher.getSettings().requiresUsePermission());
			saveReopen(player);
		});

		this.broadcastRedeemButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().broadcastRedeem() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lBroadcast Redeem", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().broadcastRedeem()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setBroadcastRedeem(!this.voucher.getSettings().broadcastRedeem());
			saveReopen(player);
		});

		this.sendTitleButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().sendTitle() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Title", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendTitle()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setSendTitle(!this.voucher.getSettings().sendTitle());
			saveReopen(player);
		});

		this.sendSubtitleButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().sendSubtitle() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Subtitle", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendSubtitle()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setSendSubtitle(!this.voucher.getSettings().sendSubtitle());
			saveReopen(player);
		});

		this.sendActionbarButton = Button.makeSimple(ItemCreator.of(this.voucher.getSettings().sendActionBar() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE, "&e&lSend Actionbar", "", "&7Current&f: " + BoolWord.get(this.voucher.getSettings().sendActionBar()), "", "&dClick &7to toggle value"), player -> {
			this.voucher.getSettings().setSendActionBar(!this.voucher.getSettings().sendActionBar());
			saveReopen(player);
		});

		this.broadcastMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&lBroadcast Message", "", "&eCurrent&f: " + voucher.getSettings().getBroadcastMessage(), "", "&dClick &7to change broadcast message"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_BROADCAST_TITLE, Localization.VoucherEdit.ENTER_BROADCAST_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setBroadcastMessage(name);
				saveReopen(player);
				return true;
			}
		});

		this.chatMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&lChat Message", "", "&eCurrent&f: " + voucher.getSettings().getRedeemMessage(), "", "&dClick &7to change redeem message"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_CHAT_MSG_TITLE, Localization.VoucherEdit.ENTER_CHAT_MSG_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setRedeemMessage(name);
				saveReopen(player);
				return true;
			}
		});

		this.titleMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&lTitle", "", "&eCurrent&f: " + voucher.getSettings().getTitle(), "", "&dClick &7to change title"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_TITLE_TITLE, Localization.VoucherEdit.ENTER_TITLE_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setTitle(name);
				saveReopen(player);
				return true;
			}
		});

		this.subtitleMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&lSubtitle", "", "&eCurrent&f: " + voucher.getSettings().getSubtitle(), "", "&dClick &7to change subtitle"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_SUBTITLE_TITLE, Localization.VoucherEdit.ENTER_SUBTITLE_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setSubtitle(name);
				saveReopen(player);
				return true;
			}
		});

		this.actionBarMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&LAction Bar", "", "&eCurrent&f: " + voucher.getSettings().getActionBar(), "", "&dClick &7to change title"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_ACTIONBAR_TITLE, Localization.VoucherEdit.ENTER_ACTIONBAR_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setActionbar(name);
				saveReopen(player);
				return true;
			}
		});

		this.permissionButton = Button.makeSimple(ItemCreator.of(CompMaterial.PAPER, "&e&LPermission", "", "&eCurrent&f: " + voucher.getSettings().getPermission(), "", "&dClick &7to change permission"), player -> new TitleInput(player, Localization.VoucherEdit.ENTER_PERMISSION_TITLE, Localization.VoucherEdit.ENTER_PERMISSION_SUBTITLE) {
			@Override
			public boolean onResult(String name) {
				if (name == null || name.length() < 1)
					return false;

				MenuVoucherSettings.this.voucher.getSettings().setPermission(name);
				saveReopen(player);
				return true;
			}
		});


		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherEdit(this.voucher).displayTo(player));
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == getSize() - 9)
			return this.backButton.getItem();

		if (slot == 9 + 1)
			return this.glowingButton.getItem();

		if (slot == 9 + 3)
			return this.askConfirmButton.getItem();

		if (slot == 9 + 5)
			return this.removeOnUseButton.getItem();

		if (slot == 9 + 7)
			return this.requireUsePermissionButton.getItem();

		if (slot == 9 * 2 + 1)
			return this.broadcastRedeemButton.getItem();

		if (slot == 9 * 2 + 3)
			return this.sendTitleButton.getItem();

		if (slot == 9 * 2 + 5)
			return this.sendSubtitleButton.getItem();

		if (slot == 9 * 2 + 7)
			return this.sendActionbarButton.getItem();

		if (slot == 9 * 4 + 1)
			return this.broadcastMessageButton.getItem();

		if (slot == 9 * 4 + 2)
			return this.chatMessageButton.getItem();

		if (slot == 9 * 4 + 3)
			return this.permissionButton.getItem();

		if (slot == 9 * 4 + 4)
			return this.rewardModeButton.getItem();

		if (slot == 9 * 4 + 5)
			return this.titleMessageButton.getItem();

		if (slot == 9 * 4 + 6)
			return this.subtitleMessageButton.getItem();

		if (slot == 9 * 4 + 7)
			return this.actionBarMessageButton.getItem();

		return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make();
	}

	private void saveReopen(@NonNull final Player player) {
		Vouchers.getVoucherManager().getVoucherHolder().save();
		newInstance().displayTo(player);
	}

	@Override
	public Menu newInstance() {
		return new MenuVoucherSettings(this.voucher);
	}
}
