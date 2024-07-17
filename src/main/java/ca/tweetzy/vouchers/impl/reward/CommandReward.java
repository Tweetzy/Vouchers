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

import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.reward.AbstractReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.Chance;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandReward extends AbstractReward {

	@Getter
	private final String command;

	@Getter
	@Setter
	private String claimMessage = "";

	public CommandReward(String command, double chance, int delay) {
		super(RewardType.COMMAND, chance, delay);
		this.command = command;
	}

	@Override
	public boolean execute(Player player, boolean guarantee, List<String> args) {
		if (guarantee) {
			if (this.getDelay() != -1)
				Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> executeCommand(player, args), this.getDelay());
			else
				executeCommand(player, args);

			return true;
		}

		if (!Chance.tryChance(this.getChance())) return false;

		if (this.getDelay() != -1)
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> executeCommand(player, args), this.getDelay());

		else
			executeCommand(player, args);

		return true;
	}

	@Override
	public String getJSONString() {
		final JsonObject object = new JsonObject();

		object.addProperty("command", this.command);
		object.addProperty("chance", this.getChance());
		object.addProperty("delay", this.getDelay());
		object.addProperty("type", RewardType.COMMAND.name());
		object.addProperty("claimMessage", this.claimMessage);

		return object.toString();
	}

	private void executeCommand(@NonNull final Player player, List<String> args) {
		// a 'cheat' way of dealing with this, TODO: create custom voucher command implementation where designed for variable (ie. {0}, {1}) vouchers

		final String formattedCommand = stripFirstColor(this.command);

		if (this.command.matches(".*\\{\\d+\\}.*"))
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), MessageFormat.format(Replacer.replaceVariables(PAPIHook.tryReplace(player, formattedCommand), "player", player.getName()), args.toArray()));

		else Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), Replacer.replaceVariables(PAPIHook.tryReplace(player, formattedCommand), "player", player.getName()));

	}

	private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("^([&§])([0-9A-FK-OR])", Pattern.CASE_INSENSITIVE);

	public String stripFirstColor(String input) {
		if (input == null) {
			return null;
		}
		Matcher matcher = STRIP_COLOR_PATTERN.matcher(input);
		StringBuilder sb = new StringBuilder(input);
		while (matcher.find()) {
			String colorCode = matcher.group(1);
			String colorLetter = matcher.group(2);
			String colorSubstring = colorCode + colorLetter;
			sb.replace(matcher.start(), matcher.end(), "");
			matcher = STRIP_COLOR_PATTERN.matcher(sb.toString());
		}
		return sb.toString();
	}

	@Override
	public String getFriendlyFormat() {
		return String.format("delay: %s chance: %f message: \"%s\" command: \"%s\"", getDelay(), getChance(), getClaimMessage(), getCommand());
	}
}
