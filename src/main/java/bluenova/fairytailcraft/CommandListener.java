package bluenova.fairytailcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener {

    public boolean command(CommandSender sender, Command command, String cmd, String[] args) {
        if (cmd.equals("ftc") || cmd.equals("fairytailcraft") || cmd.equals("ft") || cmd.equals("fairytail")) {
            if (args.length == 0) {
                return this.returnInfo(sender);
            }
        }
        return false;
    }

    private boolean returnInfo(CommandSender sender) {
        if (sender instanceof Player) {
            Player sent = (Player) sender;
            if (FairyTailCraft.Permissions != null) {
                if (FairyTailCraft.Permissions.has(sent, "fairytail.general")) {
                    returnInfoText(sent);
                    return true;
                } else {
                    sent.sendMessage(ChatColor.RED + "No Permission!");
                    return false;
                }
            } else {

                if (sent.hasPermission("fairytail.general") || sent.isOp()) {
                    returnInfoText(sent);
                    return true;
                } else {
                    sent.sendMessage(ChatColor.RED + "No Permission!");
                    return false;
                }
            }
        } else {
            sender.sendMessage("Don't run this Command from Console!");
            return true;
        }
    }

    private void returnInfoText(Player sent) {
        sent.sendMessage(ChatColor.YELLOW + "Fairy Tail Mage Plugin");
        sent.sendMessage(ChatColor.YELLOW + "/ft - shows this page");
    }
}
