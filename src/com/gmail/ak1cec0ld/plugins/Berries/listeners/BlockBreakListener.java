package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class BlockBreakListener implements Listener{
    private Berries plugin;
    private HashMap<Integer,String> dataToKeys = new HashMap<Integer,String>();
    private Random r = new Random();
    
    public BlockBreakListener(Berries plugin){
        this.plugin = plugin;
        dataToKeys.put(0, "barren");
        dataToKeys.put(1, "dry");
        dataToKeys.put(2, "dry");
        dataToKeys.put(3, "dry");
        dataToKeys.put(4, "damp");
        dataToKeys.put(5, "damp");
        dataToKeys.put(6, "damp");
        dataToKeys.put(7, "saturated");
    }
    

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if (block.getType().equals(Material.CARROT) || block.getType().equals(Material.CROPS) || block.getType().equals(Material.POTATO)){
            if (plugin.isInBerryPatch(block)){
                int blockdata = block.getData();
                if (blockdata == 7){
                    Player player = event.getPlayer();
                    if (plugin.getStorageManager().getOwner(block.getLocation()).equals(player.getUniqueId().toString())){
                        String berryname = plugin.getStorageManager().getBerryTypeAt(block.getLocation());
                        List<Integer> rates = plugin.getConfigManager().getBerryDropRates(berryname, dataToKeys.get(blockdata));
                        Block underblock = block.getWorld().getBlockAt(block.getX(), block.getY()-1, block.getZ());
                        if (rates != null){
                            if (rates.size() == 2){
                                if(rates.get(0)>rates.get(1)){
                                    rates.set(0, rates.get(0)+rates.get(1));
                                    rates.set(1, rates.get(0)-rates.get(1));
                                    rates.set(0, rates.get(0)-rates.get(1));
                                }
                                int total = r.nextInt(rates.get(1)+1-rates.get(0))+rates.get(0);
                                dropBerries(total,berryname,block);
                            } else {
                                dropBerries(rates.get(0),berryname,block);
                            }
                        } else {
                            dropBerries(1,berryname,block);
                        }
                        plugin.getStorageManager().removeFileEntry(block.getLocation());
                        block.setType(Material.AIR);
                        underblock.setType(Material.DIRT);
                        underblock.setData((byte) 2);
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED+"That is not your berry");
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
    private void dropBerries(Integer amount, String berryname, Block block){
        ItemStack item = new ItemStack(Material.APPLE,amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§aBerry");
        itemMeta.setLore(Arrays.asList("§"+plugin.getConfigManager().getBerryColor(berryname)+berryname));
        item.setItemMeta(itemMeta);
        block.getWorld().dropItemNaturally(block.getLocation(), item);
    }
}
