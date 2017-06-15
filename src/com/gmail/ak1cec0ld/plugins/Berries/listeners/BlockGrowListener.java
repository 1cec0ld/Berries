package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockGrowListener implements Listener{
    Berries plugin;
    private Random r = new Random();

    
    public BlockGrowListener(Berries plugin){
        this.plugin = plugin;
    }
    
    

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockGrow(BlockGrowEvent event){
        Block berryblock = event.getBlock();
        Block underblock = event.getBlock().getWorld().getBlockAt(berryblock.getX(), berryblock.getY()-1, berryblock.getZ());
        byte under_data = underblock.getData();
        if (plugin.isInBerryPatch(berryblock)){
            if (under_data == (byte) 0){
                berryblock.setType(Material.AIR);
                underblock.setType(Material.DIRT);
                underblock.setData((byte) 2);
                plugin.getStorageManager().removeFileEntry(berryblock.getLocation());
                return;
            } else {
                String berryname = plugin.getStorageManager().getBerryTypeAt(berryblock.getLocation());
                int delay = plugin.getConfigManager().getBerryGrowDelayChance(berryname);
                double randomVal = r.nextDouble()*100; //generates 00.00 to 99.99
                if (delay >= randomVal){ //higher delay values have higher chance to cancel the event
                    event.setCancelled(true);
                }
            }
        }
    }
}
