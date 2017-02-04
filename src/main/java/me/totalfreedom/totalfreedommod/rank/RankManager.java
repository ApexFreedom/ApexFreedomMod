package me.totalfreedom.totalfreedommod.rank;

// TotalFreedomMod
import me.apexfreedom.apexfreedommod.donator.Donator;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.FUtil;

// Misc
import net.pravian.aero.util.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankManager extends FreedomService
{

    public RankManager(TotalFreedomMod plugin)
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

    public Displayable getDisplay(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return getRank(sender); // Consoles don't have display ranks
        }

        final Player player = (Player) sender;

        // Display impostors
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        if (FUtil.TOTALFREEDOM_DEVELOPERS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }
        
        if (players.DEVELOPER.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }
        
        if (players.EXECUTIVE.contains(player.getName()))
        {
            return Title.EXE;
        }
        
        if (players.COOWNER.contains(player.getName()))
        {
            return Title.CO_OWNER;
        }
        
        if (players.COSC.contains(player.getName()))
        {
            return Title.COS;
        }
        
        final Rank rank = getRank(player);

        // Non-admins don't have titlI ms, display actual rank
        if (!rank.isAdmin())
        {
            return rank;
        }

        // If the player's an owner, display that
        if (ConfigEntry.RANK_FOUNDER.getList().contains(player.getName()))
        {
            return Title.FOUNDER;
        }

        return rank;
    }

    public Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }

        // CONSOLE?
        if (sender.getName().equals("CONSOLE"))
        {
            return ConfigEntry.ADMINLIST_CONSOLE_IS_SENIOR.getBoolean() ? Rank.SENIOR_CONSOLE : Rank.TELNET_CONSOLE;
        }

        // Console admin, get by name
        Admin admin = plugin.al.getEntryByName(sender.getName());

        // Unknown console: RCON?
        if (admin == null)
        {
            return Rank.SENIOR_CONSOLE;
        }

        Rank rank = admin.getRank();

        // Get console
        if (rank.hasConsoleVariant())
        {
            rank = rank.getConsoleVariant();
        }
        return rank;
    }

    public Rank getRank(Player player)
    {
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        final Admin entry = plugin.al.getAdmin(player);
        if (entry != null)
        {
            return entry.getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        plugin.pl.getData(player);
        final FPlayer fPlayer = plugin.pl.getPlayer(player);
        //
        boolean isAdmin = plugin.al.isAdmin(player);
        boolean isDonator = plugin.dl.isDonator(player);
        //
        
        if (isAdmin)
        {
            if (!plugin.al.isIdentityMatched(player))
            {
                FUtil.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
                fPlayer.setSuperadminIdVerified(false);
            }
            else
            {
                fPlayer.setSuperadminIdVerified(true);
                plugin.al.updateLastLogin(player);
            }
        }

        if (plugin.al.isAdminImpostor(player))
        {
            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + Rank.IMPOSTOR.getColoredLoginMessage());
            FUtil.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor and has been frozen!", ChatColor.RED);
            player.getInventory().clear();
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            plugin.pl.getPlayer(player).getFreezeData().setFrozen(true);
            player.sendMessage(ChatColor.RED + "You are marked as an impostor, please verify yourself!");
            return;
        }

        // Set display
        if (isAdmin || isDonator || 
                players.DEVELOPER.contains(player.getName()) || 
                players.COOWNER.contains(player.getName()) || 
                players.EXECUTIVE.contains(player.getName()) || 
                players.COSC.contains(player.getName()) || 
                FUtil.TOTALFREEDOM_DEVELOPERS.contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if (isAdmin)
            {
                Admin admin = plugin.al.getAdmin(player);
                if (admin.hasLoginMessage())
                {
                    loginMsg = ChatUtils.colorize(admin.getLoginMessage());
                }
            }           
            if (isDonator)
            {
                Donator donator = plugin.dl.getDonator(player);
                if (donator.hasLoginMessage())
                {
                    loginMsg = ChatUtils.colorize(donator.getLoginMessage());
                }
            }
    
            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            plugin.pl.getPlayer(player).setTag(display.getColoredTag());

            String displayName = display.getColor() + player.getName();
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
    }
}
