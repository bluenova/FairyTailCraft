/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.plugin;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

/**
 *
 * @author Sven
 */
public abstract class MagePluginEvent {
    public void callPlayerInteractEvent(PlayerInteractEvent event) {
        
    }
    public void callPlayerHitByProjectilEvent(ProjectileHitEvent event) {
        
    }

    public void callPlayerDeathEvent(PlayerDeathEvent event) {
        
    }

    public void callPlayerFishEvent(PlayerFishEvent event) {
        
    }

    public void callPlayerViolencityEvent(PlayerVelocityEvent event) {
        
    }

    public void callPlayerInventoryEvent(PlayerInventoryEvent event) {
        
    }

    public void callPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        
    }

    public void callEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        
    }
}
