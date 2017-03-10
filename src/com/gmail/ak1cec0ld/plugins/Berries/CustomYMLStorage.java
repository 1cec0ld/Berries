package com.gmail.ak1cec0ld.plugins.Berries;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomYMLStorage {
    private File file = null;
    private YamlConfiguration yml = null;
    public CustomYMLStorage(JavaPlugin plugin, String path){
        file = new File(plugin.getDataFolder().getParent()+File.separator+path);
        yml = YamlConfiguration.loadConfiguration(file);
    }
    public File getFile(){
        return this.file;
    }
    public YamlConfiguration getYamlConfiguration(){
        return this.yml;
    }
}
