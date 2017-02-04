package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "Displays information about ApexFreedomMod", usage = "/<command> <message>", aliases = "afm,af")
public class Command_apexfreedommod extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg(ChatColor.GRAY + "Welcome to ApexFreedomMod: v3.0 BETA 4");
        msg(ChatColor.RED + "ApexFreedomMod: This is a plugin for a server");
        msg(ChatColor.RED + "Bug Log >> BanHammer Doesn't ban player when hit, Cleanup crashed the server.");
        msg(ChatColor.RED + "Compiled by" + ChatColor.GRAY + " SavyKilo " + ChatColor.RED + "and on the date of" + ChatColor.GRAY + " 2/2/17 " + ChatColor.RED + "!");
        msg(ChatColor.GOLD + "Release notes are linked here: "
                + "The Main beta src and main java code is linked here: ");
        
        return true;
    }
}
        

