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

package ca.tweetzy.vouchers.impl.reward;

import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.feather.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.AbstractReward;
import ca.tweetzy.vouchers.api.voucher.RewardType;
import ca.tweetzy.vouchers.model.Chance;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;

public final class CommandReward extends AbstractReward {

	@Getter
	private final String command;

	public CommandReward(String command, double chance, int delay) {
		super(RewardType.COMMAND, chance, delay);
		this.command = command;
	}

	@Override
	public void execute(Player player, boolean guarantee, List<String> args) {
		if (guarantee) {
			if (this.getDelay() != -1)
				Common.runLater(this.getDelay(), () -> executeCommand(player, args));
			else
				executeCommand(player, args);

			return;
		}

		if (!Chance.tryChance(this.getChance())) return;

		if (this.getDelay() != -1)
			Common.runLater(this.getDelay(), () -> executeCommand(player, args));
		else
			executeCommand(player, args);
	}

	@Override
	public String toJsonString() {
		final JsonObject object = new JsonObject();

		object.addProperty("command", this.command);
		object.addProperty("chance", this.getChance());
		object.addProperty("delay", this.getDelay());
		object.addProperty("type", RewardType.COMMAND.name());

		return object.toString();
	}

	private void executeCommand(@NonNull final Player player, List<String> args) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), MessageFormat.format(Replacer.replaceVariables(this.command, "player", player.getName()), args.toArray()));
	}
}
