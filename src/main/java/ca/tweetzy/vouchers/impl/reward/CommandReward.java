/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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

package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.VoucherHelper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
@Getter
public final class CommandReward extends BaseReward {

	private String command;
	private String name;
	private List<String> description;


	public CommandReward(@NonNull final String command, final double chance, final int delay, String name, List<String> description, @NonNull final List<Message> messages) {
		super(RewardType.COMMAND, chance, delay, messages);
		this.command = command;
		this.name = name;
		this.description = description;
	}

	@Override
	public void execute(@NonNull Player player, String[] args) {
		final String cmd = VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, this.command), args).replace("%player%", player.getName());

		if (getDelay() >= 1) {
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd), getDelay());
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}

		getMessages().stream().map(msg -> (BaseMessage) msg).forEach(msg -> msg.send(player, args));
	}
}
