package bluenova.fairytailcraft.plugin;

import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEventType;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public interface MagePluginManager {
    void registerMagic(String name, String magicType, int minLevel, int requiredMana, MagePluginEvent call, MageEventType type);
    Server getServer();
    PlayerConfig getPlayerConfig(Player player);
}
