package com.gmail.ak1cec0ld.plugins.Berries.listeners;

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
        if (event.getBlock().getType()==Material.FARMLAND){
            if (plugin.isInBerryPatch(event.getBlock())){
                event.setCancelled(true);
            }
        }
    }
}
