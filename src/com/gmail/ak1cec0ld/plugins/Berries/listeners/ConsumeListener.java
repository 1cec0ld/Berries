package com.gmail.ak1cec0ld.plugins.Berries.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.gmail.ak1cec0ld.plugins.Berries.Berries;

public class ConsumeListener implements Listener{
    private Berries plugin;
    
    public ConsumeListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if (player.hasPermission("pokeitems.berries.use")){
            if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasLore()){
                String item_lore_0 = event.getItem().getItemMeta().getLore().get(0);
                if (plugin.getConfigManager().getValidBerriesWithColors().contains(item_lore_0) && plugin.getConfigManager().getBerryUses(item_lore_0).contains("consume")){
                    plugin.executeBerry(event, player, event.getItem());
                }
            }
        }
    }
}
