package com.gmail.ak1cec0ld.plugins.Berries;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConsumeListener implements Listener{
    private Berries plugin;
    
    public ConsumeListener(Berries plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if (player.hasPermission("md.pokeitems.berries.use")){
            String item_lore_0 = event.getItem().getItemMeta().getLore().get(0);
            if (plugin.getConfigManager().getValidBerries().contains(item_lore_0) && plugin.getConfigManager().getBerryUses(item_lore_0).contains("consume")){
                String effects = plugin.getConfigManager().getBerryEffects(item_lore_0);
                if (effects.contains("confusionflavor")){
                    String flavor = plugin.getConfigManager().getBerryFlavor(item_lore_0);
                    if (plugin.flavorConfuses(flavor, player.getUniqueId().toString())){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,300,1));
                    }
                }
                if (effects.contains("smallheal")){
                    player.setHealth((player.getHealth()< player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-4)?(player.getHealth()+4):player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                }
                if (effects.contains("cureslow")){
                    player.removePotionEffect(PotionEffectType.SLOW);
                }
                if (effects.contains("curepoison")){
                    player.removePotionEffect(PotionEffectType.POISON);
                }
                if (effects.contains("cureconfusion")){
                    player.removePotionEffect(PotionEffectType.CONFUSION);
                }
                if (effects.contains("cureburn")){
                    player.setFireTicks(0);
                }
                if (effects.contains("boostspeed")){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
                }
                if (effects.contains("largeheal")){
                    player.setHealth((player.getHealth()< player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-10)?(player.getHealth()+10):player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                }
            }
        }
    }
}
