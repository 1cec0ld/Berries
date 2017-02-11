package com.gmail.ak1cec0ld.plugins.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class InteractListener implements Listener{
    
    Berries plugin;
    
    public InteractListener(Berries plugin){
        this.plugin = plugin;
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (event.getClickedBlock() != null){
            Block interact_block = event.getClickedBlock();
            Block aboveblock = interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()+1, interact_block.getZ());
            Block belowblock = interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()-1, interact_block.getZ());
            if (plugin.isInBerryPatch(interact_block)){
                //process it as a berry patch
                if (event.getAction().equals(Action.PHYSICAL)){
                     if (interact_block.getType().equals(Material.SOIL)){
                         revertBerryChanges(interact_block);
                     } else {
                         return;
                     }
                } else if (event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    Player player = event.getPlayer();
                    ItemStack player_wielded = player.getInventory().getItemInMainHand();
                    ItemMeta itemmeta = (player_wielded!=null?player_wielded.getItemMeta():null);
                    List<String> mylore = (itemmeta!=null?itemmeta.getLore():null);
                    String player_wielded_lore_0 = (mylore!=null?mylore.get(0):"");
                    if (interact_block.getType().equals(Material.CARROT) || interact_block.getType().equals(Material.CROPS) || interact_block.getType().equals(Material.POTATO)){
                        //if they are holding a Sprayduck
                        if (itemmeta !=null && itemmeta.getDisplayName().equals("§9Sprayduck")){
                            event.setCancelled(true);
                            player_wielded.setType(Material.BUCKET);
                            if (plugin.getStorageManager().getOwner(interact_block.getLocation()) != player.getUniqueId().toString()){
                                byte soilmoisture = belowblock.getData();
                                player.sendMessage(ChatColor.RED+"That is not your berry");
                                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                                    public void run(){
                                        belowblock.setType(Material.SOIL);
                                        belowblock.setData(soilmoisture);
                                        player_wielded.setType(Material.WATER_BUCKET);
                                    }
                                }, 1L);
                            } else if(interact_block.getData() == 7){
                                player.sendMessage(ChatColor.GRAY+"Your "+plugin.getStorageManager().getBerryTypeAt(interact_block.getLocation())+" "+ChatColor.GREEN+"Berries "+ChatColor.GRAY+"are ready for harvesting");
                                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                                    public void run(){
                                        player_wielded.setType(Material.WATER_BUCKET);
                                    }
                                }, 1L);
                            } else if(player_wielded_lore_0.equals("§1Empty")){
                                player.sendMessage(ChatColor.RED+"Your "+ChatColor.AQUA+"Sprayduck "+ChatColor.RED+" is "+ChatColor.DARK_BLUE+"Empty");
                            } else if(belowblock.getData()==7){
                                player.sendMessage(ChatColor.DARK_BLUE+"That "+ChatColor.DARK_GRAY+"soil "+ChatColor.DARK_BLUE+"is already watered enough!");
                                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                                    public void run(){
                                        belowblock.setType(Material.SOIL);
                                        belowblock.setData((byte) 7);
                                        player_wielded.setType(Material.WATER_BUCKET);
                                    }
                                }, 1L);
                            } else {
                                belowblock.setData((byte) (belowblock.getData()+1));
                                player.sendMessage(ChatColor.GRAY+"You "+ChatColor.AQUA+"watered "+ChatColor.GRAY+"your "+plugin.getStorageManager().getBerryTypeAt(interact_block.getLocation())+" "+ChatColor.GREEN+"Berries");
                                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                                    public void run(){
                                        if (player_wielded.getItemMeta().getLore().get(1).equals("§e1")){
                                            player_wielded.getItemMeta().getLore().set(0, "§cis");
                                            player_wielded.getItemMeta().getLore().set(1, "§1Empty");
                                            player_wielded.setType(Material.BUCKET);
                                        } else {
                                            try{ //Just in case it isnt a number inside the colored lore
                                                ItemMeta meta = player_wielded.getItemMeta();
                                                String colorStripped = ChatColor.stripColor(meta.getLore().get(1));
                                                Integer bucketValue = Integer.parseInt(colorStripped);
                                                meta.setLore(Arrays.asList("§eUses Left:","§e"+(bucketValue-1)));
                                                player_wielded.setItemMeta(meta);
                                                player_wielded.setType(Material.WATER_BUCKET);
                                            } catch (NumberFormatException e){}
                                        }
                                    }
                                }, 1L);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED+"That can only be watered with a "+ChatColor.DARK_AQUA+"Sprayduck");
                        }
                    } else if (interact_block.getType().equals(Material.DIRT)){
                        //if they are holding a Soiltiller
                        if (itemmeta !=null && itemmeta.getDisplayName()!=null && itemmeta.getDisplayName().equals("§8SoilTiller")){
                            if (player_wielded.getDurability() >= player_wielded.getType().getMaxDurability()-20){
                                player.sendMessage(ChatColor.RED+"Your "+ChatColor.DARK_GRAY+"SoilTiller "+ChatColor.RED+"isn't strong enough");
                            } else {
                                if (interact_block.getData() == 2){
                                    interact_block.setType(Material.SOIL);
                                    interact_block.setData((byte) 4);
                                    player_wielded.setDurability((short) (player_wielded.getDurability()+20));
                                    player.sendMessage(ChatColor.GRAY+"You have "+ChatColor.DARK_GRAY+"tilled "+ChatColor.GRAY+"the "+ChatColor.DARK_GRAY+"soil");
                                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                                        public void run(){
                                            if (aboveblock.getType() == Material.AIR){
                                                interact_block.setType(Material.DIRT);
                                                interact_block.setData((byte) 2);
                                                player.sendMessage(ChatColor.GRAY+"The "+ChatColor.DARK_GRAY+"soil "+ChatColor.GRAY+"reverted to its previous state");
                                            }
                                        }
                                    }, 100L);
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED+"This needs to be tilled with a "+ChatColor.DARK_GRAY+"SoilTiller");
                        }
                    } else if (interact_block.getType().equals(Material.SOIL)){
                        //if they are holding a Berry
                        if (itemmeta !=null && itemmeta.getDisplayName()!=null && itemmeta.getDisplayName().equals("§aBerry")){
                            plantBerry(player_wielded_lore_0,aboveblock,player);
                            player.sendMessage(ChatColor.GRAY+"The "+player_wielded_lore_0+ChatColor.GREEN+" Berry "+ChatColor.GRAY+"was planted in the soft, earthy "+ChatColor.DARK_GRAY+"soil"+ChatColor.GRAY+".");
                            player_wielded.setAmount(player_wielded.getAmount()-1);
                        } else {
                            revertBerryChanges(interact_block);
                            player.sendMessage(ChatColor.RED+"That is not a "+ChatColor.GREEN+"Berry");
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void revertBerryChanges(Block interact_block){
        Block berryBlock = interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()+1, interact_block.getZ());
        int soilmoisture = interact_block.getData();
        int berrytype = berryBlock.getTypeId();
        int berrydata = berryBlock.getData();
        berryBlock.setType(Material.AIR);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
            public void run(){
                interact_block.setTypeId(60);
                interact_block.setData((byte) soilmoisture);
                berryBlock.setTypeId(berrytype);
                berryBlock.setData((byte) berrydata); 
            }
        }, 1L);
    }
    @SuppressWarnings("deprecation")
    private void plantBerry(String berryname, Block berryblock, Player berryowner){
        berryblock.setTypeId(plugin.getConfigManager().getBerryStalkType(berryname));
        berryblock.setData((byte) 0);
        plugin.getStorageManager().addFileEntry(berryname, berryblock.getLocation(), berryowner);
    }
}
