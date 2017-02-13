package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.Arrays;
import java.util.HashMap;
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
                } else if (args[0].equalsIgnoreCase("list")){
                    Set<String> berries = plugin.getConfigManager().getValidBerriesWithColors();
                    for (String s : berries){
                        p.sendMessage(s);
                    }
                } else if (args[0].equalsIgnoreCase("spawn")){
                    p.sendMessage("/berries spawn (water) [Uses]");
                    p.sendMessage("/berries spawn (hoe) [Material]");
                    p.sendMessage("/berries spawn {berryname} [Amount]");
                }
            } else if (args.length == 2){
                if (args[0].equalsIgnoreCase("spawn")){
                    if (args[1].equalsIgnoreCase("water") || args[1].equalsIgnoreCase("sprayduck")){
                        p.performCommand("berries spawn water 20");
                    } else if (args[1].equalsIgnoreCase("hoe") || args[1].equalsIgnoreCase("soiltiller")){
                        p.performCommand("berries spawn hoe iron");
                    } else {
                        for (String berryNamePlain : plugin.getConfigManager().getValidBerries()){
                            if (args[1].equalsIgnoreCase(berryNamePlain)){
                                p.performCommand("berries spawn "+berryNamePlain+" 1");
                            }
                        }
                    }
                }
            } else if (args.length == 3){
                if (args[0].equalsIgnoreCase("spawn")){
                    int amount;
                    try{
                        amount = Integer.parseInt(args[2]);
                        if (args[1].equalsIgnoreCase("water") || args[1].equalsIgnoreCase("sprayduck")){
                            ItemStack item = new ItemStack(Material.WATER_BUCKET,1);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setDisplayName("§9Sprayduck");
                            itemMeta.setLore(Arrays.asList("§eUses Left:","§e"+amount));
                            item.setItemMeta(itemMeta);
                            p.getWorld().dropItem(p.getLocation(), item);
                        } else {
                            for (String berryNamePlain : plugin.getConfigManager().getValidBerries()){
                                if (args[1].equalsIgnoreCase(berryNamePlain)){
                                    for (int i = 0; i < amount; i++){
                                        ItemStack item = new ItemStack(Material.APPLE,1);
                                        ItemMeta itemMeta = item.getItemMeta();
                                        itemMeta.setDisplayName("§aBerry");
                                        itemMeta.setLore(Arrays.asList("§"+plugin.getConfigManager().getBerryColor(berryNamePlain)+berryNamePlain));
                                        item.setItemMeta(itemMeta);
                                        p.getWorld().dropItem(p.getLocation(), item);
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e){
                        HashMap<String,Material> mats = new HashMap<String,Material>();
                        mats.put("wood",Material.WOOD_HOE);
                        mats.put("stone",Material.STONE_HOE);
                        mats.put("gold",Material.GOLD_HOE);
                        mats.put("iron",Material.IRON_HOE);
                        mats.put("diamond",Material.DIAMOND_HOE);
                        if (mats.keySet().contains(args[2].toLowerCase()) && (args[1].equalsIgnoreCase("hoe") || args[1].equalsIgnoreCase("soiltiller"))){
                            ItemStack item = new ItemStack(mats.get(args[2].toLowerCase()),1);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setDisplayName("§8SoilTiller");
                            item.setItemMeta(itemMeta);
                            p.getWorld().dropItem(p.getLocation(), item);
                        } else {
                            p.performCommand("berries spawn");
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
