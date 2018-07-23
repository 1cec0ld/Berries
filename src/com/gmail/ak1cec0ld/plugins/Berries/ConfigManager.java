package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private YamlConfiguration config;
    private CustomYMLStorage yml = null;
    
    ConfigManager(Berries plugin){
        yml = new CustomYMLStorage(plugin,"PokeItems"+File.separator+"berryConfig.yml");
        config = yml.getYamlConfiguration();
        yml.save();
    }
    
    public List<?> getBerryRegions(){
        return config.getList("berryPatches");
    }
    public Set<String> getValidBerries(){
        return config.getConfigurationSection("berries").getKeys(false);
    }
    public Set<String> getValidBerriesWithColors(){
        Set<String> myberries = new HashSet<String>();
        for (String plainTextBerry : config.getConfigurationSection("berries").getKeys(false)){
            String combine = ChatColor.translateAlternateColorCodes('&', "&"+getBerryColor(plainTextBerry)+plainTextBerry);
            myberries.add(combine);
        }
        return myberries;
    }
    public String getBerryColor(String berryname) {
        return config.getString("berries."+berryname+".color","2");
    }
    public Material getBerryStalkType(String berryname){
        return Material.valueOf(config.getString("berries."+ChatColor.stripColor(berryname)+".stalktype", "WHEAT"));
    }
    public String getBerryUses(String berryname){
        return config.getString("berries."+ChatColor.stripColor(berryname)+".uses","");
    }
    public String getBerryEffects(String berryname){
        return config.getString("berries."+ChatColor.stripColor(berryname)+".effects","");
    }
    public int getBerryGrowDelayChance(String berryname){
        return config.getInt("berries."+ChatColor.stripColor(berryname)+".growthdelaychance",0);
    }
    public String getBerryFlavor(String berryname){
        return config.getString("berries."+ChatColor.stripColor(berryname)+".flavor","");
    }
    public HashMap<String, Integer> getBerryDropChances(){
        HashMap<String,Integer> results = new HashMap<String,Integer>();
        for(String plainTextBerry:config.getConfigurationSection("berries").getKeys(false)){
            results.put(ChatColor.translateAlternateColorCodes('&', "&"+getBerryColor(plainTextBerry)+plainTextBerry), config.getInt("berries."+plainTextBerry+".spawnweight"));
        }
        return results;
    }
    public List<Integer> getBerryDropRates(String berryname, String status){
        return config.getIntegerList("berries."+ChatColor.stripColor(berryname)+".droprates."+status);
    }
    public List<String> getAttachableItems(){
        return config.getStringList("berryAttachable");
    }
}
