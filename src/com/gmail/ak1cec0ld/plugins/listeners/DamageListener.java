package com.gmail.ak1cec0ld.plugins.listeners;

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
    
    public DamageListener(Berries plugin){
        this.plugin = plugin;
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
                    ItemStack helmet = attacker.getInventory().getHelmet();
                    ItemStack chest = attacker.getInventory().getChestplate();
                    ItemStack leggings = attacker.getInventory().getLeggings();
                    ItemStack boots = attacker.getInventory().getBoots();
                    ItemStack main = attacker.getInventory().getItemInMainHand();
                    ItemStack off = attacker.getInventory().getItemInOffHand();
                    //attacker.is.player
                    if (validItems.contains(helmet.getType().toString())){
                        if (validBerries.contains(helmet.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(helmet.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,helmet);
                        }
                    }
                    if (validItems.contains(chest.getType().toString())){
                        if (validBerries.contains(chest.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(chest.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,chest);    
                        }
                    }
                    if (validItems.contains(leggings.getType().toString())){
                        if (validBerries.contains(leggings.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(leggings.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,leggings);    
                        }
                    }
                    if (validItems.contains(boots.getType().toString())){
                        if (validBerries.contains(boots.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(boots.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,boots);    
                        }
                    }
                    if (validItems.contains(main.getType().toString())){
                        if (validBerries.contains(main.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(main.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,main);    
                        }
                    }
                    if (validItems.contains(off.getType().toString())){
                        if (validBerries.contains(off.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(off.getItemMeta().getLore().get(0)).contains("attack")){
                            plugin.executeBerry(event_EE,attacker,off);    
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
                List<?> validItems = plugin.getConfigManager().getAttachableItems();
                ItemStack helmet = target.getInventory().getHelmet();
                ItemStack chest = target.getInventory().getChestplate();
                ItemStack leggings = target.getInventory().getLeggings();
                ItemStack boots = target.getInventory().getBoots();
                ItemStack main = target.getInventory().getItemInMainHand();
                ItemStack off = target.getInventory().getItemInOffHand();
                if (validItems.contains(helmet.getType().toString())){
                    if (validBerries.contains(helmet.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(helmet.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,helmet);
                    }
                }
                if (validItems.contains(chest.getType().toString())){
                    if (validBerries.contains(chest.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(chest.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,chest);    
                    }
                }
                if (validItems.contains(leggings.getType().toString())){
                    if (validBerries.contains(leggings.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(leggings.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,leggings);    
                    }
                }
                if (validItems.contains(boots.getType().toString())){
                    if (validBerries.contains(boots.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(boots.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,boots);    
                    }
                }
                if (validItems.contains(main.getType().toString())){
                    if (validBerries.contains(main.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(main.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,main);    
                    }
                }
                if (validItems.contains(off.getType().toString())){
                    if (validBerries.contains(off.getItemMeta().getLore().get(0)) && plugin.getConfigManager().getBerryUses(off.getItemMeta().getLore().get(0)).contains("damage")){
                        plugin.executeBerry(event,target,off);    
                    }
                }
            }
        }
    }


}
