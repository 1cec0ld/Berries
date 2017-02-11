package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class StorageManager {
    
    private String pathName;
    private File storageFile;
    private YamlConfiguration storage;
    public HashMap<String,String> storedBerries;
    
    public StorageManager(Berries plugin){
        String dataFolder = plugin.getDataFolder().getPath();
        this.pathName = dataFolder+File.separator + "storage.yml";
        storageFile = new File(pathName);
        this.storage = YamlConfiguration.loadConfiguration(storageFile);
        storedBerries = new HashMap<String,String>();
        for(String s : storage.getKeys(false)){
            storedBerries.put(s, storage.getString(s+".berrytype"));
        }
    }
    
  //file format
    //x,z:
    //  ownername: planter.getName()
    //  berrytype: berryname (without color)

    public String getOwner(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        return storage.getString(x+","+z+".owner");
    }
    public String getBerryTypeAt(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        return storage.getString(x+","+z+".berrytype");
    }
    public void addFileEntry(String berryname, Location loc, Player berryowner) {
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        storage.set(x+","+z+".owner", berryowner.getName());
        storage.set(x+","+z+".berrytype", ChatColor.stripColor(berryname));
        storedBerries.put(x+","+z, ChatColor.stripColor(berryname));
        this.save();
    }
    public void removeFileEntry(Location loc){
        int x = (int) loc.getX();
        int z = (int) loc.getZ();
        storage.set(x+","+z, null);
        storedBerries.remove(x+","+z);
        this.save();
    }
    public void save(){
        try {
            this.storage.save(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //to-do: create verification method to make sure HashMap_storedBerries matches File_storage.yml
}
