package bluenova.fairytailcraft.event;

import bluenova.fairytailcraft.plugin.MagePluginEvent;

public class MageEvent {

    public String name;
    public String magicType;
    public int minLevel;
    public int requiredMana;
    public MagePluginEvent call;
    public MageEventType type;
}
