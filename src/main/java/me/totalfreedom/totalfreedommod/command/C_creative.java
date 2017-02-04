package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Quickly change your own gamemode to creative, or define someone's username to change theirs.",
        usage = "/<command> <-a | [partialname]>",
        aliases = "gmc,gmcreative")
public class C_creative extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            if (isConsole())
            {
                sender.sendMessage("When used from the console, you must define a target player.");
                return true;
            }

            playerSender.setGameMode(GameMode.CREATIVE);
            msg(FMsg.F + ChatColor.RED + "You GameMode is set to 'Creative'");
            return true;
        }

        checkRank(Rank.SUPER_ADMIN);

        if (args[0].equals("-a"))
        {
            for (Player targetPlayer : server.getOnlinePlayers())
            {
                targetPlayer.setGameMode(GameMode.CREATIVE);
            }

            FUtil.adminAction(sender.getName(), "Changing all players gamemode to gmc", false);
            return true;
        }

        Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(CmdMessages.PLAYER_NOT_FOUND);
            return true;
        }

        msg("Setting " + player.getName() + " to game mode creative");
        msg(player, sender.getName() + " set your game mode to creative");
        player.setGameMode(GameMode.CREATIVE);

        return true;
    }
}
