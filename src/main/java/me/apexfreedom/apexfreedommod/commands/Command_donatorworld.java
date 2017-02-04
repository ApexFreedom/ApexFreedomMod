package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.command.CmdMessages;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import me.totalfreedom.totalfreedommod.world.WorldTime;
import me.totalfreedom.totalfreedommod.world.WorldWeather;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.DONATOR, source = SourceType.BOTH)
@CommandParameters(description = "Go to the DonatorWorld.",
        usage = "/<command> [guest < list | purge | add <player> | remove <player> > | time <morning | noon | evening | night> | weather <off | on | storm>]")
public class Command_donatorworld extends AF_Command
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
                        adminWorld = plugin.fwm.donatorworld.getWorld();
                    }
                    catch (Exception ex)
                    {
                    }

                    if (adminWorld == null || playerSender.getWorld() == adminWorld)
                    {
                        msg("Going to the main world.");
                        playerSender.teleport(server.getWorlds().get(0).getSpawnLocation());
                    }
                    else
                    {
                        if (plugin.fwm.donatorworld.canAccessWorld(playerSender))
                        {
                            msg("Going to the DonatorWorld.");
                            plugin.fwm.donatorworld.sendToWorld(playerSender);
                        }
                        else
                        {
                            msg("You don't have permission to access the DonatorWorld.");
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
                            msg("AdminWorld guest list: " + plugin.wm.adminworld.guestListToString());
                        }
                        else if ("purge".equalsIgnoreCase(args[1]))
                        {
                            assertCommandPerms(sender, playerSender);
                            plugin.fwm.donatorworld.purgeGuestList();
                            FUtil.adminAction(sender.getName(), "DonatorWorld guest list purged.", false);
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

                            if (plugin.fwm.donatorworld.addGuest(player, playerSender))
                            {
                                FUtil.adminAction(sender.getName(), "AdminWorld guest added: " + player.getName(), false);
                            }
                            else
                            {
                                msg("Could not add player to guest list.");
                            }
                        }
                        else if ("remove".equals(args[1]))
                        {
                            final Player player = plugin.fwm.donatorworld.removeGuest(args[2]);
                            if (player != null)
                            {
                                FUtil.adminAction(sender.getName(), "DonatorWorld guest removed: " + player.getName(), false);
                            }
                            else
                            {
                                msg("Can't find guest entry for: " + args[2]);
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
                            msg("DonatorWorld time set to: " + timeOfDay.name());
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
                            plugin.fwm.donatorworld.setWeatherMode(weatherMode);
                            msg("DonatorWorld weather set to: " + weatherMode.name());
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
