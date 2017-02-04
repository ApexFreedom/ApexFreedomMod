package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SYSTEM_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Reinmagioned SystemAdminChat | Talk privately with other system admins. Using <command> itself will toggle AdminChat on and off for all messages.", 
        usage = "/<command> [message...]", aliases = "sac,sc,sysc")
public class Command_systemadminchat extends AF_Command
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            if (senderIsConsole)
            {
                msg(FMsg.F + ChatColor.RED + "Only System Admin's in-game can toggle or use this command!");
                return true;
            }

            FPlayer userinfo = plugin.pl.getPlayer(playerSender);
            userinfo.setSystemAdminChat(!userinfo.inAdminChat());
            msg(FMsg.F + ChatColor.RED + "System Admin Chat is " + 
                    (userinfo.inAdminChat() ? ChatColor.GREEN + "enabled." : ChatColor.RED + "disabled."));
        }
        else
        {
            plugin.scm.systemAdminChat(sender, StringUtils.join(args, " "));
        }

        return true;
    }
}
