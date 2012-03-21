/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.event;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.Util.ManaRegenThread;
import bluenova.fairytailcraft.Util.Util;
import bluenova.fairytailcraft.config.PlayerConfig;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

/**
 *
 * @author Sven
 */
public class PlayerEvents implements Listener {

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerLogin(PlayerJoinEvent event) {
        PlayerConfig cfg = new PlayerConfig(event.getPlayer());
        cfg.recalculateLevel();
        FairyTailCraft.playerConfigs.add(cfg);
        FairyTailCraft.activeMagic.put(event.getPlayer(), null);
        ManaRegenThread mana = new ManaRegenThread(event.getPlayer());
        mana.start();
        FairyTailCraft.manaReg.put(event.getPlayer(), mana);

        //Add new Cooldown map when Player login for the First time
        if (!FairyTailCraft.playerCDs.containsKey(event.getPlayer())) {
            HashMap<String, Date> hm = new HashMap<String, Date>();
            FairyTailCraft.playerCDs.put(event.getPlayer(), hm);
        }

        System.out.println("[FairyTailCraft] Player " + event.getPlayer().getName() + " logged in!");
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        for (int i = 0; i < FairyTailCraft.playerConfigs.size(); i++) {
            if (FairyTailCraft.playerConfigs.get(i).isPlayer(event.getPlayer())) {
                FairyTailCraft.playerConfigs.remove(i);
                FairyTailCraft.activeMagic.remove(event.getPlayer());
                FairyTailCraft.manaReg.get(event.getPlayer()).stopThread();
                FairyTailCraft.activeMagic.remove(event.getPlayer());
                System.out.println("[FairyTailCraft] Player " + event.getPlayer().getName() + " logged out!");
            }
        }
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerUseEvent(PlayerInteractEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String[] magic = FairyTailCraft.activeMagic.get(event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig(event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.INTERACT) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerInteractEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String[] magic = FairyTailCraft.activeMagic.get(event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig(event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.INTERACTENTITY) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerInteractEntityEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerFish(PlayerFishEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String magic[] = FairyTailCraft.activeMagic.get((Player) event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig((Player) event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.FISH) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerFishEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerVelocity(PlayerVelocityEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String magic[] = FairyTailCraft.activeMagic.get((Player) event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig((Player) event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.VELOCITY) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerViolencityEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerInventory(PlayerInventoryEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String magic[] = FairyTailCraft.activeMagic.get((Player) event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig((Player) event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.INVENTORY) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerInventoryEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerItemHeld(PlayerItemHeldEvent event) {
        //if (!event.isBlockInHand() && !event.hasItem()) {
        String magic[] = FairyTailCraft.activeMagic.get((Player) event.getPlayer());
        if (magic == null) {
            return;
        }
        String mageType = Util.getPlayerConfig((Player) event.getPlayer()).getMageType();
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.ITEMHELD) {
                    if (Util.getPlayerConfig(event.getPlayer()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            Long cd = FairyTailCraft.registeredEvents.get(i).cooldown;
                            if (Util.hasCooldown(event.getPlayer(), magic[0], cd)) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerItemHeldEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig(event.getPlayer()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    Util.setCooldown(event.getPlayer(), magic[0]);
                                }
                            } else {
                                event.getPlayer().sendMessage("On Cooldown!");
                            }
                        }
                    }
                }
                return;
            }
        }
        //}
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerHitByProjectile(ProjectileHitEvent event) {
        Projectile proj = (Projectile) event.getEntity();
        List<Entity> get = proj.getNearbyEntities(1, 1, 1);
        if (get.size() > 0) {
            Entity target = get.get(0);
            if (target instanceof Player) {
                String mageType = Util.getPlayerConfig((Player) target).getMageType();
                for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
                    if (FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                        if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.GETHITBYPROJECTILE) {
                            if (Util.getPlayerConfig((Player) target).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                                if (FairyTailCraft.registeredEvents.get(i).call != null) {
                                    if (FairyTailCraft.registeredEvents.get(i).call.callEntityHitByProjectilEvent(event, 1)) {
                                        Util.getPlayerConfig((Player) target).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                    }
                                }
                            }
                        }

                    }
                }
            }
            if (proj.getShooter() instanceof Player) {
                String magic[] = FairyTailCraft.activeMagic.get((Player) proj.getShooter());
                if (magic == null) {
                    return;
                }
                String mageType = Util.getPlayerConfig((Player) proj.getShooter()).getMageType();
                for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
                    if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                        FairyTailCraft.registeredEvents.get(i).call.callEntityHitByProjectilEvent(event, new Integer(magic[1]));
                    }
                }
            }
        }
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void playerGetDmgByEntity(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        if (target instanceof Player) {
            String mageType = Util.getPlayerConfig((Player) target).getMageType();
            for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
                if (FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                    if (Util.getPlayerConfig((Player) target).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                        if (FairyTailCraft.registeredEvents.get(i).call != null) {
                            if (FairyTailCraft.registeredEvents.get(i).call.callEntityDamageByEntityEvent(event, 1)) {
                                Util.getPlayerConfig((Player) target).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                            }
                        }
                    }
                }
            }
        }
        if (event.getDamager() instanceof Player) {
            String magic[] = FairyTailCraft.activeMagic.get((Player) event.getDamager());
            if (magic == null) {
                return;
            }
            String mageType = Util.getPlayerConfig((Player) event.getDamager()).getMageType();
            for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
                if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                    if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.GETDMGBYENTITY) {
                        FairyTailCraft.registeredEvents.get(i).call.callEntityDamageByEntityEvent(event, new Integer(magic[1]));
                    }
                }
            }
        }
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void onPlayerGetExp(PlayerExpChangeEvent event) {
        if (event.getAmount() > 0) {
            if (!Util.getPlayerConfig(event.getPlayer()).getMageType().equals("none")) {
                Util.getPlayerConfig(event.getPlayer()).addExp(event.getAmount());
                Util.getPlayerConfig(event.getPlayer()).recalculateLevel();
            }
        }
    }

    /**
     * 
     * @param event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            String magic[] = FairyTailCraft.activeMagic.get((Player) event.getEntity());
            if (magic == null) {
                return;
            }
            String mageType = Util.getPlayerConfig((Player) event.getEntity()).getMageType();
            for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
                if (FairyTailCraft.registeredEvents.get(i).name.equals(magic[0]) && FairyTailCraft.registeredEvents.get(i).magicType.equals(mageType)) {
                    if (FairyTailCraft.registeredEvents.get(i).type == MageEventType.DEATH) {
                        if (Util.getPlayerConfig((Player) event.getEntity()).delCalcManaCheck(FairyTailCraft.registeredEvents.get(i).requiredMana)) {
                            if (FairyTailCraft.registeredEvents.get(i).call != null) {
                                if (FairyTailCraft.registeredEvents.get(i).call.callPlayerDeathEvent(event, new Integer(magic[1]))) {
                                    Util.getPlayerConfig((Player) event.getEntity()).delCalcMana(FairyTailCraft.registeredEvents.get(i).requiredMana);
                                }
                            }
                        }
                    }
                    return;
                }
            }
            if (!Util.getPlayerConfig((Player) event.getEntity()).getMageType().equals("none")) {
                Integer exp = Util.getPlayerConfig((Player) event.getEntity()).getExp();
                Float tmp = exp.floatValue() * 0.9f;
                exp = tmp.intValue();
                Util.getPlayerConfig((Player) event.getEntity()).setExp(exp);
                Util.getPlayerConfig((Player) event.getEntity()).recalculateLevel();
            }
        }
    }
}
