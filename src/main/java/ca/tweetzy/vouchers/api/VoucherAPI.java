package ca.tweetzy.vouchers.api;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.voucher.Voucher;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 02 2021
 * Time Created: 10:46 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class VoucherAPI {

    private static VoucherAPI instance;

    private VoucherAPI() {
    }

    /**
     * @return An instance of the Voucher API
     */
    public static VoucherAPI getInstance() {
        if (instance == null) {
            instance = new VoucherAPI();
        }
        return instance;
    }

    /**
     * Used to check if a voucher exists on the file
     *
     * @param id The ID/Name used when creating a voucher
     * @return true if the voucher is present on file.
     */
    public boolean doesVoucherExists(String id) {
        return Vouchers.getInstance().getData().contains("vouchers." + id.toLowerCase());
    }

    /**
     * Used to remove a specific voucher from the file
     *
     * @param voucher The ID/Name of the voucher being targeted
     */
    public void removeVoucher(String voucher) {
        Vouchers.getInstance().getData().set("vouchers." + voucher.toLowerCase(), null);
        Vouchers.getInstance().getData().save();
    }

    /**
     * Used to create a new voucher
     *
     * @param voucher An instance of {@link Voucher} class
     */
    public void createVoucher(Voucher voucher) {
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".display name", voucher.getDisplayName());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".permission", voucher.getPermission());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".material", voucher.getMaterial().name());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".lore", voucher.getLore());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.glowing", voucher.isGlowing());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.ask to confirm", voucher.isAskConfirm());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.unbreakable", voucher.isUnbreakable());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.hide attributes", voucher.isHideAttributes());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.remove on use", voucher.isRemoveOnUse());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.send title", voucher.isSendTitle());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.send actionbar", voucher.isSendActionbar());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.cool down.use", voucher.isUseCooldown());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".options.cool down.time", voucher.getCooldown());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".execution.commands", voucher.getCommands());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".execution.broadcast messages", voucher.getBroadcastMessages());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".execution.player messages", voucher.getPlayerMessages());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".execution.redeem sound", voucher.getRedeemSound().name());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.title", voucher.getTitle());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.subtitle", voucher.getSubTitle());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.actionbar", voucher.getActionbarMessage());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.fade in", voucher.getTitleFadeIn());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.stay", voucher.getTitleStay());
        Vouchers.getInstance().getData().set("vouchers." + voucher.getId().toLowerCase() + ".titles.fade out", voucher.getTitleFadeOut());
        Vouchers.getInstance().getData().save();
    }
}
