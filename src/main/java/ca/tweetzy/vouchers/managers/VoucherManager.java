package ca.tweetzy.vouchers.managers;

import ca.tweetzy.vouchers.voucher.Voucher;

import java.util.Collection;
import java.util.HashMap;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 9:11 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherManager {

    private final HashMap<String, Voucher> vouchers = new HashMap<>();

    public Voucher addVoucher(Voucher voucher) {
        return vouchers.put(voucher.getId(), voucher);
    }

    public Voucher removeVoucher(Voucher voucher) {
        return vouchers.remove(voucher);
    }

    public Voucher getVoucher(String id) {
        return vouchers.get(id);
    }

    public Collection<Voucher> getVouchers() {
        return vouchers.values();
    }

}
