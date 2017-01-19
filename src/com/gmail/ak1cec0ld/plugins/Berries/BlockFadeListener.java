package com.gmail.ak1cec0ld.plugins.Berries;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockFadeListener implements Listener{
    private Berries plugin;
    
    public BlockFadeListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        if (plugin.isInBerryPatch(event.getBlock())){
            if (event.getBlock().getType()==Material.SOIL){
                event.setCancelled(true);
            }
        }
    }
}
