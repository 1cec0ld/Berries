package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomYMLStorage {
    private File file = null;
    private YamlConfiguration yml = null;
    public CustomYMLStorage(JavaPlugin plugin, String path){
        file = new File(plugin.getDataFolder().getParent()+File.separator+path);
        yml = YamlConfiguration.loadConfiguration(file);
        this.save();
    }
    public File getFile(){
        return this.file;
    }
    public YamlConfiguration getYamlConfiguration(){
        return this.yml;
    }
    public void setYamlConfiguration(YamlConfiguration myYml){
        yml = myYml;
    }
    public void save(){
        try{
            this.yml.save(file);
        } catch(IOException e){}
    }
}
