package me.apexfreedom.apexfreedommod.commands;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FMsg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "A basic admintools command", usage = "/<command> <message>")
public class Command_admintools extends AF_Command
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        PlayerInventory inv = playerSender.getInventory();
        
        ItemStack stick = new ItemStack(Material.STICK, 1);
        ItemMeta stickmeta = stick.getItemMeta();
        stickmeta.setDisplayName(ChatColor.YELLOW + "Logstick");
        inv.addItem(stick);
        ItemStack logblock = new ItemStack(Material.STONE, 1);
        inv.addItem(logblock);
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        inv.addItem(compass);
        ItemStack wand = new ItemStack(Material.WOOD_AXE, 1);
        ItemMeta wandmeta = wand.getItemMeta();
        wandmeta.setDisplayName(ChatColor.YELLOW + "WorldEdit Wand");
        wand.setItemMeta(wandmeta);
        inv.addItem(wand);
        msg(FMsg.F + ChatColor.RED + "AdminTools have been added!");

        return true;
    }
}
        

