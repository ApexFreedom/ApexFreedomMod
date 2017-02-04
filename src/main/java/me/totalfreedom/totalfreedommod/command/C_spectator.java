package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Quickly change your own gamemode to spectator.", usage = "/<command>", aliases = "gmsp")
public class C_spectator extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        playerSender.setGameMode(GameMode.SPECTATOR);
        msg(ChatColor.BLUE + "Gamemode set to spectator.");
        msg("Find them griefers!");
        return true;
    }
}
