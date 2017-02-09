package com.gmail.ak1cec0ld.plugins.listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

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
            item.getItemMeta().setDisplayName("§aBerry");
            item.getItemMeta().getLore().set(0, getWeightedRandom(plugin.getConfigManager().getBerryDropChances()));
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
        }
    }
    private String getWeightedRandom(HashMap<String,Integer> m){
        Integer sum = 0;
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
        return "§2¯\\_(o_o)_/¯";
    }
}
