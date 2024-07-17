package fr.lataverne.randomreward;


import fr.lataverne.randomreward.api.ApiRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class RandomReward extends JavaPlugin {
    private static ConfigManager configManager;

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public void onEnable() {
        // Configure la configuration
        setupConfig();
        ApiRequestManager.getApiConnection();

    }

    private void setupConfig(){
        try {
            // Utilisation d'une classe pour gérer la logique de configuration
            this.configManager = new ConfigManager(this);
        }catch (InterruptedException e){
            getLogger().log(Level.SEVERE,e.getMessage());
        }


    }



    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward désactivé");
    }


}
