package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(
        description = "Reinmagioned AdminChat | Talk privately with other admins. Using <command> itself will toggle AdminChat on and off for all messages.",
        usage = "/<command> [message...]",
        aliases = "o,ac")
public class C_adminchat extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            if (senderIsConsole)
            {
                msg(FMsg.F + ChatColor.RED + "Only OP's in-game can toggle or use this command!");
                return true;
            }

            FPlayer userinfo = plugin.pl.getPlayer(playerSender);
            userinfo.setAdminChat(!userinfo.inAdminChat());
            msg(FMsg.F + ChatColor.RED + "Toggled Admin Chat " + 
                    (userinfo.inAdminChat() ? ChatColor.GREEN + "on" : ChatColor.RED + "off")
                    + ".");
        }
        else
        {
            plugin.cm.adminChat(sender, StringUtils.join(args, " "));
        }

        return true;
    }
}
