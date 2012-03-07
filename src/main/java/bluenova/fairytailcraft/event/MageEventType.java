package bluenova.fairytailcraft.event;


/**
 * 
 * @author Sven
 */
public enum MageEventType {
    /**
     * On Player Interact with something
     */
    INTERACT,
    /**
     * On Player get Hit by Projectile
     */
    GETHITBYPROJECTILE,
    /**
     * on Player Death
     */
    DEATH,
    /**
     * on Player is fishing
     */
    FISH,
    /**
     * on Player Velocity change
     */
    VELOCITY,
    /**
     * On Player Inventory Event
     */
    @Deprecated
    INVENTORY,
    /**
     * On Player Itemheld Event
     */
    ITEMHELD,
    /**
     * on Entity get Damage by Entyty event
     */
    GETDMGBYENTITY
}
