package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class StorageManager {
    
    String pathName;
    File storageFile;
    YamlConfiguration storage;
    Berries plugin;
    
    public StorageManager(Berries plugin){
        this.plugin = plugin;
        String dataFolder = plugin.getDataFolder().getPath();
        this.pathName = dataFolder+File.separator + "storage.yml";
        storageFile = new File(pathName);
        this.storage = YamlConfiguration.loadConfiguration(storageFile);
    }
    
  //file format
    //x,y,z:
    //  ownername
    //  berrytype

    public String getOwner(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        return storage.getString(x+","+z+".owner");
    }
    public String getBerryTypeAt(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        return ChatColor.translateAlternateColorCodes('&', storage.getString(x+","+z+".berrytype"));
    }
    public void addFileEntry(String berryname, Location loc, Player berryowner) {
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        storage.set(x+","+z+".owner", berryowner.getName());
        storage.set(x+","+z+".berrytype", berryname.replace('§', '&'));
        this.save();
    }
    public void removeFileEntry(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        storage.set(x+","+z, null);
        this.save();
    }
    public void save(){
        try {
            this.storage.save(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
