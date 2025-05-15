package fr.lataverne.randomreward;

import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.commands.CommandManager;
import fr.lataverne.randomreward.controllers.RewardListController;
import fr.lataverne.randomreward.models.Reward;
import fr.lataverne.randomreward.models.RewardDB;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class RandomReward extends JavaPlugin {
    private static ConfigManager configManager;
    private CommandManager commandManager;
    private static RandomReward instance;
    private RewardListController rewardListController;
    private List<RewardDB> rewardDBList;
    private ApiRequestManager apiRequestManager;

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

        // Vérifie et copie le fichier rewards.yml si nécessaire
        if (checkAndCreateRewardFile()) {
            // Initialise le RewardListController uniquement si le fichier est correctement préparé
            rewardListController = new RewardListController(configManager.getPathRewardFile());
        }

        // Initialise la gestion de l'API
        apiBuild();

        // Enregistre les commandes
        runCommands();
    }

    private void apiBuild() {
        this.apiRequestManager = new ApiRequestManager();
    }

    private void runCommands() {
        commandManager = new CommandManager();
        try {
            Objects.requireNonNull(this.getCommand("randomreward")).setExecutor(commandManager);
        } catch (NullPointerException ex) {
            getLogger().log(Level.SEVERE, "Error loading RandomReward command", ex);
            this.setEnabled(false);
        }
    }

    private void setupConfig() {
        try {
            // Utilisation d'une classe pour gérer la logique de configuration
            configManager = new ConfigManager(this);
        } catch (InterruptedException e) {
            getLogger().log(Level.SEVERE, "Configuration setup failed: " + e.getMessage());
        }
    }

    private boolean checkAndCreateRewardFile() {
        String fileRewardsName = "rewards.yml";
        File rewardFile = new File(getDataFolder(), fileRewardsName);

        // Si le fichier n'existe pas, on le copie depuis les ressources
        if (!rewardFile.exists()) {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();  // Crée le dossier de données si nécessaire
            }

            // Copie du fichier depuis les ressources
            if (copyResourceFile(fileRewardsName, rewardFile)) {
                getLogger().info("Le fichier "+fileRewardsName+" a été copié depuis les ressources.");
                return true;
            } else {
                getLogger().warning("Le fichier "+fileRewardsName+" est introuvable dans les ressources !");
                return false;
            }
        } else {
            getLogger().info("Le fichier "+fileRewardsName+" existe déjà.");
            return true;
        }
    }

    private boolean copyResourceFile(String resourceName, File destination) {
        try (InputStream resourceStream = getResource(resourceName)) {
            if (resourceStream != null) {
                Files.copy(resourceStream, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Erreur lors de la copie du fichier depuis les ressources : " + e.getMessage());
        }
        return false;
    }

    // Méthode reload()
    public void reload() {
        getLogger().info("Rechargement des configurations et des récompenses...");

        // Recharge la configuration
        setupConfig();

        // Recharge le fichier rewards.yml si nécessaire
        if (checkAndCreateRewardFile()) {
            // Réinitialise le RewardListController
            rewardListController = new RewardListController(configManager.getPathRewardFile());
        }

        getLogger().info("Rechargement terminé.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward désactivé");
    }

    public List<Reward> getRewardList() {
        return rewardListController.getRewardList();
    }
}
