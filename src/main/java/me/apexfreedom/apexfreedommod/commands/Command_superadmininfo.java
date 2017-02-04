package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "How to apply for Super Admin", usage = "/<command>", aliases = "sai,si,ai")
public class Command_superadmininfo extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg("So you wanna know how to apply for Super Admin?", ChatColor.GRAY);
        msg("First Step >> Join our Website http://apexfreedom.boards.net/", ChatColor.RED);
        msg("Second Step >> Wait 10 days till you apply!", ChatColor.RED);
        msg("Third Step >> When you wait 10 days Apply for Super Admin using this template: IN-PROGRESS", ChatColor.RED);
        msg("If you want to apply for Telnet Admin wait 30d after added to Super Admin With a valid rec from a Specialist.", ChatColor.RED);
        msg("If you get recomended from a STA & Specialist you have to wait 5 days, if you get a SRA & SYS you don't have to wait.", ChatColor.RED);
        return true;
    }
}
        

