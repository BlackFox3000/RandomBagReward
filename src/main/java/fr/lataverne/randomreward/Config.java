package fr.lataverne.randomreward;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class Config extends RandomReward{
    private String debug = "disabled";

    private static Config instance;
    private Map<String, Object> configMap;

    public String getConfigValue(String key) {
        return (String) configMap.get(key);
    }

    // Méthode pour obtenir l'instance unique de la classe Config
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    // Constructeur privé pour empêcher l'instanciation directe
    private Config() {
        createOrReadConfSendRequest("/RandomReward/config.yml");
    }

    private  void createOrReadConfSendRequest(String filePath) {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                debugPrint("creation du fichier conf!! ");
                debugPrint("! FICHIER A CONFIGURER POUR TOP VOTE !");

                file.getParentFile().mkdirs(); // Créer les répertoires parents si nécessaire
                file.createNewFile(); // Créer le fichier

                // Écrire les valeurs par défaut dans le fichier YAML
                FileWriter writer = new FileWriter(file);

                // Écrire les valeurs par défaut dans le fichier YAML
                writer.write("#Notification vote\n");
                writer.write("passPhraseVote: mapassphrase:8000\n");
                writer.write("urlSiteNotification: monurl\n");
                writer.write("##Selection du type de stockage (json/api)");
                writer.write("#par defaut par json\n");
                writer.write("storageMode: json\n");
                writer.write("#Connexion BDD\n");
                writer.write("passPhraseStorage: mapassphrase:8000\n");
                writer.write("urlApiStorage: urlApiStorage\n");
                writer.write("#Debug (enable/disable)\n");
                writer.write("debug: enable\n");
                writer.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readYaml(file.getPath());
    }

    public void readYaml(String filePath) {
        try {
            // Créer un InputStream à partir du fichier YAML
            InputStream input = new FileInputStream(filePath);

            // Créer un objet Yaml
            Yaml yaml = new Yaml();

            // Charger les données YAML dans une structure de données Map
            this.configMap = yaml.load(input);

        } catch (FileNotFoundException e) {
            // Gérer l'erreur si le fichier n'est pas trouvé
            e.printStackTrace();
        }
    }

    public boolean isDebug(){
        return !Objects.equals(debug, "disable");
    }

    public void debugPrint(String string){
        if(isDebug())
            Bukkit.getConsoleSender().sendMessage(string);
    }

}
