package me.apexfreedom.apexfreedommod.donator;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.config.YamlConfig;
import net.pravian.aero.util.Ips;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

public class DonatorList extends FreedomService
{

    public static final String CONFIG_FILENAME = "donators.yml";

    @Getter
    private final Map<String, Donator> allDonators = Maps.newHashMap(); // Includes disabled admins
    // Only active admins below
    @Getter
    private final Set<Donator> activeDonator = Sets.newHashSet();
    private final Map<String, Donator> nameTable = Maps.newHashMap();
    private final Map<String, Donator> ipTable = Maps.newHashMap();
    //
    private final YamlConfig config;

    public DonatorList(TotalFreedomMod plugin)
    {
        super(plugin);

        this.config = new YamlConfig(plugin, CONFIG_FILENAME, true);
    }

    @Override
    protected void onStart()
    {
        load();

        server.getServicesManager().register(Function.class, new Function<Player, Boolean>()
        {
            @Override
            public Boolean apply(Player player)
            {
                return isDonator(player);
            }
        }, plugin, ServicePriority.Normal);

        // deactivateOldEntries(false);
    }

    @Override
    protected void onStop()
    {
        save();
    }

    public void load()
    {
        config.load();

        allDonators.clear();
        for (String key : config.getKeys(false))
        {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
            {
                logger.warning("Invalid donator list format: " + key);
                continue;
            }

            Donator donator = new Donator(key);
            donator.loadFrom(section);

            if (!donator.isValid())
            {
                FLog.warning("Could not load donator: " + key + ". Missing details!");
                continue;
            }

            allDonators.put(key, donator);
        }

        updateTables();
        FLog.info("Loaded " + allDonators.size() + " donator (" + nameTable.size() + " active,  " + ipTable.size() + " IPs)");
    }

    public void save()
    {
        // Clear the config
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Donator donator : allDonators.values())
        {
            donator.saveTo(config.createSection(donator.getConfigKey()));
        }

        config.save();
    }

    public synchronized boolean isDonatorSync(CommandSender sender)
    {
        return isDonator(sender);
    }

    public boolean isDonator(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        Donator admin = getDonator((Player) sender);

        return admin != null && admin.isActive();
    }

    public Donator getDonator(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getDonator((Player) sender);
        }

        return getEntryByName(sender.getName());
    }

    public Donator getDonator(Player player)
    {
        // Find admin
        String ip = Ips.getIp(player);
        Donator admin = getEntryByName(player.getName());

        // Admin by name
        if (admin != null)
        {
            // Check if we're in online mode,
            // Or the players IP is in the admin entry
            if (Bukkit.getOnlineMode() || admin.getIps().contains(ip))
            {
                if (!admin.getIps().contains(ip))
                {
                    // Add the new IP if we have to
                    admin.addIp(ip);
                    save();
                    updateTables();
                }
                return admin;
            }

            // Impostor
        }

        // Admin by ip
        admin = getEntryByIp(ip);
        if (admin != null)
        {
            // Set the new username
            admin.setName(player.getName());
            save();
            updateTables();
        }

        return null;
    }

    public Donator getEntryByName(String name)
    {
        return nameTable.get(name.toLowerCase());
    }

    public Donator getEntryByIp(String ip)
    {
        return ipTable.get(ip);
    }

    public Donator getEntryByIpFuzzy(String needleIp)
    {
        final Donator directAdmin = getEntryByIp(needleIp);
        if (directAdmin != null)
        {
            return directAdmin;
        }

        for (String ip : ipTable.keySet())
        {
            if (FUtil.fuzzyIpMatch(needleIp, ip, 3))
            {
                return ipTable.get(ip);
            }
        }

        return null;
    }

    public void updateLastLogin(Player player)
    {
        final Donator admin = getDonator(player);
        if (admin == null)
        {
            return;
        }

        admin.setLastLogin(new Date());
        admin.setName(player.getName());
        save();
    }

    public boolean isAdminImpostor(Player player)
    {
        return getEntryByName(player.getName()) != null && !isDonator(player);
    }

    public boolean isIdentityMatched(Player player)
    {
        if (Bukkit.getOnlineMode())
        {
            return true;
        }
        Donator admin = getDonator(player);
        return admin == null ? false : admin.getName().equalsIgnoreCase(player.getName());
    }

    public boolean addDonator(Donator admin)
    {
        if (!admin.isValid())
        {
            logger.warning("Could not add donator: " + admin.getConfigKey() + " Donator is missing details!");
            return false;
        }

        final String key = admin.getConfigKey();

        // Store admin, update views
        allDonators.put(key, admin);
        updateTables();

        // Save admin
        admin.saveTo(config.createSection(key));
        config.save();

        return true;
    }

    public boolean removeDonator(Donator admin)
    {
        // Remove admin, update views
        if (allDonators.remove(admin.getConfigKey()) == null)
        {
            return false;
        }
        updateTables();

        // 'Unsave' admin
        config.set(admin.getConfigKey(), null);
        config.save();

        return true;
    }

    public void updateTables()
    {
        activeDonator.clear();
        nameTable.clear();
        ipTable.clear();

        for (Donator admin : allDonators.values())
        {
            if (!admin.isActive())
            {
                continue;
            }

            activeDonator.add(admin);
            nameTable.put(admin.getName().toLowerCase(), admin);

            for (String ip : admin.getIps())
            {
                ipTable.put(ip, admin);
            }

        }

        plugin.wm.adminworld.wipeAccessCache();
    }

    public Set<String> getDonatorNames()
    {
        return nameTable.keySet();
    }

    public Set<String> getDonatorIps()
    {
        return ipTable.keySet();
    }
}
