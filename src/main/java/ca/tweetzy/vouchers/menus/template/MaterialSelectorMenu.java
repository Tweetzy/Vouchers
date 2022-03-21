package ca.tweetzy.vouchers.menus.template;

import ca.tweetzy.tweety.model.ItemCreator;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.model.InventoryBorder;
import ca.tweetzy.vouchers.model.InventorySafeMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Date Created: February 17 2022
 * Time Created: 9:36 p.m.
 *
 * @author Kiran Hart
 */
public final class MaterialSelectorMenu extends View {

	private final List<Integer> fillSlots = new ArrayList<>();
	private final Consumer<CompMaterial> selected;


	public MaterialSelectorMenu(Consumer<CompMaterial> selected) {
		super("&eSelect a material");
		setRows(6);
		setDefaultItem(CompMaterial.BLACK_STAINED_GLASS_PANE.toItem());
		this.fillSlots.addAll(InventoryBorder.getInsideBorders(this.rows));
		this.selected = selected;

		draw();
	}

	private void draw() {
		reset();

		InventoryBorder.getBorders(rows).forEach(slot -> setItem(slot, this.getDefaultItem()));

		final List<CompMaterial> itemsToFill = InventorySafeMaterials.get().stream().skip((page - 1) * (long) this.fillSlots.size()).limit(this.fillSlots.size()).collect(Collectors.toList());
		pages = (int) Math.max(1, Math.ceil(InventorySafeMaterials.get().size() / (double) this.fillSlots.size()));

		generateNavigation();
		setOnPage(e -> draw());

		for (int i = 0; i < this.rows * 9; i++) {
			if (this.fillSlots.contains(i) && this.fillSlots.indexOf(i) < itemsToFill.size()) {
				final CompMaterial material = itemsToFill.get(this.fillSlots.indexOf(i));

				setButton(i, ItemCreator.of(material).lore("&eClick &7to select").make(), e -> {
					this.selected.accept(material);
				});
			}
		}
	}
}
