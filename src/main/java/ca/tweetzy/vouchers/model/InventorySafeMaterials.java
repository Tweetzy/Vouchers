package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.remain.CompMaterial;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 6:37 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class InventorySafeMaterials {

	public List<CompMaterial> get() {
		final List<CompMaterial> list = new ArrayList<>();

		final Inventory drawer = Bukkit.createInventory(null, 9, "Valid Materials");

		for (int i = 0; i < CompMaterial.values().length; i++) {
			final CompMaterial material = CompMaterial.values()[i];
			drawer.setItem(0, material.toItem());
			if (drawer.getItem(0) != null) {
				drawer.setItem(0, null);
				list.add(material);
			}
		}

		return list.stream().sorted(Comparator.comparing(CompMaterial::name)).collect(Collectors.toList());
	}
}
