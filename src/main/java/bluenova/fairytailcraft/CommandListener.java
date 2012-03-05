package bluenova.fairytailcraft;

import bluenova.fairytailcraft.config.PlayerConfig;
import bluenova.fairytailcraft.event.MageEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener {

    public boolean command(CommandSender sender, Command command, String cmd, String[] args) {
        if (cmd.equals("ftc") || cmd.equals("fairytailcraft") || cmd.equals("ft") || cmd.equals("fairytail")) {
            if (args.length == 0) {
                return this.returnInfo(sender);
            } else if (args[0].equals("learn")) {
                return this.learnMagic(sender, args);
            } else if (args[0].equals("cast")) {
                return this.setCastMagic(sender, args);
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
}
