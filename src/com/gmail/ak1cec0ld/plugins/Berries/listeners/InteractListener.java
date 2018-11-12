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
import org.bukkit.inventory.meta.Damageable;
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
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if (event.getClickedBlock() == null) return;
        Block interact_block = event.getClickedBlock();
        if (!triggerMats.contains(interact_block.getType())) return;
        if (!(plugin.isInBerryPatch(interact_block.getLocation()))) return;
        Player player = event.getPlayer();
        ItemStack player_wielded = player.getInventory().getItemInMainHand();
        if (player_wielded==null)return;
        ItemMeta itemmeta = (player_wielded.getItemMeta());

        //process it as a berry patch
        
        if (plugin.getStorageManager().getOwner(interact_block.getLocation())==null){
            //can only plant berries or till soil
            if (interact_block.getType().equals(Material.FARMLAND)){
                if (isBerry(player_wielded)){
                    List<String> mylore = itemmeta.getLore();
                    String player_wielded_lore_0 = mylore.get(0);
                    plantBerry(player_wielded_lore_0,interact_block.getRelative(0, 1, 0),player);
                    player.sendMessage(ChatColor.GRAY+"The "+player_wielded_lore_0+ChatColor.GREEN+" Berry "+ChatColor.GRAY+"was planted in the soft, earthy "+ChatColor.DARK_GRAY+"soil"+ChatColor.GRAY+".");
                    player_wielded.setAmount(player_wielded.getAmount()-1);
                } else {
                    player.sendMessage(ChatColor.RED+"That is not a real "+ChatColor.GREEN+"Berry");
                }
            } else if (interact_block.getType().equals(Material.PODZOL)){
                if (isSoilTiller(player_wielded)){
                    tillSoil(player, interact_block);
                }
            } else {
                player.sendMessage(ChatColor.RED+"This needs to be tilled with a "+ChatColor.DARK_GRAY+"SoilTiller"+ChatColor.RED+" before you can plant a berry in it!");
            }
            event.setCancelled(true);
        } else if (!plugin.getStorageManager().getOwner(interact_block.getLocation()).equals(player.getUniqueId().toString())){
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED+"That is not your berry");
        } else {
            event.setCancelled(true);
            if (isSprayduck(player_wielded)){
                if (interact_block.getBlockData() instanceof Ageable){
                    useSprayDuck(player,interact_block.getRelative(0, -1, 0));
                } else if (interact_block.getType().equals(Material.FARMLAND)) {
                    useSprayDuck(player,interact_block);
                }
            } else {
                player.sendMessage(ChatColor.RED+"That can only be watered with a real "+ChatColor.DARK_AQUA+"Sprayduck");
            }
        }
    }

    private boolean isBerry(ItemStack item) {
        if (!item.hasItemMeta())return false;
        ItemMeta itemmeta = item.getItemMeta();
        if (!itemmeta.getDisplayName().equals("§aBerry"))return false;
        if (itemmeta.getLore()==null)return false;
        if (itemmeta.getLore().get(0)==null)return false;
        return true;
    }

    private boolean isSoilTiller(ItemStack item) {
        if (!item.hasItemMeta())return false;
        ItemMeta itemmeta = item.getItemMeta();
        if (!itemmeta.getDisplayName().equals("§8SoilTiller"))return false;
        if (itemmeta.getLore()==null)return false;
        if (itemmeta.getLore().get(0)==null)return false;
        if (!(itemmeta instanceof Damageable))return false;
        return true;
    }

    private boolean isSprayduck(ItemStack item) {
        if (!item.hasItemMeta())return false;
        ItemMeta itemmeta = item.getItemMeta();
        if (!itemmeta.getDisplayName().equals("§9Sprayduck"))return false;
        if (itemmeta.getLore()==null)return false;
        if (itemmeta.getLore().get(0)==null)return false;
        if (!itemmeta.getLore().get(0).equals("§eUses Left:") && !itemmeta.getLore().get(0).equals("§cis"))return false;
        if (itemmeta.getLore().get(1)==null)return false;
        try{
            if (0 > Integer.parseInt(ChatColor.stripColor(itemmeta.getLore().get(1))))return false;
        } catch (NumberFormatException e){
            if (itemmeta.getLore().get(1).equals("§1Empty"))return true;
            plugin.getLogger().warning(ChatColor.RED+"A Sprayduck was created badly, and has a non-number in its lore!");
            return false;
        }
        return true;
    }

    private void tillSoil(Player player, Block dirt) {
        ItemStack player_wielded = player.getInventory().getItemInMainHand();
        ItemMeta itemmeta = player_wielded.getItemMeta();
        Damageable damageHandler = (Damageable)itemmeta;
        int damageCost = 20;
        if (damageHandler.getDamage() < player_wielded.getType().getMaxDurability()-damageCost){
            dirt.setType(Material.FARMLAND);
            plugin.setDamage(player_wielded, damageHandler.getDamage()+damageCost);
            plugin.setMoisture(dirt, 3);
            player.sendMessage(ChatColor.GRAY+"You have "+ChatColor.DARK_GRAY+"tilled "+ChatColor.GRAY+"the "+ChatColor.DARK_GRAY+"soil");
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                public void run(){
                    if (dirt.getRelative(0, 1, 0).getType() == Material.AIR){
                        dirt.setType(Material.PODZOL);
                        player.sendMessage(ChatColor.GRAY+"Without a berry in it, the "+ChatColor.DARK_GRAY+"soil "+ChatColor.GRAY+"reverts to its previous state.");
                    }
                }
            }, 5*20L);
        } else {
            player.sendMessage(ChatColor.RED+"Your "+ChatColor.DARK_GRAY+"SoilTiller "+ChatColor.RED+"is too heavily damaged!");
        }

    }
    
    private void useSprayDuck(Player player, Block dirt){
        ItemStack player_wielded = player.getInventory().getItemInMainHand();
        ItemMeta itemmeta = player_wielded.getItemMeta();
        List<String> mylore = itemmeta.getLore();
        String player_wielded_lore_0 = mylore.get(0);
        if(!(dirt.getBlockData() instanceof Farmland))return;
        Farmland soil = (Farmland)dirt.getBlockData();
        String berrytype = plugin.getStorageManager().getBerryTypeAt(dirt.getLocation());

        if(soil.getMoisture() == soil.getMaximumMoisture()){
            player.sendMessage(ChatColor.GRAY+"Your "+berrytype+" "+ChatColor.GREEN+"Berries "+ChatColor.GRAY+"are completely watered!");
        } else if(player_wielded_lore_0.equals("§cis")){
            player.sendMessage(ChatColor.RED+"Your "+ChatColor.AQUA+"Sprayduck "+ChatColor.RED+" is Empty");
        } else {
            if (player_wielded.getItemMeta().getLore().get(1).equals("§e1")){
                itemmeta.setLore(Arrays.asList("§cis","§1Empty"));
                player_wielded.setItemMeta(itemmeta);
                player_wielded.setType(Material.BUCKET);
            } else {
                String colorStripped = ChatColor.stripColor(itemmeta.getLore().get(1));
                Integer bucketValue = Integer.parseInt(colorStripped);
                itemmeta.setLore(Arrays.asList("§eUses Left:","§e"+(bucketValue-1)));
                player_wielded.setItemMeta(itemmeta);
            }
            incrementMoisture(dirt);
            player.sendMessage(ChatColor.GRAY+"You "+ChatColor.AQUA+"watered "+ChatColor.GRAY+"your "+berrytype+" "+ChatColor.GREEN+"Berries");
        }
    }
    
    private void incrementMoisture(Block dirt){
        if(!(dirt.getBlockData() instanceof Farmland))return;
        Farmland soil = ((Farmland)dirt.getBlockData());
        if(soil.getMoisture() == soil.getMaximumMoisture())return;
        plugin.setMoisture(dirt, soil.getMoisture()+1);
    }
    
    private void plantBerry(String berryname, Block berryblock, Player berryowner){
        berryblock.setType(plugin.getConfigManager().getBerryStalkType(berryname));
        plugin.getStorageManager().addFileEntry(berryname, berryblock.getLocation(), berryowner);
    }
}
