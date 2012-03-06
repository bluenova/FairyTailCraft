package bluenova.fairytailcraft.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig {

    private String fileString;
    private File file;
    private YamlConfiguration config;

    public MainConfig() {
        this.fileString = "plugins" + File.separator + "FairyTailCraft" + File.separator + "config.yml";
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
        System.out.println("[FairyTailCraft] config " + this.fileString + " Load!");
    }

    private void createConfig() {
        this.config.set("config.configVersion", "0.1");
        this.config.set("config.main.magicUnique", true);
        this.config.set("config.level.startLevel", 1);
        this.config.set("config.level.levelMultibler", 1.0);
        this.config.set("config.level.maxlevel", -1);
        this.config.set("config.mana.maxMana", -1);
        this.config.set("config.mana.manaNeedMultibler", 1.0);
        this.config.set("config.mana.manaPerLevelMultibler", 1.0);
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(MainConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Double getlevelMultibler() {
        return this.config.getDouble("config.level.levelMultibler");
    }

    public Integer getMaxLevel() {
        return this.config.getInt("config.level.maxlevel");
    }

    public Integer getStartLevel() {
        return this.config.getInt("config.level.startLevel");
    }

    public Double getManaPerLevelMultibler() {
        return this.config.getDouble("config.mana.manaPerLevelMultibler");
    }

    public Double getManaNeedMultibler() {
        return this.config.getDouble("config.mana.manaNeedMultibler");
    }

    public Integer getMaxMana() {
        return this.config.getInt("config.mana.maxMana");
    }
}
