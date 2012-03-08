/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.plugin;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.Util.Util;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEvent;
import bluenova.fairytailcraft.event.MageEventType;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author Sven
 */
public class MagePluginManagerImpl implements MagePluginManager {

    /**
     * Registeres a Magic in FairyTailMagic for the MageType
     * @param name Name of Magic
     * @param magicType Type Name of Magic
     * @param minLevel minimum Level for Magic
     * @param requiredMana required Mana to Use Magic
     * @param call Object of MagePluginEvent
     * @param type Type of Magic
     * @param hidden Magic is Hidden in List (true/false)
     */
    @Override
    public void registerMagic(String name, String magicType, int minLevel, int requiredMana, MagePluginEvent call, MageEventType type, boolean hidden) {
        MageEvent me = new MageEvent();
        me.name = name;
        me.magicType = magicType;
        me.minLevel = minLevel;
        me.requiredMana = requiredMana;
        me.call = call;
        me.type = type;
        me.hidden = hidden;
        FairyTailCraft.registeredEvents.add(me);
    }

    /**
     * Get Bukkit Server Object
     * @return
     */
    @Override
    public Server getServer() {
        return FairyTailCraft.server;
    }

    /**
     * Get PlayerConfig of a Player
     * @param player Player that config is wanted From
     * @return PlayerConfig of Player
     */
    @Override
    public PlayerConfig getPlayerConfig(Player player) {
        return Util.getPlayerConfig(player);
    }

    /**
     * Get Player Owned Monsers
     * @param pl Player that owns the monsers
     * @return List of Playerowned Monsers
     */
    @Override
    public List<LivingEntity> getPlayerOwnedEntitys(Player pl) {
        return FairyTailCraft.ownedLivingEntitys.get(pl);
    }

    /**
     * Sets Player Owned Monsers
     * @param pl Player that owns the monsers
     * @param list List of Playerowned Monsers
     */
    @Override
    public void setPlayerOwnedEntitys(Player pl, List<LivingEntity> list) {
        FairyTailCraft.ownedLivingEntitys.put(pl, list); 
    }
}
