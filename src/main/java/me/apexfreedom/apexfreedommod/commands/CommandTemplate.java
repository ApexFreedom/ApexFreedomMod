package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "", usage = "/<command> <message>")
public class CommandTemplate extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg("");
        FUtil.adminAction(commandLabel, label, senderIsConsole);
        FUtil.bcastMsg(label, ChatColor.GOLD);
        FLog.info("Completed");
        return true;
    }
}
        

