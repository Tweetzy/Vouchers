package ca.tweetzy.vouchers.events;

import ca.tweetzy.vouchers.voucher.Voucher;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 2:10 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */

@Getter
@Setter
public class VoucherRedeemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private Voucher voucher;

    public VoucherRedeemEvent(Player player, Voucher voucher) {
        this.player = player;
        this.voucher = voucher;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
