package bluenova.fairytailcraft.Util;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.config.PlayerConfig;
import java.util.Date;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 
 * @author Sven
 */
public class Util {

    /**
     * Gets the Config of a Player
     * @param Player that config is wanted
     * @return config of the Player
     */
    public static PlayerConfig getPlayerConfig(Player player) {
        for (int i = 0; i < FairyTailCraft.playerConfigs.size(); i++) {
            if (FairyTailCraft.playerConfigs.get(i).isPlayer(player)) {
                return FairyTailCraft.playerConfigs.get(i);
            }
        }
        return null;
    }

    /**
     * Checks if Player Has Permission
     * @param pl Player to Check
     * @param perm Permission to Check
     * @return Has Permission or Not
     */
    public static boolean hasPermission(Player pl, String perm) {
        if (FairyTailCraft.Permissions != null) {
            if (FairyTailCraft.Permissions.has(pl, perm)) {
                return true;
            } else {
                pl.sendMessage(ChatColor.RED + "No Permission!");
                return false;
            }
        } else {
            if (pl.hasPermission(perm) || pl.isOp()) {
                return true;
            } else {
                pl.sendMessage(ChatColor.RED + "No Permission!");
                return false;
            }
        }
    }

    public static boolean hasCooldown(Player pl, String magic, Long cooldown) {
        HashMap<String, Date> get = FairyTailCraft.playerCDs.get(pl);
        if (get.containsKey(magic)) {
            Date dtLastCast = get.get(magic);
            Date dtNow = new Date();
            if (dtNow.getTime() > dtLastCast.getTime() + cooldown) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    public static void setCooldown(Player pl, String magic) {
        HashMap<String, Date> get = FairyTailCraft.playerCDs.get(pl);
        get.put(magic, new Date());
        FairyTailCraft.playerCDs.put(pl, get);
    }
}
