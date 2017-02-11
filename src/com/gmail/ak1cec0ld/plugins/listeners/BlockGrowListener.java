package com.gmail.ak1cec0ld.plugins.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockGrowListener implements Listener{
    Berries plugin;

    
    public BlockGrowListener(Berries plugin){
        this.plugin = plugin;
    }
    
    

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event){
        Block berryblock = event.getBlock();
        //Block underblock = event.getBlock().getWorld().getBlockAt(berryblock.getX(), berryblock.getY()-1, berryblock.getZ());
        if (plugin.isInBerryPatch(berryblock)){
            String berryname = plugin.getStorageManager().getBerryTypeAt(berryblock.getLocation());
            plugin.getConfigManager().getBerryGrowDelayChance(berryname);
        }
        
        
        
        
        
    }
}
