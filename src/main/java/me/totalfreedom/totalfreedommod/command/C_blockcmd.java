package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(
        description = "This command is used to mute the command spammers or when u '/stfu' a player it is highly recomended to use this command for messaging spam.", 
        usage = "/<command> <-a | purge | <player>>", 
        aliases = "blockcommands,blockcommand,bc,bcmd"
)
public class C_blockcmd extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equals("purge"))
        {
            FUtil.adminAction(sender.getName(), "Unblocking commands for all OP's", true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                FPlayer playerdata = plugin.pl.getPlayer(player);
                if (playerdata.allCommandsBlocked())
                {
                    counter += 1;
                    playerdata.setCommandsBlocked(false);
                }
            }
            msg("Unblocked commands for " + counter + " players.");
            return true;
        }

        if (args[0].equals("-a"))
        {
            FUtil.adminAction(sender.getName(), "Blocking commands for all OP's", true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                if (isAdmin(player))
                {
                    continue;
                }

                counter += 1;
                plugin.pl.getPlayer(player).setCommandsBlocked(true);
                msg(player, FMsg.F + ChatColor.RED + "Your commands are curently blocked please wait...");
            }

            msg("Blocked commands for " + counter + " players.");
            return true;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(CmdMessages.PLAYER_NOT_FOUND);
            return true;
        }

        if (isAdmin(player))
        {
            msg(ChatColor.YELLOW + player.getName() + ChatColor.RED + " is an Super Admin and they can't have there command's blocked!");
            return true;
        }

        FPlayer playerdata = plugin.pl.getPlayer(player);

        playerdata.setCommandsBlocked(!playerdata.allCommandsBlocked());

        FUtil.adminAction(sender.getName(), (playerdata.allCommandsBlocked() ? "B" : "Unb") + "locking all commands for " + player.getName(), true);
        msg((playerdata.allCommandsBlocked() ? "B" : "Unb") + "locked all commands.");

        return true;
    }
}
