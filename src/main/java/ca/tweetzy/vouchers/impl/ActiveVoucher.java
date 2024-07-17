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

package ca.tweetzy.vouchers.impl;

import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.VoucherOptions;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class ActiveVoucher implements Voucher {

	private final String id;
	private String name;
	private ItemStack item;
	private List<String> description;
	private RewardMode rewardMode;
	private VoucherOptions options;
	private List<Reward> rewards;
	private EquipmentSlot voucherHand;

	@Override
	public @NonNull String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public List<String> getDescription() {
		return this.description;
	}

	@Override
	public VoucherOptions getOptions() {
		return this.options;
	}

	@Override
	public List<Reward> getRewards() {
		final List<Reward> commandRewards = new ArrayList<>(this.rewards).stream().filter(reward -> reward instanceof CommandReward).collect(Collectors.toList());

		commandRewards.addAll(new ArrayList<>(this.rewards).stream().filter(reward -> reward instanceof ItemReward).map(reward -> (ItemReward) reward).filter(reward -> reward.getItem() != null).toList());

		return commandRewards;
	}

	@Override
	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

	@Override
	public RewardMode getRewardMode() {
		return this.rewardMode;
	}

	@Override
	public void setRewardMode(RewardMode rewardMode) {
		this.rewardMode = rewardMode;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setItem(ItemStack item) {
		this.item = item;
	}

	@Override
	public void setVoucherHand(EquipmentSlot hand) {
		this.voucherHand = hand;
	}

	@Override
	public EquipmentSlot getVoucherHand() {
		return this.voucherHand;
	}

	@Override
	public void setDescription(List<String> description) {
		this.description = description;
	}

	@Override
	public String getJSONString() {
		final JsonArray jsonArray = new JsonArray();

		this.rewards.forEach(reward -> {
			final JsonObject object = JsonParser.parseString(reward.getJSONString()).getAsJsonObject();
			jsonArray.add(object);
		});

		return jsonArray.toString();
	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.getDataManager().updateVoucher(this, (error, res) -> {
			if (syncResult != null)
				syncResult.accept(error == null && res ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE);
		});
	}

	@Override
	public void store(@NonNull Consumer<Voucher> stored) {
		Vouchers.getDataManager().createVoucher(this, (error, created) -> {
			if (error == null && created != null) {
				stored.accept(created);
				Vouchers.getVoucherManager().add(created);
			} else {
				stored.accept(null);
			}
		});
	}

	@Override
	public ItemStack buildItem(Player player) {
//		this.description =this.description.stream().filter(line -> !line.contains("-Blank-")).collect(Collectors.toList());
		final ItemStack item = QuickItem
				.of(this.item)
				.name(PAPIHook.tryReplace(player, this.name))
				.lore(PAPIHook.tryReplace(player, this.getFilteredDescription()))
				.glow(this.options.isGlowing())
				.hideTags(true)
				.tag("Tweetzy:Vouchers", this.id)
				.make();

		return this.unbreakable(item);
	}

	@Override
	public ItemStack buildItem(Player player, List<String> args) {
		String vArgs = String.join(" ", args);

		final ItemStack item = QuickItem
				.of(this.item)
				.name(java.text.MessageFormat.format(PAPIHook.tryReplace(player, this.name), args.toArray()))
				.lore(PAPIHook.tryReplace(player, this.getFilteredDescription()).stream().map(line -> java.text.MessageFormat.format(line, args.toArray())).collect(Collectors.toList()))
				.glow(this.options.isGlowing())
				.hideTags(true)
				.tag("Tweetzy:Vouchers", this.id)
				.tag("Tweetzy:VouchersArgs", vArgs)
				.make();

		return this.unbreakable(item);
	}

	private ItemStack unbreakable(ItemStack item) {
		final ItemMeta meta = item.getItemMeta();

		if (meta == null)
			return item;

		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public void addReward(Reward reward) {
		this.rewards.add(reward);
		sync(null);
	}

	@Override
	public void removeReward(Reward reward) {
		this.rewards.remove(reward);
		sync(null);
	}

}
