package bluenova.fairytailcraft;

import com.nijiko.permissions.PermissionHandler;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * oreRespawn for Bukkit
 *
 * @author svenbrnn
 */
public class FairyTailCraft extends JavaPlugin {

    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    private PermissionHandler Permissions;

    @Override
    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events
        // Register our events

        PluginManager pm = getServer().getPluginManager();
        setupPermissions();

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    private void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (this.Permissions == null) {
            if (test != null) {
                this.Permissions = ((Permissions)test).getHandler();
                System.out.println("[OreRespawn] Permission system detected!");
            } else {
                System.out.println("[OreRespawn] Permission system not detected, defaulting to OP");
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
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}
