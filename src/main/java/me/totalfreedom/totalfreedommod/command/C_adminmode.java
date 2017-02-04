package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_CONSOLE, blockHostConsole = true)
@CommandParameters(description = "Makes the server only open to Super-Admin's!", usage = "/<command> [on | off]")
public class C_adminmode extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("off"))
        {
            ConfigEntry.ADMIN_ONLY_MODE.setBoolean(false);
            FUtil.adminAction(sender.getName(), "Opening the server to operators!", true);
            return true;
        }
        else if (args[0].equalsIgnoreCase("on"))
        {
            ConfigEntry.ADMIN_ONLY_MODE.setBoolean(true);
            FUtil.adminAction(sender.getName(), "Closing the server only Super-Admins can join!", true);
            for (Player player : server.getOnlinePlayers())
            {
                if (!isAdmin(player))
                {
                    player.kickPlayer(FMsg.F + ChatColor.RED + "The server is only open to Super-Admin's!");
                }
            }
            return true;
        }

        return false;
    }
}
