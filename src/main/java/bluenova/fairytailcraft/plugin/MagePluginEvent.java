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
 * Abstract Mage Plugin Event
 * @author Sven
 */
public abstract class MagePluginEvent {

    /**
     * Called when EventType is INTERACT
     * @param event
     */
    public void callPlayerInteractEvent(PlayerInteractEvent event) {
    }

    /**
     * Called when EventType is GETHITBYENTITY or when Other Events by Shooter cause a ProjectileHitEvent
     * @param event
     */
    public void callEntityHitByProjectilEvent(ProjectileHitEvent event) {
    }

    /**
     * Called when EventType is DEATH
     * @param event
     */
    public void callPlayerDeathEvent(PlayerDeathEvent event) {
    }

    /**
     * Called when EventType is FISH
     * @param event
     */
    public void callPlayerFishEvent(PlayerFishEvent event) {
    }

    /**
     * Called When EventType is VELOCITY
     * @param event
     */
    public void callPlayerViolencityEvent(PlayerVelocityEvent event) {
    }

    /**
     * Called when EventType is INVENTORY
     * @param event
     */
    @Deprecated
    public void callPlayerInventoryEvent(PlayerInventoryEvent event) {
    }

    /**
     * Called when EventType is ITEM
     * @param event
     */
    public void callPlayerItemHeldEvent(PlayerItemHeldEvent event) {
    }

    /**
     * Called when EventType is GETDMGBYENTITY or wen Damager has Magic active
     * @param event
     */
    public void callEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    }
}
