package bluenova.fairytailcraft.commands.Util;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.commands.BasicCommands;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.plugin.MagePlugin;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Sven
 */
public class CommandListener {

    /*
     * Classes with Commandmethodes for CommandListener 
     */
    private List<Class<?>> commandClasses = new ArrayList<Class<?>>();
    /*
     * Classes registered by Plugin
     */
    private HashMap<Class<?>, MagePlugin> classesByPlugin = new HashMap<Class<?>, MagePlugin>();
    
    public CommandListener() {
        this.registerClass(BasicCommands.class, null);
    }

    /**
     * Constructor
     * @param sender sender of Command
     * @param command the Command
     * @param cmd Command as String
     * @param args Parameters of Command
     * @return
     */
    public boolean command(CommandSender sender, Command command, String cmd, String[] args) {
        bluenova.fairytailcraft.commands.Util.Command annotation;
        for (Class<?> cls : commandClasses) {
            MagePlugin plug = this.classesByPlugin.get(cls);
            if (plug != null) {
                if (sender instanceof Player) {
                    Player sent = (Player) sender;
                    PlayerConfig playerConfig = bluenova.fairytailcraft.Util.Util.getPlayerConfig(sent);
                    if (!plug.getMagicName().equals(playerConfig.getMageType())) {
                        continue;
                    }
                } else {
                     continue;
                }
            }
            Method[] methods = cls.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(bluenova.fairytailcraft.commands.Util.Command.class)) {
                    annotation = m.getAnnotation(bluenova.fairytailcraft.commands.Util.Command.class);
                    Object newInstance = null;
                    String firstArg = "";
                    if (args.length > 0) {
                        firstArg = args[0];
                    }
                    List<String> fst = new ArrayList<String>();
                    for (String s : annotation.firstArg()) {
                        fst.add(s);
                    }
                    if (fst.contains(firstArg)) {
                        try {
                            cls.getConstructor();
                            newInstance = cls.newInstance();
                        } catch (NoSuchMethodException ex) {
                            Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (newInstance != null) {
                            try {
                                Object invoke = m.invoke(newInstance, (CommandSender) sender, cmd, args);
                                if (invoke instanceof Boolean) {
                                    if ((Boolean) invoke == true) {
                                        return (Boolean) invoke;
                                    }
                                }
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean registerClass(Class<?> cls, MagePlugin byPlugin) {
        if (this.commandClasses.contains(cls)) {
            return false;
        }
        if (registerToCommandMap(cls)) {
            this.classesByPlugin.put(cls, byPlugin);
            return true;
        }
        return false;
    }
    
    private boolean registerToCommandMap(Class<?> cls) {
        CommandMap commandMap = getField(FairyTailCraft.pm, "commandMap");
        Method[] methods = cls.getMethods();
        boolean register = false;
        for (Method m : methods) {
            if (m.isAnnotationPresent(bluenova.fairytailcraft.commands.Util.Command.class)) {
                bluenova.fairytailcraft.commands.Util.Command ann = m.getAnnotation(bluenova.fairytailcraft.commands.Util.Command.class);
                if (commandMap != null) {
                    DynamicPluginCommand cmd = new DynamicPluginCommand(ann.cmd(), "", "/" + ann.cmd()[0], FairyTailCraft.plugin, null);
                    commandMap.register(FairyTailCraft.plugin.getDescription().getName(), cmd);
                    this.commandClasses.add(cls);
                    register = true;
                } else {
                    System.out.println("CommandMap is Empty");
                }
            }
        }
        return register;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            }
        } while (checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }
}
