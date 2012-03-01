package bluenova.fairytailcraft.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.fileString = "plugins"+File.separator+"FairyTailCraft"+File.separator+"players"+File.separator+this.player.getName() + ".yml";
        this.file = new File(this.fileString);
        this.config = new YamlConfiguration();
        if(!this.file.exists()) {
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
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isPlayer(Player pl) {
        if(this.player == pl) {
            return true;
        } else {
            return false;
        }
    }

    public String getMageType() {
        String type = this.config.getString("magetype");
        if(type == null)
            type = "none";
        return type;
    }

    public void learnMagic(String string) {
        this.config.set("magetype", string);
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(PlayerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
