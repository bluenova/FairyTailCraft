package bluenova.fairytailcraft;

import bluenova.fairytailcraft.commands.Util.CommandListener;
import bluenova.fairytailcraft.Util.ManaRegenThread;
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
import java.util.Date;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;

/**
 * oreRespawn for Bukkit
 *
 * @author svenbrnn
 */
public class FairyTailCraft extends JavaPlugin {
    
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    /**
     * Hashmap with ManaRegenthreads
     */
    public static HashMap<Player, ManaRegenThread> manaReg = new HashMap<Player, ManaRegenThread>();
    /**
     * Main Config of Project
     */
    public static MainConfig configuration;
    private List<MagePlugin> plugins;
    /**
     * List of Registered Magic Names
     */
    public static List<String> MagicNames = new ArrayList<String>();
    /**
     * Permission Handler for Permission Plugins
     */
    public static PermissionHandler Permissions;
    /**
     * Hashmap with Configs for Online Players
     */
    public static List<PlayerConfig> playerConfigs = new ArrayList<PlayerConfig>();
    /**
     * Listener Object For commands
     */
    public static CommandListener command;
    /**
     * List of Registered MageEvents
     */
    public static List<MageEvent> registeredEvents = new ArrayList<MageEvent>();
    /**
     * Hashmap with Active Magics of the Online Players
     */
    public static HashMap<Player, String[]> activeMagic = new HashMap<Player, String[]>();
    /**
     * The Plugin Manager of the Bukkit Server
     */
    public static PluginManager pm;
    /**
     * The Bukkit Server
     */
    public static Server server;
    /**
     * Player owned Monsers
     */
    public static HashMap<Player, List<LivingEntity>> ownedLivingEntitys = new HashMap<Player, List<LivingEntity>>();
    
    /**
     * Cooldowns of Players
     */
    public static HashMap<Player,HashMap<String,Date>> playerCDs = new HashMap<Player, HashMap<String,Date>>();
    
    public static Plugin plugin;
    
    @Override
    public void onEnable() {
        FairyTailCraft.plugin = this;
        FairyTailCraft.pm = getServer().getPluginManager();
        FairyTailCraft.server = getServer();
        this.setupPermissions();
        FairyTailCraft.configuration = new MainConfig();
        FairyTailCraft.command = new CommandListener();
                
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
    
    /**
     * 
     */
    @Override
    public void onDisable() {
        this.unloadMagePlugins();
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " disabled!");
    }
    
    /**
     * Called on Command
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
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
            if(!FairyTailCraft.MagicNames.contains((p.getMagicName()))) {
                FairyTailCraft.MagicNames.add(p.getMagicName()); 
                p.loadPlugin();
            } else {
                System.out.println("Magic "+ p.getMagicName() + " already load! Ignoring it!");
            }
        }
    }
    
    private void unloadMagePlugins() {
        for (MagePlugin p : plugins) {
            p.unloadPlugin();
        }
    }
}
