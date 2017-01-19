package com.gmail.ak1cec0ld.plugins.Berries;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener{
    private Berries plugin;
    
    public BlockPhysicsListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockFade(BlockPhysicsEvent event){
        if (plugin.isInBerryPatch(event.getBlock())){
            if (event.getBlock().getType()==Material.SOIL){
                event.setCancelled(true);
            }
        }
    }
}
