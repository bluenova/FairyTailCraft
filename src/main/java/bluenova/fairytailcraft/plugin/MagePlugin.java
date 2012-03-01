package bluenova.fairytailcraft.plugin;

public interface MagePlugin {    
    void setPluginManager(MagePluginManager manager);
    String getMagicName();
    void loadPlugin();
    void unloadPlugin();
}
