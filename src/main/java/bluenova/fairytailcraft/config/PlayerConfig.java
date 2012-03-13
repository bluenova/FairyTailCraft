package bluenova.fairytailcraft.config;

import bluenova.fairytailcraft.FairyTailCraft;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Config of a Player
 * @author Sven
 */
public class PlayerConfig {

    private Player player;
    private String fileString;
    private File file;
    private YamlConfiguration config;

    /**
     * 
     * @param player Player thats config is defined
     */
    public PlayerConfig(Player player) {
        this.player = player;
        this.fileString = "plugins" + File.separator + "FairyTailCraft" + File.separator + "players" + File.separator + this.player.getName() + ".yml";
        this.file = new File(this.fileString);
        this.config = new YamlConfiguration();
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.createConfig();
        } else {
            try {
                this.config.load(this.file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidConfigurationException ex) {
                Logger.getLogger(MainConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createConfig() {
        this.config.set("name", this.player.getName());
        this.config.set("magetype", "none");
        this.config.set("exp", 0);
        this.config.set("level", 1);
        this.config.set("mana", 100);
        this.config.set("maxMana", 0);
        saveConfig();
    }

    private void saveConfig() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if the Player defined in Config is the same like the Inputed
     * @param pl Player to check
     * @return Player is Player of Config
     */
    public boolean isPlayer(Player pl) {
        if (this.player == pl) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get Typ of Magic from Config
     * @return Typ of Magic
     */
    public String getMageType() {
        String type = this.config.getString("magetype");
        if (type == null) {
            type = "none";
        }
        return type;
    }

    /**
     * Sets a Magic to Config
     * @param string Name of Magic to Learn
     */
    public void learnMagic(String string) {
        this.config.set("magetype", string);
        saveConfig();
    }

    /**
     * Get Magic level of the Player
     * @return Magic Level
     */
    public int getLevel() {
        Integer level = this.config.getInt("level");
        return level;
    }

    /**
     * Add exp to the Player in config
     * @param amount Amout of EXP to add
     */
    public void addExp(int amount) {
        Integer Exp = this.config.getInt("exp");
        Exp += amount;
        this.config.set("exp", Exp);
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets exp of Player in config
     * @param exp Value to set EXP to
     */
    public void setExp(int exp) {
        this.config.set("exp", exp);
        saveConfig();
    }

    /**
     * Get Players EXP from config
     * @return Players EXP
     */
    public int getExp() {
        Integer Exp = this.config.getInt("exp");
        return Exp;
    }

    /**
     * Recalculate the Level of the Player and Send a Message to him/her if he Levels up/down.
     */
    public void recalculateLevel() {
        Integer oldLevel = this.config.getInt("level");
        Integer Exp = this.config.getInt("exp");
        double neededExp = 100.00 * FairyTailCraft.configuration.getlevelMultibler();
        Integer newLevel = 1;
        while (neededExp < Exp && (newLevel < FairyTailCraft.configuration.getMaxLevel() || FairyTailCraft.configuration.getMaxLevel() == -1)) {
            newLevel++;
            neededExp = neededExp + (neededExp * 0.08);
        }
        if (oldLevel > newLevel) {
            player.sendMessage(ChatColor.GREEN + "Leveled up to Level " + newLevel + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Leveled down to Level " + newLevel + "!");
        }
        this.config.set("level", newLevel);
        recalculatemaxMana();
    }

    /**
     * Recalculates the Maximal mana of a Player
     */
    public void recalculatemaxMana() {
        Integer level = this.config.getInt("level");
        Double maxMana = 100.0 + FairyTailCraft.configuration.getManaPerLevelMultibler();
        for (int i = 0; i < level; i++) {
            maxMana = maxMana + (maxMana * 0.1);
        }
        if (maxMana > FairyTailCraft.configuration.getMaxMana() && FairyTailCraft.configuration.getMaxMana() != -1) {
            maxMana = FairyTailCraft.configuration.getMaxMana().doubleValue();
        }
        int mMana = new Double(Math.round(maxMana)).intValue();
        this.config.set("maxMana", mMana);
        saveConfig();
    }

    /**
     * Get the Current Mana of Player from Config
     * @return current mana of Player
     */
    public Integer getMana() {
        Integer mana = this.config.getInt("mana");
        return mana;
    }

    /**
     * Set mana to Config, Sets maxMana if the Value gets bigger then the maxMana
     * @param value Mana to Set
     */
    public void setMana(Integer value) {
        Integer maxMana = this.config.getInt("maxMana");
        if (value > maxMana) {
            value = maxMana;
        }

        this.config.set("mana", value);
        this.saveConfig();
    }
    
    /**
     * Add mana to Config, Sets maxMana if the Value gets bigger then the maxMana
     * @param value
     */
    public void addMana(Integer value) {
        Integer maxMana = this.config.getInt("maxMana");
        Integer mana = this.config.getInt("mana");
        mana += value;
        if (mana > maxMana) {
            mana = maxMana;
        }

        this.config.set("mana", mana);
        this.saveConfig();
    }
    
    /**
     * Removes mana from Player if he/she has enough
     * @param value mana to remove
     * @return mana was removed (if Player has not anough mana return false)
     */
    public boolean delMana(Integer value) {
        Integer mana = this.config.getInt("mana");
        mana -= value;
        if (mana < 0) {
            return false;
        }

        this.config.set("mana", mana);
        this.saveConfig();
        return true;
    }
    
    /**
     * Deletes the Mana Calculated with the Mananeedmultibler if he/she has enough
     * @param value Mana to Remove
     * @return mana was removed (if Player has not anough mana return false)
     */
    public boolean delCalcMana(Integer value) {
        int level = this.getLevel();
        Double mana = value.doubleValue();
        for(int i = 0; i < level; i++) {
            mana = mana + mana * 0.05;
        }
        return delMana(new Double(Math.round(mana)).intValue());
    }

    /**
     * Returns the Maximal Mana of a Player
     * @return Maximal Mana
     */
    public Integer getmaxMana() {
        Integer maxMana = this.config.getInt("maxMana");
        return maxMana;
    }

    public boolean delCalcManaCheck(Integer value) {
        int level = this.getLevel();
        Double mana = value.doubleValue();
        for(int i = 0; i < level; i++) {
            mana = mana + mana * 0.05;
        }
        Integer imana = this.config.getInt("mana");
        imana -= new Double(Math.round(mana)).intValue();
        if (imana < 0) {
            return false;
        }
        return true;
    }
}
