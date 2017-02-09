package com.gmail.ak1cec0ld.plugins.listeners;

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
    public void onBlockFade(BlockFromToEvent event){
        if (plugin.isInBerryPatch(event.getBlock())){
            event.setCancelled(true);
        }
    }
}
