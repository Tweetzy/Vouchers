package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.sync.*;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Voucher extends Displayable, Identifiable<String>, Storeable<Voucher>, Synchronize, Trackable, Jsonable {

	String getItem();

	void setItem(@NonNull final String item);

	VoucherSettings getSettings();

	List<Message> getMessages();

	List<Reward> getRewards();
}
