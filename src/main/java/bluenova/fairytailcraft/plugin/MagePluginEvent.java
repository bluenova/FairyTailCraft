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
     * @return Call Successfully?
     */
    public boolean callPlayerInteractEvent(PlayerInteractEvent event) {
        return false;
    }

    /**
     * Called when EventType is GETHITBYENTITY or when Other Events by Shooter cause a ProjectileHitEvent
     * @param event
     * @return Call Successfully?
     */
    public boolean callEntityHitByProjectilEvent(ProjectileHitEvent event) {
        return false;
    }

    /**
     * Called when EventType is DEATH
     * @param event
     * @return Call Successfully?
     */
    public boolean callPlayerDeathEvent(PlayerDeathEvent event) {
        return false;
    }

    /**
     * Called when EventType is FISH
     * @param event
     * @return Call Successfully?
     */
    public boolean callPlayerFishEvent(PlayerFishEvent event) {
        return false;
    }

    /**
     * Called When EventType is VELOCITY
     * @param event
     * @return Call Successfully?
     */
    public boolean callPlayerViolencityEvent(PlayerVelocityEvent event) {
        return false;
    }

    /**
     * Called when EventType is INVENTORY
     * @param event
     * @return Call Successfully?
     */
    @Deprecated
    public boolean callPlayerInventoryEvent(PlayerInventoryEvent event) {
        return false;
    }

    /**
     * Called when EventType is ITEM
     * @param event
     * @return Call Successfully?
     */
    public boolean callPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        return false;
    }

    /**
     * Called when EventType is GETDMGBYENTITY or wen Damager has Magic active
     * @param event
     * @return Call Successfully?
     */
    public boolean callEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        return false;
    }
}
