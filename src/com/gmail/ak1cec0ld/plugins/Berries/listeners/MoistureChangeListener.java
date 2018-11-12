package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.MoistureChangeEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class MoistureChangeListener implements Listener {
    private Berries plugin;
    Random r = new Random();
    final double chanceToDry = .20;
    
    public MoistureChangeListener(Berries plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeafDecay(MoistureChangeEvent event){
        String xz = event.getBlock().getX()+","+event.getBlock().getZ();
        if (plugin.getStorageManager().storedBerries.containsKey(xz)){
            if(r.nextInt(1000) >= 1000*chanceToDry){
                event.setCancelled(true);
            }
        }
    }
}
