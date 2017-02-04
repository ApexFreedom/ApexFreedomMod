package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.TELNET_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "This command is used to clean up the server by reloadinng everything", usage = "/<command>")
public class Command_cleanup extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
            FUtil.adminAction(sender.getName(), "Nightly clean up is enabled prepare for lag!!", false);
            
            server.dispatchCommand(sender, "opall -c");
            server.dispatchCommand(sender, "rd");
            server.dispatchCommand(sender, "setl");
            server.dispatchCommand(sender, "purgeall");
            server.dispatchCommand(sender, "banlist purge");
            server.dispatchCommand(sender, "glist purge");
            server.dispatchCommand(sender, "tfm reload");
            server.dispatchCommand(sender, "saconfig clean");
            server.dispatchCommand(sender, "nc");
            
        return true;
    }
}