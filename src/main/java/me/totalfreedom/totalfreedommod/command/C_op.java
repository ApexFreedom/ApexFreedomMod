package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.DepreciationAggregator;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Makes a player operator", usage = "/<command> <playername>")
public class C_op extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("everyone"))
        {
            msg(FMsg.F + ChatColor.RED + "Correct usage: /opall");
            return true;
        }

        OfflinePlayer player = null;
        for (Player onlinePlayer : server.getOnlinePlayers())
        {
            if (args[0].equalsIgnoreCase(onlinePlayer.getName()))
            {
                player = onlinePlayer;
            }
        }

        // if the player is not online
        if (player == null)
        {
            if (plugin.al.isAdmin(sender) || senderIsConsole)
            {
                player = DepreciationAggregator.getOfflinePlayer(server, args[0]);
            }
            else
            {
                msg(FMsg.F + ChatColor.RED + "That player is not online.");
                msg("You don't have permissions to OP offline players.", ChatColor.YELLOW);
                return true;
            }
        }

        FUtil.adminAction(ChatColor.BLUE + sender.getName(), "Opping " + player.getName(), false);
        player.setOp(true);

        return true;
    }
}
