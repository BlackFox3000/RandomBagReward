package fr.lataverne.randomreward;


import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class RandomReward extends JavaPlugin {
    private static ConfigManager configManager;
    private CommandManager commandManager;

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public void onEnable() {
        setupConfig();
        runCommands();

        //ApiRequestManager.getApiConnection();

    }

    private void runCommands() {
        commandManager = new CommandManager();
        try {
            Objects.requireNonNull(this.getCommand("randomreward")).setExecutor(commandManager);
        } catch (NullPointerException ex) {
            Bukkit.getConsoleSender().sendMessage("Error !!!!");
            this.setEnabled(false);
        }
    }

    private void setupConfig() {
        try {
            // Utilisation d'une classe pour gérer la logique de configuration
            this.configManager = new ConfigManager(this);
        } catch (InterruptedException e) {
            getLogger().log(Level.SEVERE, e.getMessage());
        }


    }


    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward désactivé");
    }


}
