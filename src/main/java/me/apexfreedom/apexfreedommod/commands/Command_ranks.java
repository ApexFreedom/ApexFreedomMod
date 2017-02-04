package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows ranks", usage = "/<command>")
public class Command_ranks extends AF_Command
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg(ChatColor.GRAY + "-_ Rank Ladder _-");
        msg(ChatColor.RED + "System Admin");
        msg(ChatColor.GOLD + "Senior Admin");
        msg(ChatColor.GREEN + "Telnet Admin");
        msg(ChatColor.BLUE + "Super Admin");
        msg(ChatColor.LIGHT_PURPLE + "Donator");
        msg("                                ");
        msg(ChatColor.GRAY + "-_ Title Ladder _-");
        msg(ChatColor.RED + "Founder");
        msg(ChatColor.BLUE + "Co-Founder");
        msg(ChatColor.RED + "Administrator");
        msg(ChatColor.RED + "Chief Of Security");
        msg(ChatColor.DARK_PURPLE + "ApexFreedomMod-Developer");
        msg(ChatColor.DARK_PURPLE + "TotalFreedomMod-Developer");
        msg(ChatColor.GOLD + "Super OP");
        return true;
    }
}