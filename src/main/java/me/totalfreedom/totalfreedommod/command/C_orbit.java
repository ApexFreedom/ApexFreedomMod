package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "POW!!! Right in the kisser! One of these days Alice, straight to the Moon!",
        usage = "/<command> <target> [<<power> | stop>]")
public class C_orbit extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(CmdMessages.PLAYER_NOT_FOUND, ChatColor.RED);
            return true;
        }

        FPlayer playerdata = plugin.pl.getPlayer(player);

        double strength = 10.0;

        if (args.length >= 2)
        {
            if (args[1].equals("stop"))
            {
                msg("Stopped orbiting " + player.getName());
                playerdata.stopOrbiting();
                return true;
            }

            try
            {
                strength = Math.max(1.0, Math.min(150.0, Double.parseDouble(args[1])));
            }
            catch (NumberFormatException ex)
            {
                msg(ex.getMessage(), ChatColor.RED);
                return true;
            }
        }

        player.setGameMode(GameMode.SURVIVAL);
        playerdata.startOrbiting(strength);

        player.setVelocity(new Vector(0, strength, 0));
        FUtil.adminAction(sender.getName(), "Orbiting " + player.getName(), false);
        msg(player, ChatColor.RED + "JOHN CENA SMAHED YOU IN THE AIR!!!!");
        msg(player, ChatColor.RED + "You have done fucked up now");
        msg(player, ChatColor.RED + "You are now orbiting in mid air");
        FUtil.adminAction(sender.getName(), "Welcome to Hell " + player.getName(), false);
        
        return true;
    }
}
