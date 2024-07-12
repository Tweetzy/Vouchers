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

package ca.tweetzy.vouchers.gui.admin.reward;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.gui.VouchersBaseGUI;
import lombok.NonNull;
import org.bukkit.entity.Player;

public final class GUIRewardType extends VouchersBaseGUI {

	private final Voucher voucher;

	public GUIRewardType(@NonNull final Player player, @NonNull final Voucher voucher) {
		super(new GUIRewardList(player, voucher), player, "&bVouchers &8> &7" + voucher.getId() + " &8> &7Reward Type", 4);
		this.voucher = voucher;
		draw();

	}

	@Override
	protected void draw() {

		setButton(1, 2, QuickItem
				.of(CompMaterial.PAPER)
				.name("&b&lCommand Reward")
				.lore("")
				.lore("&b&lClick &8» &7To create command reward")
				.make(), click -> click.manager.showGUI(click.player, new GUICreateReward(click.player, this.voucher, RewardType.COMMAND)));

		setButton(1, 6, QuickItem
				.of(CompMaterial.DIAMOND_SWORD)
				.name("&b&lItem Reward")
				.lore("")
				.lore("&b&lClick &8» &7To create item reward")
				.make(), click -> click.manager.showGUI(click.player, new GUICreateReward(click.player, this.voucher, RewardType.ITEM)));

		applyBackExit();
	}
}
