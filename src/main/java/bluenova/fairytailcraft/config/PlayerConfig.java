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

public class PlayerConfig {

    private Player player;
    private String fileString;
    private File file;
    private YamlConfiguration config;

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
        this.config.set("level", 0);
        this.config.set("mana", 0);
        this.config.set("maxmana", 0);
        saveConfig();
    }

    private void saveConfig() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isPlayer(Player pl) {
        if (this.player == pl) {
            return true;
        } else {
            return false;
        }
    }

    public String getMageType() {
        String type = this.config.getString("magetype");
        if (type == null) {
            type = "none";
        }
        return type;
    }

    public void learnMagic(String string) {
        this.config.set("magetype", string);
        saveConfig();
    }

    public int getLevel() {
        Integer level = this.config.getInt("level", 0);
        return level;
    }

    public void addExp(int amount) {
        Integer Exp = this.config.getInt("exp", 0);
        Exp += amount;
        this.config.set("exp", Exp);
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setExp(int exp) {
        this.config.set("exp", exp);
        saveConfig();
    }

    public int getExp() {
        Integer Exp = this.config.getInt("exp", 0);
        return Exp;
    }

    public void recalculateLevel() {
        Integer oldLevel = this.config.getInt("level", 0);
        Integer Exp = this.config.getInt("exp", 0);
        double neededExp = 100.00 * FairyTailCraft.configuration.getlevelMultibler();
        Integer newLevel = 1;
        while (neededExp < Exp && newLevel != FairyTailCraft.configuration.getMaxLevel()) {
            newLevel++;
            neededExp = neededExp + (neededExp * 0.08);
        }
        if (oldLevel > newLevel) {
            player.sendMessage(ChatColor.GREEN + "Leveled up to Level " + newLevel + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Leveled down to Level " + newLevel + "!");
        }
        recalculateMaxMana();
    }

    public void recalculateMaxMana() {
        Integer level = this.config.getInt("level", 0);
        Double maxMana = 100.0 + FairyTailCraft.configuration.getManaPerLevelMultibler();
        for (int i = 0; i < level; i++) {
            maxMana = maxMana + (maxMana * 0.1);
        }
        if (maxMana > FairyTailCraft.configuration.getMaxMana()) {
            maxMana = FairyTailCraft.configuration.getMaxMana().doubleValue();
        }
        int mMana = maxMana.intValue();
        this.config.set("maxMana", mMana);
        saveConfig();
    }

    public Integer getMana() {
        Integer mana = this.config.getInt("mana", 0);
        return mana;
    }

    public void setMana(Integer value) {
        Integer maxmana = this.config.getInt("maxmana", 0);
        if (value > maxmana) {
            value = maxmana;
        }

        this.config.set("mana", value);
        this.saveConfig();
    }
    
    public void addMana(Integer value) {
        Integer maxmana = this.config.getInt("maxmana", 0);
        Integer mana = this.config.getInt("mana", 0);
        mana += value;
        if (mana > maxmana) {
            mana = maxmana;
        }

        this.config.set("mana", mana);
        this.saveConfig();
    }
    
    public boolean delMana(Integer value) {
        Integer mana = this.config.getInt("mana", 0);
        mana -= value;
        if (mana < 0) {
            return false;
        }

        this.config.set("mana", mana);
        this.saveConfig();
        return true;
    }
    
    public boolean delCalcMana(Integer value) {
        int level = this.getLevel();
        Double mana = value.doubleValue();
        for(int i = 0; i < level; i++) {
            mana = mana + mana * 0.05;
        }
        return delMana(mana.intValue());
    }
}
