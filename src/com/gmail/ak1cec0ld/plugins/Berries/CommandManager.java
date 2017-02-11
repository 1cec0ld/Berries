package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CommandManager implements CommandExecutor {

    private Berries plugin;
    
    public CommandManager(Berries plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player)sender;
            if (args.length == 0){
                p.sendMessage("You liek berriez?");


            } else if (args.length == 1){
                if (args[0].equals("regions")){
                    List<?> l = plugin.getConfigManager().getBerryRegions();
                    for (Object o : l){
                        p.sendMessage(((String)o));
                    }
                } else if (args[0].equalsIgnoreCase("berries")){
                    Set<String> berries = plugin.getConfigManager().getValidBerriesWithColors();
                    for (String s : berries){
                        p.sendMessage(s);
                    }
                } else if (args[0].equalsIgnoreCase("spawn")){
                    p.sendMessage("Use /berries spawn (water,hoe,{berryname})");
                }
            } else if (args.length == 2){
                if (args[0].equalsIgnoreCase("spawn")){
                    if (args[1].equalsIgnoreCase("water")){
                        ItemStack item = new ItemStack(Material.WATER_BUCKET,1);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName("§9Sprayduck");
                        itemMeta.setLore(Arrays.asList("§eUses Left:","§e20"));
                        item.setItemMeta(itemMeta);
                        p.getWorld().dropItem(p.getLocation(), item);
                    } else if (args[1].equalsIgnoreCase("hoe")){
                        ItemStack item = new ItemStack(Material.IRON_HOE,1);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName("§8SoilTiller");
                        item.setItemMeta(itemMeta);
                        p.getWorld().dropItem(p.getLocation(), item);
                    } else if (plugin.getConfigManager().getValidBerries().contains(args[1])){
                        ItemStack item = new ItemStack(Material.APPLE,1);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName("§aBerry");
                        itemMeta.setLore(Arrays.asList("§"+plugin.getConfigManager().getBerryColor(args[1])+args[1]));
                        item.setItemMeta(itemMeta);
                        p.getWorld().dropItem(p.getLocation(), item);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
