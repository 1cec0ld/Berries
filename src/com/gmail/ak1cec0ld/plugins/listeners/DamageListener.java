package com.gmail.ak1cec0ld.plugins.listeners;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class DamageListener implements Listener{
    Berries plugin;
    HashSet<ItemStack> invSlots;
    
    public DamageListener(Berries plugin){
        this.plugin = plugin;
        this.invSlots = new HashSet<ItemStack>();
    }

    
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent event_EE = (EntityDamageByEntityEvent)event;
            if (event_EE.getDamager() instanceof Player){
                Player attacker = (Player)event_EE.getDamager();
                if (attacker.hasPermission("pokeitems.berries.use")){
                    Set<String> validBerries = plugin.getConfigManager().getValidBerriesWithColors();
                    List<?> validItems = plugin.getConfigManager().getAttachableItems();
                    invSlots.add(attacker.getInventory().getHelmet());
                    invSlots.add(attacker.getInventory().getChestplate());
                    invSlots.add(attacker.getInventory().getLeggings());
                    invSlots.add(attacker.getInventory().getBoots());
                    invSlots.add(attacker.getInventory().getItemInMainHand());
                    invSlots.add(attacker.getInventory().getItemInOffHand());
                    //attacker.is.player
                    for (ItemStack item : invSlots){
                        if (item.getType()!=null && validItems.contains(item.getType().toString())){
                            if (item.getItemMeta() != null && item.getItemMeta().hasLore()){
                                if (validBerries.contains(item.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(item.getItemMeta().getLore().get(0)).contains("attack")){
                                    plugin.executeBerry(event_EE,attacker,item);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player){
            Player target = (Player) event.getEntity();
            //target.is.player
            if (target.hasPermission("pokeitems.berries.use")){
                Set<String> validBerries = plugin.getConfigManager().getValidBerriesWithColors();
                List<String> validItems = plugin.getConfigManager().getAttachableItems();
                invSlots.add(target.getInventory().getHelmet());
                invSlots.add(target.getInventory().getChestplate());
                invSlots.add(target.getInventory().getLeggings());
                invSlots.add(target.getInventory().getBoots());
                invSlots.add(target.getInventory().getItemInMainHand());
                invSlots.add(target.getInventory().getItemInOffHand());
                for (ItemStack item : invSlots){
                    if (item!=null && validItems.contains(item.getType().toString())){
                        if (item.getItemMeta() != null && item.getItemMeta().hasLore()){
                            if (validBerries.contains(item.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(item.getItemMeta().getLore().get(0)).contains("damage")){
                                plugin.executeBerry(event,target,item);
                            }
                        }
                    }
                }
                invSlots.clear();
            }
        }
    }


}
