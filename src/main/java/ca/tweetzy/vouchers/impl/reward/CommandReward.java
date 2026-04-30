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

import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.BaseReward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.VoucherHelper;
import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.settings.Translations;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

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
		if (this.command == null || this.command.isEmpty()) {
			Vouchers.getInstance().getLogger().warning("Attempted to execute empty command reward for player: " + player.getName());
			return;
		}
		
		// Replace variables in order: PAPI placeholders -> dynamic args -> player name
		String processedCmd = PAPIHook.tryReplace(player, this.command);
		processedCmd = VoucherHelper.dynamicVariablesReplace(processedCmd, args);
		
		// Replace player-related placeholders (case-insensitive)
		final String playerName = player.getName();
		final String playerUuid = player.getUniqueId().toString();
		final String worldName = player.getWorld().getName();
		
		final String cmd = processedCmd.replace("%player%", playerName)
				.replace("%PLAYER%", playerName)
				.replace("%Player%", playerName)
				.replace("%player_name%", playerName)
				.replace("%PLAYER_NAME%", playerName)
				.replace("%Player_Name%", playerName)
				.replace("%uuid%", playerUuid)
				.replace("%UUID%", playerUuid)
				.replace("%world%", worldName)
				.replace("%WORLD%", worldName)
				.trim();
		
		if (cmd.isEmpty()) {
			Vouchers.getInstance().getLogger().warning("Command reward resulted in empty command for player: " + player.getName());
			return;
		}

		if (!Settings.ALLOW_COMMAND_REWARDS.getBoolean()) {
			Vouchers.getInstance().getLogger().warning("Blocked command reward (disabled in config) for " + player.getName() + ": " + cmd);
			return;
		}

		if (!commandPassesPrefixWhitelist(cmd)) {
			Vouchers.getInstance().getLogger().warning("Blocked command reward (prefix whitelist) for " + player.getName() + ": " + cmd);
			Common.tell(player, TranslationManager.string(Translations.COMMAND_REWARD_BLOCKED));
			return;
		}

		if (Settings.LOG_COMMAND_REWARDS.getBoolean()) {
			Vouchers.getInstance().getLogger().info("Command reward | player=" + player.getName() + " | cmd=" + cmd);
		}

		if (getDelay() >= 1) {
			Bukkit.getServer().getScheduler().runTaskLater(Vouchers.getInstance(), () -> {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				getMessages().stream().map(msg -> (BaseMessage) msg).forEach(msg -> msg.send(player, args));
			}, getDelay());
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				getMessages().stream().map(msg -> (BaseMessage) msg).forEach(msg -> msg.send(player, args));
		}
	}

	private static boolean commandPassesPrefixWhitelist(String cmd) {
		final List<String> prefixes = Settings.COMMAND_REWARD_PREFIX_WHITELIST.getStringList();
		if (prefixes == null || prefixes.isEmpty()) return true;
		final String c = cmd.trim().toLowerCase(Locale.ROOT);
		for (String p : prefixes) {
			if (p == null || p.isBlank()) continue;
			if (c.startsWith(p.trim().toLowerCase(Locale.ROOT))) return true;
		}
		return false;
	}
}
