package bluenova.fairytailcraft.plugin;

/**
 * Mage Plugin Interface
 * @author Sven
 */
public interface MagePlugin {    
    /**
     * Sets the Plugin Manager
     * @param manager
     */
    void setPluginManager(MagePluginManager manager);
    /**
     * Gets the Magic Name
     * @return
     */
    String getMagicName();
    /**
     * Called on Load Plugin
     */
    void loadPlugin();
    /**
     * Called on Unload Plugin
     */
    void unloadPlugin();
}
