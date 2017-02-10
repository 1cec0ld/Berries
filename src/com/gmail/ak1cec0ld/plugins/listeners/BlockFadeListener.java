package com.gmail.ak1cec0ld.plugins.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockFadeListener implements Listener{
    private Berries plugin;
    
    public BlockFadeListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        String x = event.getBlock().getX()+","+event.getBlock().getZ();
        if (!plugin.getStorageManager().storedBerries.containsKey(x)){
            return;
        }
        if (plugin.isInBerryPatch(event.getBlock())){
            if (event.getBlock().getType()==Material.SOIL){
                event.setCancelled(true);
            }
        }
    }
}
