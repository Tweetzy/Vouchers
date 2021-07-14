package ca.tweetzy.vouchers.settings;

import ca.tweetzy.core.configuration.Config;
import ca.tweetzy.vouchers.Vouchers;

import java.util.HashMap;

/**
 * The current file has been created by Kiran Hart
 * Date Created: June 22 2021
 * Time Created: 2:15 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class LocaleSettings {

    static final HashMap<String, String> languageNodes = new HashMap<>();

    static {
        languageNodes.put("general.prefix", "&8[&eVouchers&8]");
        languageNodes.put("general.air", "&cSorry, but you cannot set the item to air o.O");
        languageNodes.put("general.notanumber", "&cThe entry &4%value% &cis not a valid number!");
        languageNodes.put("general.playeroffline", "&cThe following player &4%player% &ccould not be found!");
        languageNodes.put("general.cooldown", "&cYou must wait &4%remaining_time%&cs before you can use that voucher");

        languageNodes.put("voucher.nopermission", "&cYou do not have permission to use that voucher!");
        languageNodes.put("voucher.novouchers", "&cThere are no vouchers created!");
        languageNodes.put("voucher.create", "&aCreated a new voucher with the id: &2%voucher_id%");
        languageNodes.put("voucher.remove", "&cRemoved the voucher with the id: &4%voucher_id%");
        languageNodes.put("voucher.exists", "&cA voucher by the id &e%voucher_id% &4already exists!");
        languageNodes.put("voucher.invalid", "&cCould not find any vouchers by the id: &4%voucher_id%");
        languageNodes.put("voucher.give", "&aGave &2%player% &ax%amount% &2%voucher_id% &avoucher(s)");
        languageNodes.put("voucher.giveall", "&aGave everyone x%amount% &2%voucher_id% &avoucher(s)");
        languageNodes.put("voucher.received", "&aYou received x%amount% &2%voucher_id% &avoucher(s)");
        languageNodes.put("voucher.reload", "&aYou reloaded the data.yml (vouchers updated)");
    }

    public static void setup() {
        Config config = Vouchers.getInstance().getLocale().getConfig();

        languageNodes.keySet().forEach(key -> {
            config.setDefault(key, languageNodes.get(key));
        });

        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
