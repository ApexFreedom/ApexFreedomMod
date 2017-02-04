package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import me.totalfreedom.totalfreedommod.util.FUtil;
import me.totalfreedom.totalfreedommod.world.WorldTime;
import me.totalfreedom.totalfreedommod.world.WorldWeather;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Go to the AdminWorld.",
        usage = "/<command> [guest < list | purge | add <player> | remove <player> > | time <morning | noon | evening | night> | weather <off | on | storm>]")
public class C_adminworld extends FreedomCommand
{

    private enum CommandMode
    {

        TELEPORT, GUEST, TIME, WEATHER;
    }

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        CommandMode commandMode = null;

        if (args.length == 0)
        {
            commandMode = CommandMode.TELEPORT;
        }
        else if (args.length >= 2)
        {
            if ("guest".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.GUEST;
            }
            else if ("time".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.TIME;
            }
            else if ("weather".equalsIgnoreCase(args[0]))
            {
                commandMode = CommandMode.WEATHER;
            }
        }

        if (commandMode == null)
        {
            return false;
        }

        try
        {
            switch (commandMode)
            {
                case TELEPORT:
                {
                    if (!(sender instanceof Player) || playerSender == null)
                    {
                        return true;
                    }

                    World adminWorld = null;
                    try
                    {
                        adminWorld = plugin.wm.adminworld.getWorld();
                    }
                    catch (Exception ex)
                    {
                    }

                    if (adminWorld == null || playerSender.getWorld() == adminWorld)
                    {
                        msg(FMsg.F + ChatColor.RED + "Welcome to the Main-World!");
                        playerSender.teleport(server.getWorlds().get(0).getSpawnLocation());
                    }
                    else
                    {
                        if (plugin.wm.adminworld.canAccessWorld(playerSender))
                        {
                            msg(FMsg.F + ChatColor.RED + "Welcome to the Admin-World: 'A World only made for the Admins!'");
                            plugin.wm.adminworld.sendToWorld(playerSender);
                        }
                        else
                        {
                            msg(FMsg.F + ChatColor.RED + "You have no access to travel to the AdminWorld: 'Apply for Admin to get access to the AdminWold'");
                        }
                    }

                    break;
                }
                case GUEST:
                {
                    if (args.length == 2)
                    {
                        if ("list".equalsIgnoreCase(args[1]))
                        {
                            msg(ChatColor.RED + "[AW] Guest List: " + ChatColor.YELLOW +  plugin.wm.adminworld.guestListToString());
                        }
                        else if ("purge".equalsIgnoreCase(args[1]))
                        {
                            assertCommandPerms(sender, playerSender);
                            plugin.wm.adminworld.purgeGuestList();
                            FUtil.adminAction(sender.getName(), "Purging the AdminWorld GuestList!", false);
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (args.length == 3)
                    {
                        assertCommandPerms(sender, playerSender);

                        if ("add".equalsIgnoreCase(args[1]))
                        {
                            final Player player = getPlayer(args[2]);

                            if (player == null)
                            {
                                sender.sendMessage(CmdMessages.PLAYER_NOT_FOUND);
                                return true;
                            }

                            if (plugin.wm.adminworld.addGuest(player, playerSender))
                            {
                                FUtil.adminAction(sender.getName(), "Adding a AdminWorld Guest: " + player.getName(), false);
                            }
                            else
                            {
                                msg(FMsg.F + ChatColor.RED + "Guest can not be added!");
                            }
                        }
                        else if ("remove".equals(args[1]))
                        {
                            final Player player = plugin.wm.adminworld.removeGuest(args[2]);
                            if (player != null)
                            {
                                FUtil.adminAction(sender.getName(), "Removing a AdminWorld Guest: " + player.getName(), false);
                            }
                            else
                            {
                                msg(ChatColor.RED + "Can't find guest entry for: " + ChatColor.YELLOW + args[2]);
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }

                    break;
                }
                case TIME:
                {
                    assertCommandPerms(sender, playerSender);

                    if (args.length == 2)
                    {
                        WorldTime timeOfDay = WorldTime.getByAlias(args[1]);
                        if (timeOfDay != null)
                        {
                            plugin.wm.adminworld.setTimeOfDay(timeOfDay);
                            msg("AdminWorld time set to: " + timeOfDay.name());
                        }
                        else
                        {
                            msg("Invalid time of day. Can be: sunrise, noon, sunset, midnight");
                        }
                    }
                    else
                    {
                        return false;
                    }

                    break;
                }
                case WEATHER:
                {
                    assertCommandPerms(sender, playerSender);

                    if (args.length == 2)
                    {
                        WorldWeather weatherMode = WorldWeather.getByAlias(args[1]);
                        if (weatherMode != null)
                        {
                            plugin.wm.adminworld.setWeatherMode(weatherMode);
                            msg("AdminWorld weather set to: " + weatherMode.name());
                        }
                        else
                        {
                            msg("Invalid weather mode. Can be: off, rain, storm");
                        }
                    }
                    else
                    {
                        return false;
                    }

                    break;
                }
                default:
                {
                    return false;
                }
            }
        }
        catch (PermissionDeniedException ex)
        {
            if (ex.getMessage().isEmpty())
            {
                return noPerms();
            }
            sender.sendMessage(ex.getMessage());
            return true;
        }

        return true;
    }

    // TODO: Redo this properly
    private void assertCommandPerms(CommandSender sender, Player playerSender) throws PermissionDeniedException
    {
        if (!(sender instanceof Player) || playerSender == null || !isAdmin(sender))
        {
            throw new PermissionDeniedException();
        }
    }

    private class PermissionDeniedException extends Exception
    {

        private static final long serialVersionUID = 1L;

        private PermissionDeniedException()
        {
            super("");
        }

        private PermissionDeniedException(String string)
        {
            super(string);
        }
    }

}
