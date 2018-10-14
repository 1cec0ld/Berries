package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockBreakListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockFadeListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockFlowListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockGrowListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockPhysicsListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.BlockPlaceListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.ConsumeListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.DamageListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.InteractListener;
import com.gmail.ak1cec0ld.plugins.Berries.listeners.LeafDecayListener;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class Berries extends JavaPlugin{
    
    private WorldGuardPlugin WG;
    private ConfigManager configManager;
    private StorageManager storageManager;
    
    public void onEnable(){
        this.WG = this.setWorldGuard();
        this.configManager = new ConfigManager(this);
        this.storageManager = new StorageManager(this);
        
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFadeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFlowListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockGrowListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPhysicsListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ConsumeListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new LeafDecayListener(this), this);
        
        getServer().getPluginCommand("berries").setExecutor(new CommandManager(this));
    }
    
    private WorldGuardPlugin setWorldGuard(){
        Plugin WGuard = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if ((WGuard == null) || (!(WGuard instanceof WorldGuardPlugin))){
            this.getLogger().log(Level.SEVERE, "WorldGuard Not Found!!!!");
            return null;
        }
        this.getLogger().log(Level.INFO, "WorldGuard Plugin Loaded!");
        return (WorldGuardPlugin)WGuard;
    }
    
    public WorldGuardPlugin getWorldGuard(){
        return this.WG;
    }
    public RegionQuery getWorldGuardUsefulPart(){
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    }
    public ConfigManager getConfigManager(){
        return this.configManager;
    }
    public StorageManager getStorageManager(){
        return this.storageManager;
    }
    
    public boolean isInBerryPatch(Block block){
        ApplicableRegionSet blockRegions = getWorldGuardUsefulPart().getApplicableRegions(BukkitAdapter.adapt(block.getLocation()));
        List<?> myRegions = getConfigManager().getBerryRegions();
        if (blockRegions.size() == 0){
            return false;
        } else {
            for (ProtectedRegion r : blockRegions){
                for (Object regionName : myRegions){
                    if (((String)regionName).equalsIgnoreCase(r.getId())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isBerryName(String berryname){
        for (String names:getConfigManager().getValidBerries()){
            if (berryname.toLowerCase().equals(names.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    public boolean flavorConfuses(String flavor, String uuid){
        Integer taste = 0;
        for (char hex : uuid.toCharArray()){
            taste+= Character.digit(hex, 16);
        }
        taste %= 5;
        return ((flavor.equalsIgnoreCase("bitter") && taste == 0) || 
                (flavor.equalsIgnoreCase("spicy") && taste == 1) ||
                (flavor.equalsIgnoreCase("sour") && taste == 2) ||
                (flavor.equalsIgnoreCase("sweet") && taste == 3) ||
                (flavor.equalsIgnoreCase("dry") && taste == 4));
    }
    
    public void executeBerry(Event event, Player executor, ItemStack item) {
        String uses = getConfigManager().getBerryUses(item.getItemMeta().getLore().get(0));
        String effects = getConfigManager().getBerryEffects(item.getItemMeta().getLore().get(0));
        Boolean use = true;
        if (uses.contains("lowhealth")&&executor.getHealth()>5){
            use = false;
        } else if (uses.contains("middlehealth")&&executor.getHealth()>8){
            use = false;
        } else if (uses.contains("highhealth")&&executor.getHealth()>10){
            use = false;
        } else if (uses.contains("criticalhit")&&executor.getFallDistance()<=0){
            use = false;
        } else if (uses.contains("itemdurability")&&item.getDurability() < item.getType().getMaxDurability()/2){
            getLogger().log(Level.INFO, "ItemDurability wasn't low enough");
            use = false;
        } else if (uses.contains("physicaldamage")&&event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent t = (EntityDamageByEntityEvent)event;
            if (t.getDamager() instanceof Projectile){
                use = false;
            }
        } else if (uses.contains("rangeddamage")&&event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent t = (EntityDamageByEntityEvent)event;
            if (!(t.getDamager() instanceof Projectile)){
                use = false;
            }
        }
        if(use || event instanceof PlayerItemConsumeEvent){
            Boolean used = false;
            if (effects.contains("smallheal")){
                used = true;
                executor.setHealth(Math.min(executor.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), executor.getHealth()+4));
                executor.sendMessage("You've been healed a little!");
            }
            if (effects.contains("largeheal")){
                used = true;
                executor.setHealth(Math.min(executor.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), executor.getHealth()+10));
                executor.sendMessage("You've been healed a lot!");
            }
            if (effects.contains("confusionflavor")){
                used = true;
                String flavor = getConfigManager().getBerryFlavor(item.getItemMeta().getLore().get(0));
                if (flavorConfuses(flavor, executor.getUniqueId().toString())){
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,300,1));
                    executor.sendMessage("You disliked the Flavor!");
                }
            }
            if (effects.contains("cureslow")){
                used = true;
                executor.removePotionEffect(PotionEffectType.SLOW);
            }
            if (effects.contains("curepoison")){
                used = true;
                executor.removePotionEffect(PotionEffectType.POISON);
            }
            if (effects.contains("cureconfusion")){
                used = true;
                executor.removePotionEffect(PotionEffectType.CONFUSION);
            }
            if (effects.contains("cureburn")){
                used = true;
                executor.setFireTicks(0);
            }
            if (effects.contains("boostspeed")){
                used = true;
                executor.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
            }
            if (effects.contains("boostdamage")){
                if (event instanceof EntityDamageEvent){
                    used = true;
                    EntityDamageEvent e = (EntityDamageEvent)event;
                    e.setDamage(e.getDamage()+4);
                }
            }
            if (effects.contains("fixitem")){
                used = true;
                item.setDurability((short) (item.getDurability()-(.25*item.getType().getMaxDurability())));
                executor.sendMessage("Your item was repaired some!");
            }
            if (effects.contains("boostmcmmoluck")){
                used = true;
                getServer().dispatchCommand(getServer().getConsoleSender(), "pex user "+executor.getName()+" timed add mcmmo.perks.lucky.all 10");
            }
            if (effects.contains("quarterdamage")){
                if (event instanceof EntityDamageEvent){
                    used = true;
                    EntityDamageEvent e = (EntityDamageEvent)event;
                    e.setDamage(e.getDamage()/4);
                }
            }
            if (effects.contains("reducedamage")){
                if (event instanceof EntityDamageEvent){
                    used = true;
                    EntityDamageEvent e = (EntityDamageEvent)event;
                    e.setDamage(e.getDamage()-5);
                }
            }
            if (effects.contains("addconfusion")){
                used = true;
                executor.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,1));
            }
            if(used){
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setLore(null);
                item.setItemMeta(itemMeta);
            }
        }
    }
}
