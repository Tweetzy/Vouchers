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

package ca.tweetzy.vouchers.api;

import ca.tweetzy.vouchers.api.voucher.Redeem;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface VouchersAPI {

	// vouchers

	/**
	 * This function returns a list of all vouchers.
	 *
	 * @return A list of all the vouchers in the database.
	 */
	List<Voucher> getAllVouchers();

	/**
	 * Find a voucher by its key.
	 *
	 * @param key The key of the voucher to find.
	 * @return A Voucher object.
	 */
	Voucher findVoucher(@NonNull String key);

	/**
	 * Add a voucher to the local list
	 *
	 * @param voucher The voucher to be added.
	 */
	void addVoucher(@NonNull Voucher voucher);

	/**
	 * Removes a voucher from the local list
	 *
	 * @param voucher The voucher id to remove.
	 */
	void removeVoucher(@NonNull String voucher);

	/**
	 * Returns true if the item is a voucher, false otherwise.
	 *
	 * @param item The item to check.
	 * @return A boolean value.
	 */
	boolean isVoucher(@NonNull final ItemStack item);

	// redeems

	/**
	 * Get all redeems.
	 *
	 * @return A list of all the redeems in the database.
	 */
	List<Redeem> getAllRedeems();

	/**
	 * Find a redeem by its UUID.
	 *
	 * @param uuid The UUID of the redeem you want to find
	 * @return A Redeem object.
	 */
	Redeem findRedeem(@NonNull UUID uuid);

	/**
	 * Adds a redeem to the local list
	 *
	 * @param redeem The redeem object to be added.
	 */
	void addRedeem(@NonNull Redeem redeem);

	/**
	 * Removes a redeem from the local list
	 *
	 * @param uuid The UUID of the redeem to be removed.
	 */
	void removeRedeem(@NonNull UUID uuid);

	/**
	 * Get the total number of times a player has redeemed a voucher
	 *
	 * @param playerUUID The UUID of the player you want to get the total redeems for.
	 * @param voucherId The id of the voucher.
	 * @return The total number of times the player has redeemed the voucher.
	 */
	int getTotalRedeems(@NonNull final UUID playerUUID, @NonNull final String voucherId);

	/**
	 * Get the total number of times a player has redeemed a voucher
	 *
	 * @param player The player you want to get the total redeems for.
	 * @param voucher The voucher you want to get the total redeems of.
	 * @return The total number of times a player has redeemed a voucher.
	 */
	int getTotalRedeems(@NonNull final Player player, @NonNull final Voucher voucher);

	/**
	 * > Returns true if the player has reached the maximum number of times they can redeem the voucher
	 *
	 * @param player The player who is redeeming the voucher.
	 * @param voucher The voucher to check the limit for.
	 * @return A boolean value.
	 */
	boolean isAtRedeemLimit(@NonNull final Player player, @NonNull final Voucher voucher);

	/**
	 * Redeem a voucher for a player
	 *
	 * @param player The player who is redeeming the voucher.
	 * @param voucher The voucher to redeem.
	 * @param ignoreRedeemLimit If true, the player will be able to redeem the voucher even if they have reached the redeem limit.
	 * @param ignoreCooldown If true, the player will be able to redeem the voucher even if they are on cooldown.
	 * @param args The arguments that the player provided when redeeming the voucher.
	 */
	void redeemVoucher(@NonNull final Player player, @NonNull final Voucher voucher, final boolean ignoreRedeemLimit, final boolean ignoreCooldown, List<String> args);

	/**
	 * Redeem a voucher for a player
	 *
	 * @param player The player who is redeeming the voucher.
	 * @param voucher The voucher to redeem.
	 * @param ignoreRedeemLimit If true, the player will be able to redeem the voucher even if they have reached the redeem limit.
	 * @param ignoreCooldown If true, the player will be able to redeem the voucher even if they are on cooldown.
	 */
	void redeemVoucher(@NonNull final Player player, @NonNull final Voucher voucher, final boolean ignoreRedeemLimit, final boolean ignoreCooldown);

	/**
	 * If the player has not redeemed the voucher, then redeem it
	 *
	 * @param player The player who redeemed the voucher.
	 * @param voucher The voucher that the player is redeeming.
	 */
	void registerRedeemIfApplicable(@NonNull final Player player, @NonNull final Voucher voucher);

	// cooldowns

	/**
	 * Adds a player to the cooldown list for a specific voucher
	 *
	 * @param player The player's UUID
	 * @param voucher The voucher that the player is using.
	 */
	void addPlayerToCooldown(@NonNull final UUID player, @NonNull final Voucher voucher);

	/**
	 * Returns true if the player is in cooldown, false otherwise.
	 *
	 * @param player The player's UUID
	 * @return A boolean value.
	 */
	boolean isPlayerInCooldown(@NonNull final UUID player);

	/**
	 * Returns true if the player is in cooldown for the given voucher
	 *
	 * @param player The UUID of the player you want to check.
	 * @param voucher The voucher you want to check if the player is in cooldown for.
	 * @return A boolean value.
	 */
	boolean isPlayerInCooldownForVoucher(@NonNull final UUID player, @NonNull final Voucher voucher);

	/**
	 * Returns the time in milliseconds until the player can use the voucher again
	 *
	 * @param player The UUID of the player you want to check the cooldown time for.
	 * @param voucher The voucher to check the cooldown time for.
	 * @return The cooldown time in milliseconds.
	 */
	long getCooldownTime(@NonNull final UUID player, @NonNull final Voucher voucher);
}
