package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Farmland;
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
    
    

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event){
        Block berryblock = event.getBlock();
        Block underblock = event.getBlock().getRelative(0, -1, 0);
        if(!(underblock.getBlockData() instanceof Farmland))return;
        if(!plugin.isInBerryPatch(berryblock.getLocation()))return;
        Farmland soil = (Farmland)underblock.getBlockData();
        if (soil.getMoisture() == 0){
            berryblock.setType(Material.AIR);
            underblock.setType(Material.PODZOL);
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
