/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.Util;

import bluenova.fairytailcraft.FairyTailCraft;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 *
 * @author Sven
 */
public class ManaRegenThread extends Thread {

    private Player player;
    private boolean run = true;

    public ManaRegenThread(Player player) {
        this.player = player;
    }
    
    public void stopThread() {
        run = false;
    }

    @Override
    public void run() {
        boolean firstRun = true;
        while(run) {
            if(!firstRun) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                }
            } 
            firstRun = false;
            if(!Util.getPlayerConfig(player).getMageType().equals("none"))
                Util.getPlayerConfig(player).addMana(getManaRegeneration());            
        }
    }
    
    private Integer getManaRegeneration() {
        Double baseManaRegen = 10.0 * FairyTailCraft.configuration.getManaRegenMultibler();
        Integer level = Util.getPlayerConfig(player).getLevel();
        for(int i = 0; i < level; i++) {
            baseManaRegen = baseManaRegen + (baseManaRegen * 0.05);
        }
        return baseManaRegen.intValue();
    }
}
