package fr.lataverne.randomreward;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigManager {
    private final RandomReward plugin;
    FileConfiguration config ;

//    public ConfigManager(RandomReward plugin) throws InterruptedException {
//        this.plugin = plugin;
//        this.config = plugin.getConfig();
//
//        // Sauvegarde la configuration par défaut si elle n'existe pas
//        plugin.saveDefaultConfig();
//        plugin.reloadConfig();
//
//        if (!checkConfiguration()) {
//            throw new InterruptedException("Config.yml invalid or null");
//        }
//    }

    public ConfigManager(RandomReward plugin) throws InterruptedException {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        // Sauvegarde initiale si le fichier n'existe pas
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        // Vérifie la validité de la config
        if (!checkConfiguration()) {
            EnvironmentDetector.log("Config.yml invalide. Sauvegarde de l'ancien fichier et création d'une nouvelle configuration.");

            // Renommage de l'ancien fichier config.yml
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            renameFileWithTimestamp(configFile);

            // Re-génère la config vierge
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            this.config = plugin.getConfig(); // recharge dans l'objet actuel
        }
    }

    /**
     * Check if congif file contain all field and not empty
     * @return true  if config is valid
     */
    private boolean checkConfiguration() {
        boolean fielsExist = config.contains("tokenNotification",true) &&
                config.contains("urlNotification",true) &&
                config.contains("tokenReward",true) &&
                config.contains("urlReward",true ) &&
                config.contains("urlGetVote",true) &&
                config.contains("pathFilesJson",true);

        boolean fielsNotEmpty = getTokenNotification() != null &&
                getUrlNotification() != null &&
                getTokenReward() != null &&
                getUrlReward() != null  &&
                getUrlGetVote() != null &&
                getPathFilesJson() != null ;

        return fielsExist && fielsNotEmpty;
    }

    public void reloadConfig() throws IOException {
        // Recharge la configuration
        plugin.reloadConfig();
    }

    public String getTokenNotification() {
        return config.getString("tokenNotification");
    }

    public String getUrlNotification() {
        return config.getString("urlNotification");
    }

    public String getTokenReward() {
        return config.getString("tokenReward");
    }

    public String getUrlReward() {
        return config.getString("urlReward");
    }

    public boolean getDebug() {
        return "enable".equals(config.getString("debug"));
    }

    public String getUrlGetVote()  {
        return config.getString("urlGetVote");
    }

    public String getPathFilesJson(){
        return config.getString("pathFilesJson");
    }

    public static boolean renameFileWithTimestamp(File originalFile) {
        if (originalFile == null || !originalFile.exists()) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String timestamp = dateFormat.format(new Date());

        String parent = originalFile.getParent();
        String name = originalFile.getName();

        File renamedFile = getFile(name, parent, timestamp);

        return originalFile.renameTo(renamedFile);
    }

    private static @NotNull File getFile(String name, String parent, String timestamp) {
        int dotIndex = name.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : name.substring(dotIndex);

        File renamedFile = new File(parent, baseName + "-" + timestamp + extension);

        // Protection : si le fichier existe déjà, on ajoute des "x"
        while (renamedFile.exists()) {
            baseName += "x";
            renamedFile = new File(parent, baseName + "-" + timestamp + extension);
        }
        return renamedFile;
    }

    public String getPathRewardFile() {
        return config.getString("pathFileReward");
    }
}

