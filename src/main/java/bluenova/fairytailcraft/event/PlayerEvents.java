/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.event;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.config.PlayerConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Sven
 */
public class PlayerEvents implements Listener {

    @EventHandler
    public void playerLogin(PlayerJoinEvent event) {
        PlayerConfig cfg = new PlayerConfig(event.getPlayer());
        FairyTailCraft.playerConfigs.add(cfg);
        System.out.println("[FairyTailCraft] Player "+ event.getPlayer().getName()+ " logged in!");
    }
    
    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        for(int i = 0; i < FairyTailCraft.playerConfigs.size(); i++) {
            if(FairyTailCraft.playerConfigs.get(i).isPlayer(event.getPlayer())){
                FairyTailCraft.playerConfigs.remove(i);
                System.out.println("[FairyTailCraft] Player "+ event.getPlayer().getName()+ " logged out!");
            }
        }
    }
}
