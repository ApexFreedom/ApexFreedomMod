package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Use this command to toggle a command spyer, this is used to see what the Naughty operators are up to.",
        usage = "/<command>", 
        aliases = "commandspy")
public class C_cmdspy extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        FPlayer playerdata = plugin.pl.getPlayer(playerSender);
        playerdata.setCommandSpy(!playerdata.cmdspyEnabled());
        msg(ChatColor.RED + "CommandSpy " + 
                (playerdata.cmdspyEnabled() ? ChatColor.GREEN + "enabled." : ChatColor.RED + "disabled."));

        return true;
    }
}
