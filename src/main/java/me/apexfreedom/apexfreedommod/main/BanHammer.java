package me.apexfreedom.apexfreedommod.main;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BanHammer
{
    public static ItemStack getBanHammer()
    {
        ItemStack banhammer = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta banhammermeta = banhammer.getItemMeta();
        banhammermeta.setLore(Arrays.asList(ChatColor.BLUE + "Unleash the power of...", ChatColor.YELLOW + "The BanHammer!"));
        banhammermeta.setDisplayName(ChatColor.RED + "BanHammer!");
        banhammer.setItemMeta(banhammermeta);
        return banhammer;
    }
}
