package fr.lataverne.randomreward;


import com.fasterxml.jackson.databind.DatabindContext;
import fr.lataverne.randomreward.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class RandomReward extends JavaPlugin {
    private static ConfigManager configManager;
    private CommandManager commandManager;
    private static RandomReward instance;

    public static RandomReward getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        instance = this;
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
