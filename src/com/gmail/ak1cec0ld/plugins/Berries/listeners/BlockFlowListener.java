package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockFlowListener implements Listener{
    private Berries plugin;
    
    public BlockFlowListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockFlow(BlockFromToEvent event){
        String x = event.getToBlock().getX()+","+event.getToBlock().getZ();
        if (plugin.getStorageManager().storedBerries.containsKey(x)){ //if something flows into an actual Berry in the Storage HashMap
            event.setCancelled(true);
        } else { //if it flows over a Soil Block, then check if that Soil Block is in the worldguard regions that matter
            Block underblock = event.getToBlock().getWorld().getBlockAt(event.getToBlock().getX(), event.getToBlock().getY()-1, event.getToBlock().getZ());
            if (underblock.getType().equals(Material.SOIL) || underblock.getType().equals(Material.DIRT)){
                if (plugin.isInBerryPatch(event.getToBlock().getWorld().getBlockAt(event.getToBlock().getX(), event.getToBlock().getY()-1, event.getToBlock().getZ()))){
                    event.setCancelled(true);
                }
            }
        }
    }
}
