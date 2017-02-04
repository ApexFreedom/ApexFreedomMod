package me.apexfreedom.apexfreedommod.commands;

import java.util.Date;
import me.apexfreedom.apexfreedommod.donator.Donator;
import me.totalfreedom.totalfreedommod.command.CmdMessages;
import me.totalfreedom.totalfreedommod.command.FreedomCommand;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_CONSOLE)
@CommandParameters(
description = "Freedom >> Donator system not finished will be explaned why its hear and what is it's used for later on hint (v3.2)",
usage = "/<command> [list, reload] <add remove info [playername]> <setrank [username] [rank]>"
)
public class Command_donator extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        switch (args[0])
        {
            case "list":
            {
                msg("DonatorList: " + StringUtils.join(plugin.dl.getDonatorNames(), ", "), ChatColor.GOLD);

                return true;
            }

            case "reload":
            {
                checkRank(Rank.SUPER_ADMIN);

                FUtil.adminAction(sender.getName(), "Reloading the Donator list", true);
                plugin.dl.load();
                msg("Donator list reloaded!");
                return true;
            }

            case "info":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkRank(Rank.SUPER_ADMIN);

                Donator donator = plugin.dl.getEntryByName(args[1]);

                if (donator == null)
                {
                    final Player player = getPlayer(args[1]);
                    if (player != null)
                    {
                        donator = plugin.dl.getDonator(player);
                    }
                }

                if (donator == null)
                {
                    msg("Donator not found: " + args[1]);
                }
                else
                {
                    msg(donator.toString());
                }

                return true;
            }

            case "add":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkConsole();
                checkRank(Rank.TELNET_ADMIN);
                
                final Player player = getPlayer(args[1]);
                if (player != null && plugin.dl.isDonator(player))
                {
                    msg("That player is already a donator.");
                    return true;
                }

                String name = player != null ? player.getName() : args[1];
                Donator donator = null;
                for (Donator loopDonator : plugin.dl.getAllDonators().values())
                {
                    if (loopDonator.getName().equalsIgnoreCase(name))
                    {
                        donator = loopDonator;
                        break;
                    }
                }

                if (donator == null)
                {
                    if (player == null)
                    {
                        msg(CmdMessages.PLAYER_NOT_FOUND);
                        return true;
                    }

                    FUtil.adminAction(sender.getName(), "Adding " + player.getName() + " to the donator list", true);
                    plugin.dl.addDonator(new Donator(player));
                }
                else
                {
                    FUtil.adminAction(sender.getName(), "Readding " + donator.getName() + " to the donator list", true);

                    if (player != null)
                    {
                        donator.setName(player.getName());
                        donator.addIp(Ips.getIp(player));
                    }

                    donator.setActive(true);
                    donator.setLastLogin(new Date());

                    plugin.dl.save();
                    plugin.dl.updateTables();
                }

                if (player != null)
                {
                    final FPlayer fPlayer = plugin.pl.getPlayer(player);
                    if (fPlayer.getFreezeData().isFrozen())
                    {
                        fPlayer.getFreezeData().setFrozen(false);
                        msg(player.getPlayer(), "You have been unfrozen.");
                    }
                }

                return true;
            }

            case "remove":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkConsole();
                checkRank(Rank.TELNET_ADMIN);

                Player player = getPlayer(args[1]);
                Donator donator = player != null ? plugin.dl.getDonator(player) : plugin.dl.getEntryByName(args[1]);

                if (donator == null)
                {
                    msg("Donator not found: " + args[1]);
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Removing " + donator.getName() + " from the donator list", true);
                donator.setActive(false);
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }

            default:
            {
                return false;
            }
        }
    }

}
