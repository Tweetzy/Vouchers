package ca.tweetzy.vouchers.menu;

import ca.tweetzy.tweety.Common;
import ca.tweetzy.tweety.ItemUtil;
import ca.tweetzy.tweety.menu.Menu;
import ca.tweetzy.tweety.menu.MenuPagged;
import ca.tweetzy.tweety.menu.button.Button;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.remain.CompSound;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 4:59 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class MenuSoundSelect extends MenuPagged<CompSound> {

	private final Voucher voucher;
	private final Button backButton;

	public MenuSoundSelect(@NonNull final Voucher voucher) {
		super(Arrays.stream(CompSound.values()).sorted(Comparator.comparing(CompSound::name)).collect(Collectors.toList()));
		setTitle("&eSelect Sound");
		this.voucher = voucher;
		this.backButton = Button.makeSimple(ItemCreator.of(CompMaterial.IRON_DOOR, "&e&lBack", "", "&dClick &7to go back"), player -> new MenuVoucherSettings(this.voucher).displayTo(player));
	}

	@Override
	protected ItemStack convertToItemStack(CompSound sound) {
		return ItemCreator
				.of(CompMaterial.MUSIC_DISC_CHIRP)
				.name("&e&l" + ItemUtil.bountifyCapitalized(sound))
				.lore("&dLeft Click &7to select this sound", "&dRight Click &7to play sound")
				.hideTags(true)
				.make();
	}

	@Override
	protected List<Button> getButtonsToAutoRegister() {
		return Collections.singletonList(this.backButton);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == getSize() - 9)
			return this.backButton.getItem();

		return super.getItemAt(slot);
	}

	@Override
	protected void onPageClick(Player player, CompSound sound, ClickType click) {
		if (click == ClickType.LEFT) {
			this.voucher.getSettings().setSound(sound);
			Common.runAsync(() -> Vouchers.getVoucherManager().getVoucherHolder().save());
			new MenuVoucherEdit(this.voucher).displayTo(player);
		}

		if (click == ClickType.RIGHT) {
			player.stopAllSounds();
			sound.play(player);
		}
	}

	@Override
	public Menu newInstance() {
		return new MenuSoundSelect(this.voucher);
	}
}
