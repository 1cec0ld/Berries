package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private FileConfiguration config;
    
    ConfigManager(Berries plugin){
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public List<?> getBerryRegions(){
        return config.getList("berryPatches");
    }
    public Set<String> getValidBerries(){
        return config.getConfigurationSection("berries").getKeys(false);
    }
    public int getBerryStalkType(String berryname){
        return config.getInt("berries."+berryname.replace('§', '&')+".stalktype", 59);
    }
    public String getBerryUses(String berryname){
        return config.getString("berries."+berryname.replace('§', '&')+".uses","");
    }
    public String getBerryEffects(String berryname){
        return config.getString("berries."+berryname.replace('§', '&')+".effects","");
    }
    public int getBerryGrowDelayChance(String berryname){
        return config.getInt("berries."+berryname.replace('§', '&')+".growthdelaychance",0);
    }
    public String getBerryFlavor(String berryname){
        return config.getString("berries."+berryname.replace('§', '&')+".flavor","");
    }
    public HashMap<String, Integer> getBerryDropChances(){
        HashMap<String,Integer> results = new HashMap<String,Integer>();
        for(String x:config.getConfigurationSection("berries").getKeys(false)){
            results.put(x.replace('§', '&'), config.getInt("berries."+x.replace('§', '&')+".spawnweight"));
        }
        return results;
    }
    public List<?> getAttachableItems(){
        return config.getList("berryAttachable");
    }
}
