package bluenova.fairytailcraft;

import bluenova.fairytailcraft.config.MainConfig;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEvent;
import bluenova.fairytailcraft.event.PlayerEvents;
import bluenova.fairytailcraft.plugin.MagePlugin;
import bluenova.fairytailcraft.plugin.MagePluginManager;
import bluenova.fairytailcraft.plugin.MagePluginManagerImpl;
import bluenova.fairytailcraft.plugin.PluginLoader;
import com.nijiko.permissions.PermissionHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;

/**
 * oreRespawn for Bukkit
 *
 * @author svenbrnn
 */
public class FairyTailCraft extends JavaPlugin {
    
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static MainConfig configuration;
    private List<MagePlugin> plugins;
    public static List<String> MagicNames = new ArrayList<String>();
    public static PermissionHandler Permissions;
    public static List<PlayerConfig> playerConfigs = new ArrayList<PlayerConfig>();
    public static CommandListener command = new CommandListener();
    public static List<MageEvent> registeredEvents = new ArrayList<MageEvent>();
    public static HashMap<Player, String> activeMagic = new HashMap<Player, String>();
    public static PluginManager pm;
    public static Server server;
    
    @Override
    public void onEnable() {
        FairyTailCraft.pm = getServer().getPluginManager();
        FairyTailCraft.server = getServer();
        this.setupPermissions();
        FairyTailCraft.configuration = new MainConfig();
        
        FairyTailCraft.pm.registerEvents(new PlayerEvents(), this);        
        
        this.loadMagePlugins();
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }
    
    private void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (FairyTailCraft.Permissions == null) {
            if (test != null) {
                FairyTailCraft.Permissions = ((Permissions) test).getHandler();
                System.out.println("[FairyTailCraft] Permission system detected!");
            } else {
                System.out.println("[FairyTailCraft] Permission system not detected, defaulting to OP");
            }
        }
    }
    
    @Override
    public void onDisable() {
        this.unloadMagePlugins();
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return FairyTailCraft.command.command(sender, command, label, args);
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
    
    private void loadMagePlugins() {
        try {
             plugins = PluginLoader.loadPlugins(new File("plugins" + File.separator + "FairyTailCraft" + File.separator + "plugins"), this);
        } catch (IOException ex) {
            Logger.getLogger(FairyTailCraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MagePluginManager manager = new MagePluginManagerImpl();
        for (MagePlugin p : plugins) {
            p.setPluginManager(manager);
        }
        for (MagePlugin p : plugins) {
            FairyTailCraft.MagicNames.add(p.getMagicName());
        }
        for (MagePlugin p : plugins) {
            p.loadPlugin();
        }
    }
    
    private void unloadMagePlugins() {
        for (MagePlugin p : plugins) {
            p.unloadPlugin();
        }
    }
}
