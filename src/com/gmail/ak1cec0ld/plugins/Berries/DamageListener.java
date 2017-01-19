package com.gmail.ak1cec0ld.plugins.Berries;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener {
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
                attacker.getInventory().getHelmet();
                attacker.getInventory().getChestplate();
                attacker.getInventory().getLeggings();
                attacker.getInventory().getBoots();
                attacker.getInventory().getItemInMainHand();
                attacker.getInventory().getItemInOffHand();
                //attacker.is.player
            }
        }
        if (event.getEntity() instanceof Player){
            Player target = (Player) event.getEntity();
            //target.is.player
        }
    }
}
