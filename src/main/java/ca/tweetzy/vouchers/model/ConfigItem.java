package ca.tweetzy.vouchers.model;

import ca.tweetzy.tweety.collection.SerializedMap;
import ca.tweetzy.tweety.menu.model.ItemCreator;
import ca.tweetzy.tweety.model.ConfigSerializable;
import ca.tweetzy.tweety.remain.CompMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 4:04 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@AllArgsConstructor
@Getter
public final class ConfigItem implements ConfigSerializable {

	private List<Integer> slots;
	private CompMaterial material;
	private String name;
	private List<String> lore;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Slots", this.slots,
				"Material", this.material,
				"Name", this.name,
				"Lore", this.lore
		);
	}

	public static ConfigItem deserialize(SerializedMap map) {
		return new ConfigItem(map.getList("Slots", Integer.class), map.getMaterial("Material"), map.getString("Name"), map.getStringList("Lore"));
	}

	public ItemCreator build() {
		return ItemCreator.of(this.material).name(this.name).lore(this.lore);
	}
}
