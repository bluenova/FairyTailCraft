package bluenova.fairytailcraft.event;

import bluenova.fairytailcraft.plugin.MagePluginEvent;

/**
 * 
 * @author Sven
 */
public class MageEvent {

    /**
     * Name of Magic
     */
    public String name;
    /**
     * Type of Magic
     */
    public String magicType;
    /**
     * Minimum Level to Cast magic
     */
    public int minLevel;
    /**
     * Mana Required to cast Magic
     */
    public int requiredMana;
    /**
     * The Call Object of the Magic
     */
    public MagePluginEvent call;
    /**
     * The Eventtype of the Magic
     */
    public MageEventType type;
    /**
     * Magic is Hidden from list command?
     */
    public boolean hidden;
    /**
     * Cooldown of Magic
     */
    public Long cooldown;
}
