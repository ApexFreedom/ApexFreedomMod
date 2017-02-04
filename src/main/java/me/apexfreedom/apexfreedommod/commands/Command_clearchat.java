package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import me.totalfreedom.totalfreedommod.util.FMsg;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.TELNET_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Clears the general chat - only for telnet because of abuse", usage = "/<command>", aliases = "cc")
public class Command_clearchat extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        for (int i = 0; i <= 100; i++)
        {
            FUtil.bcastMsg("");
        }
        FUtil.bcastMsg(FMsg.F + ChatColor.RED + "Chat has been cleared by " + sender.getName() + "!");

        return true;
    }
}
        

