package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_CONSOLE, blockHostConsole = true)
@CommandParameters(description = "Manage permanently banned players and IPs.", usage = "/<command> <reload>")
public class Command_hardcorebanlist extends AF_Command
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (!args[0].equalsIgnoreCase("reload"))
        {
            return false;
        }

        msg("Reloading hardcoreban list...", ChatColor.RED);
        plugin.hbl.stop();
        plugin.hbl.start();
        msg("Reloaded hardcoreban list!");
        msg(plugin.hbl.getHardcorebannedIps().size() + " IPs and "
                + plugin.hbl.getHardcorebannedNames().size() + " usernames loaded.");
        return true;
    }

}
