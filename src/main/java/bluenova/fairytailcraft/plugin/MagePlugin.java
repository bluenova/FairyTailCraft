package bluenova.fairytailcraft.plugin;

import org.bukkit.plugin.PluginManager;

public interface MagePlugin {
    void setPluginManager(MagePluginManager manager);
    void loadPlugin();
    void unloadPlugin();
}
