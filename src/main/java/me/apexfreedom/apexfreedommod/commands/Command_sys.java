package me.apexfreedom.apexfreedommod.commands;

import java.util.Date;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.command.CmdMessages;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.util.ChatUtils;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Ayeeee Fixed again :P", usage = "/<command> <setrank <username> <rank> | <add | remove | suspend> <username>>")
public class Command_sys extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player init = null;
        Admin target = getAdmin(playerSender);
        Player targetPlayer = playerSender;

        switch (args[0])
        {

            case "setrank":
            {
                checkRank(Rank.SYSTEM_ADMIN);

                if (args.length < 3)
                {
                    return false;
                }

                Rank rank = Rank.findRank(args[2]);
                if (rank == null)
                {
                    msg("Unknown rank: " + rank);
                    return true;
                }

                if (!rank.isAtLeast(Rank.SUPER_ADMIN))
                {
                    msg("Rank must be Super Admin or higher.", ChatColor.RED);
                    return true;
                }

                Admin admin = plugin.al.getEntryByName(args[1]);
                if (admin == null)
                {
                    msg("Unknown admin: " + args[1]);
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Setting " + admin.getName() + "'s rank to " + rank.getName(), true);

                admin.setRank(rank);
                plugin.al.save();

                msg("Set " + admin.getName() + "'s rank to " + rank.getName());
                return true;
            }

            case "add":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkRank(Rank.SYSTEM_ADMIN);

                // Player already an admin?
                final Player player = getPlayer(args[1]);
                if (player != null && plugin.al.isAdmin(player))
                {
                    msg("That player is already admin.");
                    return true;
                }

                // Find the old admin entry
                String name = player != null ? player.getName() : args[1];
                Admin admin = null;
                for (Admin loopAdmin : plugin.al.getAllAdmins().values())
                {
                    if (loopAdmin.getName().equalsIgnoreCase(name))
                    {
                        admin = loopAdmin;
                        break;
                    }
                }

                if (admin == null) // New admin
                {
                    if (player == null)
                    {
                        msg(CmdMessages.PLAYER_NOT_FOUND);
                        return true;
                    }

                    FUtil.adminAction(sender.getName(), "Adding " + player.getName() + " to the admin list", true);
                    plugin.al.addAdmin(new Admin(player));
                }
                else // Existing admin
                {
                    FUtil.adminAction(sender.getName(), "Readding " + admin.getName() + " to the admin list", true);

                    if (player != null)
                    {
                        admin.setName(player.getName());
                        admin.addIp(Ips.getIp(player));
                    }

                    admin.setActive(true);
                    admin.setLastLogin(new Date());

                    plugin.al.save();
                    plugin.al.updateTables();
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

                checkRank(Rank.SYSTEM_ADMIN);

                Player player = getPlayer(args[1]);
                Admin admin = player != null ? plugin.al.getAdmin(player) : plugin.al.getEntryByName(args[1]);

                if (admin == null)
                {
                    msg("Admin not found: " + args[1]);
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Removing " + admin.getName() + " from the admin list", true);
                admin.setActive(false);
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }
            
            case "suspend":
            {
                if (args.length < 2)
                {
                    return false;
                }
                
                checkRank(Rank.SYSTEM_ADMIN);
                
                Rank rank = Rank.findRank(args[2]);
                if (rank == null)
                {
                    msg("Unknown rank: " + rank);
                    return true;
                }

                Player player = getPlayer(args[1]);
                Admin admin = player != null ? plugin.al.getAdmin(player) : plugin.al.getEntryByName(args[1]);

                if (admin == null)
                {
                    msg("Admin not found: " + args[1]);
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Suspendending " + admin.getName() + " from the Admin list", true);
                admin.setActive(false);
                plugin.al.save();
                plugin.al.updateTables();
                player.kickPlayer(FMsg.F + ChatColor.RED + "You have been suspended. Check the forums for more information.");
                return true;
            }

            default:
            {
                return false;
            }
        }
    }

}

