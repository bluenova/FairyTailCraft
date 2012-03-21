package bluenova.fairytailcraft.plugin;

import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEventType;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Interface from MagePlugin to FairyTailCraft
 * @author Sven
 */
public interface MagePluginManager {
    /**
     * Registeres a Magic in FairyTailMagic for the MageType
     * @param name Name of Magic
     * @param magicType Type Name of Magic
     * @param minLevel minimum Level for Magic
     * @param requiredMana required Mana to Use Magic
     * @param call Object of MagePluginEvent
     * @param type Type of Magic
     * @param hidden Magic is Hidden in List (true/false)
     * @param cooldown Cooldown of Magic in Miliseconds
     */
    void registerMagic(String name, String magicType, int minLevel, int requiredMana, MagePluginEvent call, MageEventType type, boolean hidden, Long cooldown);
    /**
     * Get Bukkit Server Object
     * @return
     */
    Server getServer();
    /**
     * Get PlayerConfig of a Player
     * @param player Player that config is wanted From
     * @return PlayerConfig of Player
     */
    PlayerConfig getPlayerConfig(Player player);
        
    /**
     * Get Player Owned Monsers
     * @param pl Player that owns the monsers
     * @return List of Playerowned Monsers
     */
    public List<LivingEntity> getPlayerOwnedEntitys(Player pl);

    /**
     * Sets Player Owned Monsers
     * @param pl Player that owns the monsers
     * @param list List of Playerowned Monsers
     */
    public void setPlayerOwnedEntitys(Player pl, List<LivingEntity> list);
    
     /**
     * Registers a CommandListener Class
     * @param cls Class with Commands
     * @return Class successfuly registered (false if class is already registered)
     */
    public boolean registerCommandListener(Class<?> cls, MagePlugin plg);
}
