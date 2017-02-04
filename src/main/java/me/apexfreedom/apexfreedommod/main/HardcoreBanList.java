package me.apexfreedom.apexfreedommod.main;

import com.google.common.collect.Sets;
import java.util.Set;
import lombok.Getter;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.config.YamlConfig;
import net.pravian.aero.util.Ips;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

public class HardcoreBanList extends FreedomService
{

    public static final String CONFIG_FILENAME = "hardcoreban.yml";

    @Getter
    private final Set<String> hardcorebannedNames = Sets.newHashSet();
    @Getter
    private final Set<String> hardcorebannedIps = Sets.newHashSet();

    public HardcoreBanList(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
        hardcorebannedNames.clear();
        hardcorebannedIps.clear();

        final YamlConfig config = new YamlConfig(plugin, CONFIG_FILENAME, true);
        config.load();

        for (String name : config.getKeys(false))
        {
            hardcorebannedNames.add(name.toLowerCase().trim());
            hardcorebannedIps.addAll(config.getStringList(name));
        }

        FLog.info("Loaded " + hardcorebannedIps.size() + " hardcore IP bans and " + hardcorebannedNames.size() + " hardcore username bans.");
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        final String username = event.getPlayer().getName();
        final String ip = Ips.getIp(event);

        // Permbanned IPs
        for (String testIp : getHardcorebannedIps())
        {
            if (FUtil.fuzzyIpMatch(testIp, ip, 4))
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                        ChatColor.RED + "Your IP address is hardcore banned from this server, you will not be relesed!");
                return;
            }
        }

        // Permbanned usernames
        for (String testPlayer : getHardcorebannedNames())
        {
            if (testPlayer.equalsIgnoreCase(username))
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                        ChatColor.RED + "Your username is permanently banned from this server, you will not be relesed!");
                return;
            }
        }

    }

}
