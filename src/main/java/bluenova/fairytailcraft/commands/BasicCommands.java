/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.commands;

import bluenova.fairytailcraft.FairyTailCraft;
import bluenova.fairytailcraft.commands.Util.Command;
import bluenova.fairytailcraft.Util.Util;
import bluenova.fairytailcraft.commands.Util.CommandListener;
import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEvent;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Sven
 */
public class BasicCommands {

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"", "help"})
    public boolean returnInfo(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (Util.hasPermission(sent, "fairytail.general")) {
                sent.sendMessage(ChatColor.YELLOW + "Fairy Tail Mage Plugin");
                sent.sendMessage(ChatColor.YELLOW + "/ft - shows this page");
                sent.sendMessage(ChatColor.YELLOW + "/ft list - lists all MagicTypes");
                sent.sendMessage(ChatColor.YELLOW + "/ft info [player] - Shows info about you [or an other player]");
                if (Util.getPlayerConfig(sent) != null && Util.getPlayerConfig(sent).getMageType().equals("none")) {
                    sent.sendMessage(ChatColor.YELLOW + "/ft learn <magic> - Learns a MagicType");
                }

                if (Util.getPlayerConfig(sent) != null && !Util.getPlayerConfig(sent).getMageType().equals("none")) {
                    sent.sendMessage(ChatColor.YELLOW + "/ft unlearn - Unlearns your Magic - You get back to Zero");
                    sent.sendMessage(ChatColor.YELLOW + "/ft mymagics - Lists your Magics");
                    sent.sendMessage(ChatColor.YELLOW + "/ft cast <magicname> - Sets or Removes Magic to your Hand");
                    List<String> plgInfos = CommandListener.commandInfos.get(Util.getPlayerConfig(sent).getMageType());
                    if (plgInfos != null) {
                        for (String info : plgInfos) {
                            sent.sendMessage(ChatColor.YELLOW + info);
                        }
                    }
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

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"learn"})
    public boolean learnMagic(CommandSender sender, String cmd, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong Parameters!");
            return returnInfo(sender, cmd, args);
        }
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (Util.hasPermission(sent, "fairytail.learn")) {
                if (FairyTailCraft.MagicNames.contains(args[1])) {
                    if (Util.hasPermission(sent, "fairytail." + args[1] + ".learn")) {
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

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"unlearn"})
    public boolean unlearnMagic(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (Util.hasPermission(sent, "fairytail.unlearn")) {
                if (!Util.getPlayerConfig(sent).getMageType().equals("none")) {
                    Util.getPlayerConfig(sent).resetConfig();
                    sender.sendMessage(ChatColor.GREEN + "Unlearning successfully! You are Back to Zero!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "You dont have a Magic!");
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

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"cast"})
    public boolean setCastMagic(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            String magic = Util.getPlayerConfig(sent).getMageType();
            if (Util.hasPermission(sent, "fairytail." + magic.toLowerCase() + ".cast")) {
                if (args.length == 1) {
                    FairyTailCraft.activeMagic.put(sent, null);
                    sender.sendMessage(ChatColor.GREEN + "Removed Magic from your Hand!");
                    return true;
                } else {
                    MageEvent eventIsRegistered = eventIsRegistered(args[1], magic);
                    if (eventIsRegistered != null) {
                        if (eventIsRegistered.minLevel <= Util.getPlayerConfig(sent).getLevel()) {
                            String[] cast;
                            if (args.length > 2) {
                                Integer valInt = 1;
                                try {
                                    valInt = new Integer(args[2]);
                                } catch (Exception e) {
                                }
                                String val = valInt.toString();
                                cast = new String[]{
                                    args[1],
                                    val
                                };
                            } else {
                                cast = new String[]{
                                    args[1],
                                    "1"
                                };
                            }
                            FairyTailCraft.activeMagic.put(sent, cast);
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

    public MageEvent eventIsRegistered(String name, String MagicType) {
        for (int i = 0; i < FairyTailCraft.registeredEvents.size(); i++) {
            if (FairyTailCraft.registeredEvents.get(i).name.equals(name) && FairyTailCraft.registeredEvents.get(i).magicType.equals(MagicType)) {
                return FairyTailCraft.registeredEvents.get(i);
            }
        }
        return null;
    }

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"list"})
    public boolean returnMagicList(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (Util.hasPermission(sent, "fairytail.list")) {
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

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"mymagics"})
    public boolean returnSenderMagics(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            String magic = Util.getPlayerConfig(sent).getMageType();
            if (!magic.equals("none")) {
                if (Util.hasPermission(sent, "fairytail." + magic + ".list")) {
                    sent.sendMessage(ChatColor.GREEN + "Your Magics:");
                    for (MageEvent ev : FairyTailCraft.registeredEvents) {
                        if (!ev.hidden) {
                            if (ev.magicType.equals(magic)) {
                                if (ev.minLevel > Util.getPlayerConfig(sent).getLevel()) {
                                    sent.sendMessage(ChatColor.YELLOW + "- " + ev.name + " (Level " + ev.minLevel + ")");
                                } else {
                                    sent.sendMessage(ChatColor.GRAY + "- " + ev.name + " (Level " + ev.minLevel + ")");
                                }
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

    @Command(cmd = {"ftc", "fairytailcraft", "ft", "fairytail"},
    firstArg = {"info"})
    public boolean returnMyInfo(CommandSender sender, String cmd, String[] args) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (args.length > 1) {
                if (Util.hasPermission(sent, "fairytail.info.other")) {
                    Player player = FairyTailCraft.server.getPlayer(args[1]);
                    if (player != null) {
                        PlayerConfig playerConfig = Util.getPlayerConfig(player);
                        sent.sendMessage(ChatColor.GREEN + "Info ueber " + player.getName() + ":");
                        sent.sendMessage(ChatColor.YELLOW + "Magietyp: " + playerConfig.getMageType());
                        if (!playerConfig.getMageType().equals("none")) {
                            sent.sendMessage(ChatColor.YELLOW + "Level: " + playerConfig.getLevel());
                            sent.sendMessage(ChatColor.YELLOW + "EXP: " + playerConfig.getExp());
                            sent.sendMessage(ChatColor.YELLOW + "Mana: " + playerConfig.getMana() + "/" + playerConfig.getmaxMana());
                            String[] get = FairyTailCraft.activeMagic.get(player);
                            String val = "none";
                            if(get != null)
                                val = get[1];
                            sent.sendMessage(ChatColor.YELLOW + "Activespell: " + val);
                        }

                    } else {
                        sent.sendMessage(ChatColor.RED + "Spieler " + args[1] + " nicht gefunden!");
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                if (Util.hasPermission(sent, "fairytail.info.my")) {
                    Player player = sent;
                    if (player != null) {
                        PlayerConfig playerConfig = Util.getPlayerConfig(player);
                        sent.sendMessage(ChatColor.GREEN + "Info ueber " + player.getName() + ":");
                        sent.sendMessage(ChatColor.YELLOW + "Magietyp: " + playerConfig.getMageType());
                        if (!playerConfig.getMageType().equals("none")) {
                            sent.sendMessage(ChatColor.YELLOW + "Level: " + playerConfig.getLevel());
                            sent.sendMessage(ChatColor.YELLOW + "EXP: " + playerConfig.getExp());
                            sent.sendMessage(ChatColor.YELLOW + "Mana: " + playerConfig.getMana() + "/" + playerConfig.getmaxMana());
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
