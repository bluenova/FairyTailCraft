package bluenova.fairytailcraft;

import bluenova.fairytailcraft.config.PlayerConfig;
import org.bukkit.entity.Player;

public class Util {

    public static PlayerConfig getPlayerConfig(Player player) {
        for (int i = 0; i < FairyTailCraft.playerConfigs.size(); i++) {
            if (FairyTailCraft.playerConfigs.get(i).isPlayer(player)) {
                return FairyTailCraft.playerConfigs.get(i);
            }
        }
        return null;
    }
}
