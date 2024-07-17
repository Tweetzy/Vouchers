package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.sync.Identifiable;
import ca.tweetzy.vouchers.api.sync.Jsonable;
import ca.tweetzy.vouchers.api.sync.Storeable;
import ca.tweetzy.vouchers.api.sync.Synchronize;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface Category extends Identifiable<String>, Storeable<Category>, Synchronize, Jsonable {

	String getName();

	void setName(String name);

	ItemStack getItem();

	void setItem(ItemStack item);

	String getDescription();

	void setDescription(String description);

	Set<String> getVoucherIds();
}
