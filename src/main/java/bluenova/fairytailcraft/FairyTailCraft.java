package bluenova.fairytailcraft;

import bluenova.fairytailcraft.config.MainConfig;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.PlayerEvents;
import com.nijiko.permissions.PermissionHandler;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.ArrayList;
import java.util.List;

/**
 * oreRespawn for Bukkit
 *
 * @author svenbrnn
 */
public class FairyTailCraft extends JavaPlugin {

    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    private PermissionHandler Permissions;
    private MainConfig configuration;
    public static List<PlayerConfig> playerConfigs = new ArrayList<PlayerConfig>();

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        this.setupPermissions();
        this.configuration = new MainConfig();

        pm.registerEvents(new PlayerEvents(), this);
    
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    private void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (this.Permissions == null) {
            if (test != null) {
                this.Permissions = ((Permissions)test).getHandler();
                System.out.println("[FairyTailCraft] Permission system detected!");
            } else {
                System.out.println("[FairyTailCraft] Permission system not detected, defaulting to OP");
            }
        }
    }
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " disabled!");
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return this.debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        this.debugees.put(player, value);
    }
}
