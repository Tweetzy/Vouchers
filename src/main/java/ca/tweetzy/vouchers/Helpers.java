package ca.tweetzy.vouchers;

import ca.tweetzy.core.compatibility.ServerVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Base64;

/**
 * The current file has been created by Kiran Hart
 * Date Created: March 06 2021
 * Time Created: 3:09 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public class Helpers {

    public static String toString(Serializable object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object fromString(String s) {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois;
        Object o = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            o = ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }

    public static ItemStack getHeldItem(Player player) {
        return ServerVersion.isServerVersionAbove(ServerVersion.V1_8) ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInHand();
    }
}
