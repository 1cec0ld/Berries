package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class StorageManager {
    
    YamlConfiguration storage;
    
    public StorageManager(){
        File f = new File("storage.yml");
        this.storage = YamlConfiguration.loadConfiguration(f);
    }
    
    //x,y,z:
    //  ownername
    //  berrytype
    
    public String getOwner(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        return storage.getString(x+","+y+","+z+".owner");
    }
    public String getBerryTypeAt(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        return storage.getString(x+","+y+","+z+".berrytype");
    }
    public void addFileEntry(String berryname, Location loc, Player berryowner) {
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        storage.set(x+","+y+","+z+".owner", berryowner);
        storage.set(x+","+y+","+z+".berrytype", berryname);
    }
}
