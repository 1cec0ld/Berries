package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class InteractListener implements Listener{
    
    private Berries plugin;
    Set<Material> triggerMats = new HashSet<Material>();
    
    public InteractListener(Berries plugin){
        this.plugin = plugin;
        triggerMats.add(Material.FARMLAND);
        triggerMats.add(Material.PODZOL);
        triggerMats.add(Material.CARROTS);
        triggerMats.add(Material.WHEAT);
        triggerMats.add(Material.POTATOES);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (event.getClickedBlock() == null) return;
        Block interact_block = event.getClickedBlock();
        if (!triggerMats.contains(interact_block.getType())) return;
        if (!(plugin.isInBerryPatch(interact_block))) return;
        
        Player player = event.getPlayer();
        ItemStack player_wielded = player.getInventory().getItemInMainHand();
        ItemMeta itemmeta = (player_wielded != null ? player_wielded.getItemMeta() : null);

        //process it as a berry patch
        
        if (interact_block.getType().equals(Material.FARMLAND)){
            Block berryBlock = interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()+1, interact_block.getZ());
            if (event.getAction().equals(Action.PHYSICAL)){
                event.setCancelled(true);
                revertBerryChanges(interact_block);
            } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !berryBlock.getType().equals(Material.AIR)){
                if (itemmeta != null && itemmeta.getDisplayName() != null && itemmeta.getDisplayName().equals("§9Sprayduck")){
                    event.setCancelled(true);
                    doSprayDuck(player,interact_block,berryBlock);
                } else {
                    player.sendMessage(ChatColor.RED+"That can only be watered with a "+ChatColor.DARK_AQUA+"Sprayduck");
                }
            } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
              //if they are holding a Berry
                if (itemmeta !=null && itemmeta.getDisplayName()!=null && itemmeta.getDisplayName().equals("§aBerry")){
                    List<String> mylore = (itemmeta != null ? itemmeta.getLore() : null);
                    String player_wielded_lore_0 = (mylore != null ? mylore.get(0) : "");
                    plantBerry(player_wielded_lore_0,berryBlock,player);
                    player.sendMessage(ChatColor.GRAY+"The "+player_wielded_lore_0+ChatColor.GREEN+" Berry "+ChatColor.GRAY+"was planted in the soft, earthy "+ChatColor.DARK_GRAY+"soil"+ChatColor.GRAY+".");
                    player_wielded.setAmount(player_wielded.getAmount()-1);
                } else {
                    revertBerryChanges(interact_block);
                    player.sendMessage(ChatColor.RED+"That is not a "+ChatColor.GREEN+"Berry");
                }
            }
        } else if (interact_block.getType().equals(Material.PODZOL)){
            if(itemmeta != null && itemmeta.getDisplayName() != null && itemmeta.getDisplayName().equals("§8SoilTiller")){
                if (player_wielded.getDurability() >= player_wielded.getType().getMaxDurability()-20){
                    player.sendMessage(ChatColor.RED+"Your "+ChatColor.DARK_GRAY+"SoilTiller "+ChatColor.RED+"is too heavily damaged!");
                } else {
                    interact_block.setType(Material.FARMLAND);
                    ((Farmland)interact_block).setMoisture(4);
                    player_wielded.setDurability((short) (player_wielded.getDurability()+20));
                    player.sendMessage(ChatColor.GRAY+"You have "+ChatColor.DARK_GRAY+"tilled "+ChatColor.GRAY+"the "+ChatColor.DARK_GRAY+"soil");
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                        public void run(){
                            if (interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()+1, interact_block.getZ()).getType() == Material.AIR){
                                interact_block.setType(Material.PODZOL);
                                player.sendMessage(ChatColor.GRAY+"Without a berry in it, the "+ChatColor.DARK_GRAY+"soil "+ChatColor.GRAY+"reverts to its previous state.");
                            }
                        }
                    }, 100L);
                }
            } else {
                player.sendMessage(ChatColor.RED+"This needs to be tilled with a "+ChatColor.DARK_GRAY+"SoilTiller"+ChatColor.RED+" before you can plant a berry in it!");
            }
        } else if (interact_block instanceof Ageable) {
            Block dirtBlock = interact_block.getWorld().getBlockAt(interact_block.getX(), interact_block.getY()-1, interact_block.getZ());
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if (itemmeta != null && itemmeta.getDisplayName() != null && itemmeta.getDisplayName().equals("§9Sprayduck")){
                    event.setCancelled(true);
                    doSprayDuck(player,dirtBlock,interact_block);
                } else {
                    player.sendMessage(ChatColor.RED+"That can only be watered with a "+ChatColor.DARK_AQUA+"Sprayduck");
                }
            }
        }
    }
      /*} else if (event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player player = event.getPlayer();
            ItemStack player_wielded = player.getInventory().getItemInMainHand();
            ItemMeta itemmeta = (player_wielded != null ? player_wielded.getItemMeta() : null);
            List<String> mylore = (itemmeta!=null?itemmeta.getLore():null);
            String player_wielded_lore_0 = (mylore!=null?mylore.get(0):"");
            if (interact_block instanceof Ageable){
            } else if (interact_block.getType().equals(Material.DIRT)){
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
            }
        }
    }*/

    private void doSprayDuck(Player player, Block dirt, Block berry){
        Farmland soil = (Farmland)dirt;
        ItemStack player_wielded = player.getInventory().getItemInMainHand();
        ItemMeta itemmeta = (player_wielded != null ? player_wielded.getItemMeta() : null);
        List<String> mylore = (itemmeta != null ? itemmeta.getLore() : null);
        String player_wielded_lore_0 = (mylore != null ? mylore.get(0) : "");
        

        if (!plugin.getStorageManager().getOwner(berry.getLocation()).equals(player.getUniqueId().toString())){
            int soilmoisture = soil.getMoisture();
            player.sendMessage(ChatColor.RED+"That is not your berry");
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                public void run(){
                    dirt.setType(Material.FARMLAND);
                    soil.setMoisture(soilmoisture);
                }
            }, 1L);
        } else if(soil.getMoisture() == soil.getMaximumMoisture()){
            player.sendMessage(ChatColor.GRAY+"Your "+plugin.getStorageManager().getBerryTypeAt(berry.getLocation())+" "+ChatColor.GREEN+"Berries "+ChatColor.GRAY+"are ready for harvesting");
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                public void run(){
                    player_wielded.setType(Material.WATER_BUCKET);
                }
            }, 1L);
        } else if(player_wielded_lore_0.equals("§cis")){
            player.sendMessage(ChatColor.RED+"Your "+ChatColor.AQUA+"Sprayduck "+ChatColor.RED+" is Empty");
        } else {
            /*belowblock.setData((byte) (belowblock.getData()+1));*/soil.setMoisture(soil.getMoisture()+1);
            player.sendMessage(ChatColor.GRAY+"You "+ChatColor.AQUA+"watered "+ChatColor.GRAY+"your "+plugin.getStorageManager().getBerryTypeAt(berry.getLocation())+" "+ChatColor.GREEN+"Berries");
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                public void run(){
                    if (player_wielded.getItemMeta().getLore().get(1).equals("§e1")){
                        ItemMeta meta = player_wielded.getItemMeta();
                        meta.setLore(Arrays.asList("§cis","§1Empty"));
                        player_wielded.setItemMeta(meta);
                        player_wielded.setType(Material.BUCKET);
                    } else {
                        try{ //Using try/catch, Just in case it isnt a number inside the colored lore
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
    }
    private void revertBerryChanges(Block base){
        Block berryBlock = base.getWorld().getBlockAt(base.getX(), base.getY()+1, base.getZ());
        Farmland berrySoil = (Farmland)base;
        int soilmoisture = berrySoil.getMoisture();
        Material berrytype = berryBlock.getType();
        Ageable berryPlant = (Ageable)berryBlock;
        int berryAge = berryPlant.getAge();
        
        berryBlock.setType(Material.AIR);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
            public void run(){
                base.setType(Material.FARMLAND);
                berrySoil.setMoisture(soilmoisture);
                berryBlock.setType(berrytype);
                berryPlant.setAge(berryAge); 
            }
        }, 1L);
    }
    private void plantBerry(String berryname, Block berryblock, Player berryowner){
        berryblock.setType(plugin.getConfigManager().getBerryStalkType(berryname));
        plugin.getStorageManager().addFileEntry(berryname, berryblock.getLocation(), berryowner);
    }
}
