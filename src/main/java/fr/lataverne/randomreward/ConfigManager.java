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
                config.contains("urlStorage",true ) &&
                config.contains("urlGetVotes",true) &&
                config.contains("urlGetVote",true);

        boolean fielsNotEmpty = getTokenNotification() != null &&
                getUrlNotification() != null &&
                getPassPhraseStorage() != null &&
                getUrlStorage() != null  &&
                getUrlGetVote() != null ;

        return fielsExist && fielsNotEmpty;
    }

    public void reloadConfig() throws IOException {
        // Recharge la configuration
        plugin.reloadConfig();
        System.out.println("test: " + getTokenNotification());
    }

    public String getTokenNotification() {
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

    public String getUrlGetVote()  {
        return config.getString("UrlGetVote");
    }

    public String getUrlGetVotes()  {
        return config.getString("UrlGetVotes");
    }

}

