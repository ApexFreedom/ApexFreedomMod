package me.apexfreedom.apexfreedommod.commands;

import me.apexfreedom.apexfreedommod.main.BanHammer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "banhammer", usage = "/<command> <unleash | remove>")
public class Command_banhammer extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        Player player = playerSender;
        
        if (args.length < 1)
        {
            return false;
        }

        switch (args[0])
        {

            case "unleash":
            {    
               player.getInventory().addItem(BanHammer.getBanHammer());
               player.getWorld().strikeLightning(player.getLocation());
               Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " >> has unleashed the BanHammer!");
              
               return true;
            }
            
            case "remove":
            {
               player.getInventory().remove(BanHammer.getBanHammer());
               Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " >> has placed the BanHammer back into its sheath");
              
               return true; 
            }
            
            default:
            {
                return false;
            }
        }
    }
}

