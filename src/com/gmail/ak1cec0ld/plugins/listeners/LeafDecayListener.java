package com.gmail.ak1cec0ld.plugins.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class LeafDecayListener implements Listener{
    
    Berries plugin;
    private Random r = new Random();
    
    public LeafDecayListener(Berries plugin){
        this.plugin = plugin;
    }

    
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event){
        if (r.nextDouble()*100 < 1){ //generates 0 to 99.999999
            ItemStack item = new ItemStack(Material.APPLE, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName("§aBerry");
            String randomBerryName = getWeightedRandomBerry();
            itemMeta.setLore(Arrays.asList(randomBerryName));
            item.setItemMeta(itemMeta);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
        }
    }
    private String getWeightedRandomBerry(){
        Integer sum = 0;
        HashMap<String, Integer> m = plugin.getConfigManager().getBerryDropChances();
        for(Integer s:m.values()){
            sum += s;
        }
        Integer counter = r.nextInt(sum);
        for(String str : m.keySet()){
            counter -= m.get(str);
            if (counter < 0){
                return str;
            }
        }
        return ChatColor.DARK_GREEN+"¯\\_(o_o)_/¯";
    }
}
