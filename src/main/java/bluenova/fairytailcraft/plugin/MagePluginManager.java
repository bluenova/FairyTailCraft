package bluenova.fairytailcraft.plugin;

import bluenova.fairytailcraft.event.MageEventType;

public interface MagePluginManager {
    void registerMagic(String name, String magicType, int minLevel, int requiredMana, MagePluginEvent call, MageEventType type);
}
