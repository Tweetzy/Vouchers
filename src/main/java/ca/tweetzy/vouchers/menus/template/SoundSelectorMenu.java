package ca.tweetzy.vouchers.menus.template;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.remain.CompSound;
import ca.tweetzy.tweety.util.ItemUtil;
import ca.tweetzy.vouchers.model.InventoryBorder;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Date Created: February 17 2022
 * Time Created: 9:36 p.m.
 *
 * @author Kiran Hart
 */
public final class SoundSelectorMenu extends View {

	private final List<Integer> fillSlots = new ArrayList<>();
	private final Consumer<CompSound> selected;


	public SoundSelectorMenu(Consumer<CompSound> selected) {
		super("&eSelect a sound");
		setRows(6);
		setDefaultItem(CompMaterial.BLACK_STAINED_GLASS_PANE.toItem());
		this.fillSlots.addAll(InventoryBorder.getInsideBorders(this.rows));
		this.selected = selected;

		draw();
	}

	private void draw() {
		reset();

		InventoryBorder.getBorders(rows).forEach(slot -> setItem(slot, this.getDefaultItem()));

		final List<CompSound> itemsToFill = Arrays.stream(CompSound.values()).skip((page - 1) * (long) this.fillSlots.size()).limit(this.fillSlots.size()).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(CompSound.values().length / (double) this.fillSlots.size()));

		generateNavigation();
		setOnPage(e -> draw());

		for (int i = 0; i < this.rows * 9; i++) {
			if (this.fillSlots.contains(i) && this.fillSlots.indexOf(i) < itemsToFill.size()) {
				final CompSound sound = itemsToFill.get(this.fillSlots.indexOf(i));

				setButton(i, ItemCreator
						.of(CompMaterial.MUSIC_DISC_CHIRP)
						.name("&e&l" + ItemUtil.bountifyCapitalized(sound))
						.lore("&dLeft Click &7to select this sound", "&dRight Click &7to play sound")
						.hideTags(true)
						.make(), click -> {

					if (click.clickType == ClickType.LEFT) {
						this.selected.accept(sound);
					}

					if (click.clickType == ClickType.RIGHT) {
						click.player.stopAllSounds();
						sound.play(click.player);
					}
				});
			}
		}
	}
}
