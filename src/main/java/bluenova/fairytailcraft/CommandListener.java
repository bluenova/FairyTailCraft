package bluenova.fairytailcraft;

import bluenova.fairytailcraft.Util.Util;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Sven
 */
public class CommandListener {

    /**
     * Constructor
     * @param sender sender of Command
     * @param command the Command
     * @param cmd Command as String
     * @param args Parameters of Command
     * @return
     */
    public boolean command(CommandSender sender, Command command, String cmd, String[] args) {
        if (cmd.equals("ftc") || cmd.equals("fairytailcraft") || cmd.equals("ft") || cmd.equals("fairytail")) {
            if (args.length == 0) {
                return this.returnInfo(sender);
            } else if (args[0].equals("learn")) {
                return this.learnMagic(sender, args);
            } else if (args[0].equals("info")) {
                return this.returnMyInfo(sender, args);
            } else if (args[0].equals("cast")) {
                return this.setCastMagic(sender, args);
            } else if (args[0].equals("list")) {
                return returnMagicList(sender);
            } else if (args[0].equals("mymagics")) {
                return returnSenderMagics(sender);
            }
        }
        return false;
    }

    private boolean returnInfo(CommandSender sender) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (hasPermission(sent, "fairytail.general")) {
                sent.sendMessage(ChatColor.YELLOW + "Fairy Tail Mage Plugin");
                sent.sendMessage(ChatColor.YELLOW + "/ft - shows this page");
                sent.sendMessage(ChatColor.YELLOW + "/ft list - lists all MagicTypes");
                if (Util.getPlayerConfig(sent) != null && Util.getPlayerConfig(sent).getMageType().equals("none")) {
                    sent.sendMessage(ChatColor.YELLOW + "/ft learn <magic> - Learns a MagicType");
                }

                if (Util.getPlayerConfig(sent) != null && !Util.getPlayerConfig(sent).getMageType().equals("none")) {
                    sent.sendMessage(ChatColor.YELLOW + "/ft mymagics - Lists your Magics");
                    sent.sendMessage(ChatColor.YELLOW + "/ft cast <magicname> - Sets or Removes Magic to your Hand");
                }
                return true;
            } else {
                return false;
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private boolean hasPermission(Player pl, String perm) {
        if (FairyTailCraft.Permissions != null) {
            if (FairyTailCraft.Permissions.has(pl, perm)) {
                return true;
            } else {
                pl.sendMessage(ChatColor.RED + "No Permission!");
                return false;
            }
        } else {
            if (pl.hasPermission(perm) || pl.isOp()) {
                return true;
            } else {
                pl.sendMessage(ChatColor.RED + "No Permission!");
                return false;
            }
        }
    }

    private boolean learnMagic(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong Parameters!");
            return returnInfo(sender);
        }
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (hasPermission(sent, "fairytail.learn")) {
                if (FairyTailCraft.MagicNames.contains(args[1])) {
                    if (hasPermission(sent, "fairytail." + args[1] + ".learn")) {
                        if (Util.getPlayerConfig(sent).getMageType().equals("none")) {
                            Util.getPlayerConfig(sent).learnMagic(args[1]);
                            sender.sendMessage(ChatColor.GREEN + "Learned " + args[1] + " successfully!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "You have already Learned a Magic!");
                            sender.sendMessage(ChatColor.RED + "You can only have one Magic!");
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Magic " + args[1] + " does not Exist!");
                    return true;
                }
            } else {
                return false;
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private boolean setCastMagic(CommandSender sender, String[] args) {
        if (args.length > 2) {
            sender.sendMessage(ChatColor.RED + "Wrong Parameters!");
            return returnInfo(sender);
        }
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            String magic = Util.getPlayerConfig(sent).getMageType();
            if (hasPermission(sent, "fairytail." + magic.toLowerCase() + ".cast")) {
                if (args.length == 1) {
                    FairyTailCraft.activeMagic.put(sent, null);
                    sender.sendMessage(ChatColor.GREEN + "Removed Magic from your Hand!");
                    return true;
                } else {
                    MageEvent eventIsRegistered = eventIsRegistered(args[1], magic);
                    if (eventIsRegistered != null) {
                        if (eventIsRegistered.minLevel <= Util.getPlayerConfig(sent).getLevel()) {
                            FairyTailCraft.activeMagic.put(sent, args[1]);
                            sender.sendMessage(ChatColor.GREEN + "Set Magic " + args[1] + " to your Hand!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "You must at least be level " + eventIsRegistered.minLevel + "!");
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Magic " + args[1] + " unknown!");
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private MageEvent eventIsRegistered(String name, String MagicType) {
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(name) && FairyTailCraft.registeredEvents.get(i).magicType.equals(MagicType)) {
                return FairyTailCraft.registeredEvents.get(i);
            }
        }
        return null;
    }

    private boolean returnMagicList(CommandSender sender) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (hasPermission(sent, "fairytail.list")) {
                sent.sendMessage(ChatColor.GREEN + "Magics on Server:");
                for (String mag : FairyTailCraft.MagicNames) {
                    sent.sendMessage(ChatColor.YELLOW + "- " + mag);
                }
                return true;
            } else {
                return false;
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private boolean returnSenderMagics(CommandSender sender) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            String magic = Util.getPlayerConfig(sent).getMageType();
            if (!magic.equals("none")) {
                if (hasPermission(sent, "fairytail." + magic + ".list")) {
                    sent.sendMessage(ChatColor.GREEN + "Your Magics:");
                    for (MageEvent ev : FairyTailCraft.registeredEvents) {
                        if (!ev.hidden) {
                            if (ev.minLevel > Util.getPlayerConfig(sent).getLevel()) {
                                sent.sendMessage(ChatColor.YELLOW + "- " + ev.name + " (Level " + ev.minLevel + ")");
                            } else {
                                sent.sendMessage(ChatColor.GRAY + "- " + ev.name + " (Level " + ev.minLevel + ")");
                            }
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You did not Learn any Magic!");
                return false;
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private boolean returnMyInfo(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (args.length > 1) {
                if (hasPermission(sent, "fairytail.info.other")) {
                    Player player = FairyTailCraft.server.getPlayer(args[1]);
                    if (player != null) {
                        PlayerConfig playerConfig = Util.getPlayerConfig(player);
                        sent.sendMessage(ChatColor.GREEN + "Info ueber " + player.getName() + ":");
                        sent.sendMessage(ChatColor.YELLOW + "Magietyp: " + playerConfig.getMageType());
                        if (!playerConfig.getMageType().equals("none")) {
                            sent.sendMessage(ChatColor.YELLOW + "Level: " + playerConfig.getLevel());
                            sent.sendMessage(ChatColor.YELLOW + "EXP: " + playerConfig.getExp());
                            sent.sendMessage(ChatColor.YELLOW + "Mana: " + playerConfig.getMana() + "/" + playerConfig.getMaxMana());
                            sent.sendMessage(ChatColor.YELLOW + "Activespell: " + FairyTailCraft.activeMagic.get(player));
                        }

                    } else {
                        sent.sendMessage(ChatColor.RED + "Spieler " + args[1] + " nicht gefunden!");
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                if (hasPermission(sent, "fairytail.info.my")) {
                    Player player = sent;
                    if (player != null) {
                        PlayerConfig playerConfig = Util.getPlayerConfig(player);
                        sent.sendMessage(ChatColor.GREEN + "Info ueber " + player.getName() + ":");
                        sent.sendMessage(ChatColor.YELLOW + "Magietyp: " + playerConfig.getMageType());
                        if (!playerConfig.getMageType().equals("none")) {
                            sent.sendMessage(ChatColor.YELLOW + "Level: " + playerConfig.getLevel());
                            sent.sendMessage(ChatColor.YELLOW + "EXP: " + playerConfig.getExp());
                            sent.sendMessage(ChatColor.YELLOW + "Mana: " + playerConfig.getMana() + "/" + playerConfig.getMaxMana());
                            sent.sendMessage(ChatColor.YELLOW + "Activespell: " + FairyTailCraft.activeMagic.get(player));
                        }

                    } else {
                        sent.sendMessage(ChatColor.RED + "Spieler " + args[1] + " nicht gefunden!");
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }
}
