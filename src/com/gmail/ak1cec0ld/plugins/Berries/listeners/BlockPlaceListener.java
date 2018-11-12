package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockPlaceListener implements Listener{
    private Berries plugin;
    
    public BlockPlaceListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getBlockAgainst().getType()==Material.FARMLAND){
            if (plugin.isInBerryPatch(event.getBlock().getLocation())){
                if (!(event.getItemInHand().getItemMeta() !=null && event.getItemInHand().getItemMeta().hasDisplayName() && event.getItemInHand().getItemMeta().getDisplayName().equals("§aBerry"))){
                    //if the item in hand (assuming it has a displayname) isn't a Berry, cancel.
                    event.setCancelled(true);
                }
            }
        }
    }
}
