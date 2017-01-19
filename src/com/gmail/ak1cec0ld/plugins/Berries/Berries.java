package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.Set;
import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Berries extends JavaPlugin{
    
    private WorldGuardPlugin WG;
    private ConfigManager configManager;
    private StorageManager storageManager;
    
    public void onEnable(){
        this.WG = this.setWorldGuard();
        this.configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new LeafDecayListener(this), this);
        getServer().getPluginManager().registerEvents(new ConsumeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFadeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFlowListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPhysicsListener(this), this);
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
    public ConfigManager getConfigManager(){
        return this.configManager;
    }
    public StorageManager getStorageManager(){
        return this.storageManager;
    }
    
    public boolean isInBerryPatch(Block block){
        ApplicableRegionSet blockRegions = getWorldGuard().getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
        Set<String> myRegions = getConfigManager().getBerryRegions();
        if (blockRegions.size() == 0){
            return false;
        } else {
            for (ProtectedRegion r : blockRegions){
                if (myRegions.contains(r.getId())){
                    return true;
                }
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
}
