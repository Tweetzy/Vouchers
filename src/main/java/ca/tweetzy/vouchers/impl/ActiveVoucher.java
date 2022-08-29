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

import ca.tweetzy.feather.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.api.voucher.RewardMode;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.VoucherOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

	@Override
	public String getId() {
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
		return this.rewards;
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
	public void setDescription(List<String> description) {
		this.description = description;
	}

	@Override
	public String getRewardJson() {
		final JsonArray jsonArray = new JsonArray();

		this.rewards.forEach(reward -> {
			final JsonObject object = JsonParser.parseString(reward.toJsonString()).getAsJsonObject();
			jsonArray.add(object);
		});

		return jsonArray.toString();
	}

	@Override
	public void sync(boolean silent) {
		Vouchers.getDataManager().updateVoucher(this, null);
	}

	@Override
	public ItemStack buildItem() {
		return QuickItem
				.of(this.item)
				.name(this.name)
				.lore(this.description)
				.glow(this.options.isGlowing())
				.hideTags(true)
				.unbreakable(true)
				.tag("Tweetzy:Vouchers", this.id)
				.make();
	}

	@Override
	public ItemStack buildItem(List<String> args) {
		String vArgs = String.join(" ", args);

		return QuickItem
				.of(this.item)
				.name(java.text.MessageFormat.format(this.name, args.toArray()))
				.lore(this.description.stream().map(line -> java.text.MessageFormat.format(line, args.toArray())).collect(Collectors.toList()))
				.glow(this.options.isGlowing())
				.hideTags(true)
				.unbreakable(true)
				.tag("Tweetzy:Vouchers", this.id)
				.tag("Tweetzy:VouchersArgs", vArgs)
				.make();
	}
}
