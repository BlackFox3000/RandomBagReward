package fr.lataverne.randomreward;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class ConfigManager {
    private final RandomReward plugin;
    FileConfiguration config ;

    public ConfigManager(RandomReward plugin) throws InterruptedException {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        // Sauvegarde la configuration par défaut si elle n'existe pas
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        if (!checkConfiguration()) {
            throw new InterruptedException("Config.yml invalid or null");
        }
    }

    /**
     * Check if congif file contain all field and not empty
     * @return true  if config is valid
     */
    private boolean checkConfiguration() {
        boolean fielsExist = config.contains("passPhraseNotification",true) &&
                config.contains("urlNotification",true) &&
                config.contains("passPhraseStorage",true) &&
                config.contains("urlStorage",true);

        boolean fielsNotEmpty = getPassPhraseNotification() != null &&
                getUrlNotification() != null &&
                getPassPhraseStorage() != null &&
                getUrlStorage() != null ;

        return fielsExist && fielsNotEmpty;
    }

    public void reloadConfig() throws IOException {
        // Recharge la configuration
        plugin.reloadConfig();
        System.out.println("test: " + getPassPhraseNotification());
    }

    public String getPassPhraseNotification() {
        return config.getString("passPhraseNotification");
    }

    public String getUrlNotification() {
        return config.getString("urlNotification");
    }

    public String getPassPhraseStorage() {
        return config.getString("passPhraseStorage");
    }

    public String getUrlStorage() {
        return config.getString("urlStorage");
    }

    public boolean getDebug() {
        return "enable".equals(config.getString("debug"));
    }

}

