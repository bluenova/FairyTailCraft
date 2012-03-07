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
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 *
 * @author Sven
 */
public class MagePluginManagerImpl implements MagePluginManager {

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

    @Override
    public Server getServer() {
        return FairyTailCraft.server;
    }

    @Override
    public PlayerConfig getPlayerConfig(Player player) {
        return Util.getPlayerConfig(player);
    }
}
