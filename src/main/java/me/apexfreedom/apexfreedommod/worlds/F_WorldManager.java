package me.apexfreedom.apexfreedommod.worlds;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import static me.totalfreedom.totalfreedommod.util.FUtil.playerMsg;
import me.totalfreedom.totalfreedommod.world.WorldWeather;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class F_WorldManager extends FreedomService
{

    public DonatorWorld donatorworld;

    public F_WorldManager(TotalFreedomMod plugin)
    {
        super(plugin);

        this.donatorworld = new DonatorWorld();
    }

    @Override
    protected void onStart()
    {
        donatorworld.getWorld();

        // Disable weather
        if (ConfigEntry.DISABLE_WEATHER.getBoolean())
        {
            for (World world : server.getWorlds())
            {
                world.setThundering(false);
                world.setStorm(false);
                world.setThunderDuration(0);
                world.setWeatherDuration(0);
            }
        }
    }

    @Override
    protected void onStop()
    {
        donatorworld.getWorld().save();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        final Player player = event.getPlayer();
        final FPlayer fPlayer = plugin.pl.getPlayer(player);

        if (!plugin.dl.isDonator(player) && fPlayer.getFreezeData().isFrozen())
        {
            return;
        }

        donatorworld.validateMovement(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        final Location from = event.getFrom();
        final Location to = event.getTo();

        try
        {
            if (from.getWorld() == to.getWorld() && from.distanceSquared(to) < (0.0002 * 0.0002))
            {
                // If player just rotated, but didn't move, don't process this event.
                return;
            }
        }
        catch (IllegalArgumentException ex)
        {
        }

        donatorworld.validateMovement(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onThunderChange(ThunderChangeEvent event)
    {
        try
        {
            if (event.getWorld().equals(donatorworld.getWorld()) && donatorworld.getWeatherMode() != WorldWeather.OFF)
            {
                return;
            }
        }
        catch (Exception ex)
        {
        }

        if (ConfigEntry.DISABLE_WEATHER.getBoolean() && event.toThunderState())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event)
    {
        try
        {
            if (event.getWorld().equals(donatorworld.getWorld()) && donatorworld.getWeatherMode() != WorldWeather.OFF)
            {
                return;
            }
        }
        catch (Exception ex)
        {
        }

        if (ConfigEntry.DISABLE_WEATHER.getBoolean() && event.toWeatherState())
        {
            event.setCancelled(true);
        }
    }

    public void gotoWorld(Player player, String targetWorld)
    {
        if (player == null)
        {
            return;
        }

        if (player.getWorld().getName().equalsIgnoreCase(targetWorld))
        {
            playerMsg(player, "Going to main world.", ChatColor.GRAY);
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            return;
        }

        for (World world : Bukkit.getWorlds())
        {
            if (world.getName().equalsIgnoreCase(targetWorld))
            {
                playerMsg(player, "Going to world: " + targetWorld, ChatColor.GRAY);
                player.teleport(world.getSpawnLocation());
                return;
            }
        }

        playerMsg(player, "World " + targetWorld + " not found.", ChatColor.GRAY);
    }

}
