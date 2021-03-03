package ca.tweetzy.vouchers.managers;

import ca.tweetzy.vouchers.settings.Settings;
import ca.tweetzy.vouchers.voucher.Voucher;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 9:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class CooldownManager {

    private final HashMap<UUID, Long> entries = new HashMap<>();

    public void add(UUID uuid, Voucher voucher) {
        if (Bukkit.getPlayer(uuid).hasPermission("vouchers.nocooldown")) {
            return;
        }

        if (voucher.getCooldown() != 0) {
            entries.put(uuid, System.currentTimeMillis() + voucher.getCooldown() * 1000L);
        } else {
            entries.put(uuid, System.currentTimeMillis() + Settings.DEFAULT_COOLDOWN_DELAY.getInt() * 1000L);
        }
    }

    public boolean isOnCoolDown(UUID uuid) {
        Long time = entries.get(uuid);

        if (time == null) {
            return false;
        }

        if (time >= System.currentTimeMillis()) {
            return true;
        }

        entries.remove(uuid);
        return false;
    }

    public long getTime(UUID uuid) {
        Long time = entries.get(uuid);

        if (time == null) {
            return 0L;
        }
        return (time - System.currentTimeMillis()) / 1000;
    }
}
