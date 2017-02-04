package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "This is a for the OP's that want to clear there chat", usage = "/<command>")
public class Command_cmc extends AF_Command
{
    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        for (int i = 0; i <= 150; i++)
        {
            msg("");
        }
        msg(FMsg.F + ChatColor.RED + "You cleared your personal chat.");

        return true;
    }
}
