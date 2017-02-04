package me.apexfreedom.apexfreedommod.main;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FSync;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SystemAdminChatManager extends FreedomService
{

    public SystemAdminChatManager(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    private void handleChatEvent(AsyncPlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        String message = event.getMessage().trim();

        // Check for adminchat
        final FPlayer fplayer = plugin.pl.getPlayerSync(player);
        if (fplayer.inSystemAdminChat())
        {
            FSync.adminChatMessage(player, message);
            event.setCancelled(true);
            return;
        }
    }

    public void systemAdminChat(CommandSender sender, String message)
    {
        String name = sender.getName() + " " + plugin.rm.getDisplay(sender).getColoredTag() + ChatColor.WHITE;
        FLog.info("[SystemAdminChat] " + name + ": " + message);

        for (Player player : server.getOnlinePlayers())
        {
            if (plugin.al.isAdmin(player))
            {
                player.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "SysAdminChat" + ChatColor.GRAY + "] " + ChatColor.DARK_RED + name + ": " + ChatColor.GOLD + message);
            }
        }
    }
}
