package bluenova.fairytailcraft.plugin;

import java.lang.reflect.Method;

public interface MagePluginManager {
    void registerMagic(String name, int minLevel, int requiredMana, Method call);
}
