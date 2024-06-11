package mock;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ConfigMock {

    private static ConfigMock instance;
    private Map<String, Object> configMap;

    public String getConfigValue(String key) {
        return (String) configMap.get(key);
    }

    // Méthode pour obtenir l'instance unique de la classe Config
    public static ConfigMock getInstance() {
        if (instance == null) {
            instance = new ConfigMock();
        }
        return instance;
    }

    public Set<String> getKeysMap() {
        return configMap.keySet();
    }

    // Constructeur privé pour empêcher l'instanciation directe
    private ConfigMock() {
        createOrReadConfSendRequest("src/test/java/fr/lataverne/randomreward/config.yml");
    }

    private void createOrReadConfSendRequest(String filePath) {
        File file = new File("src/test/java/fr/lataverne/randomreward/config.yml");
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

    public boolean isDebug() {
        String debug = "disabled";
        return !Objects.equals(debug, "disable");
    }

    public void debugPrint(String string) {
        if (isDebug())
            System.out.println(string);
    }

    public void isValid() throws IOException {
        if (this.configMap == null) {
            throw new IOException("Configuration File ERROR : conf.yml not exist or can't read");
        }
        Set<String> keys = getKeysMap();
        boolean structuralValidity = (
                keys.contains("passPhraseVote") &&
                        keys.contains("urlSiteNotification") &&
                        keys.contains("storageMode") &&
                        keys.contains("passPhraseStorage") &&
                        keys.contains("urlApiStorage") &&
                        keys.contains("debug")
        );
        if (!structuralValidity) {
            throw new IOException("Structural Configuration ERROR : rebuild your conf.yml");
        }
        for (String key : keys) {
            if (getConfigValue(key) == null) {
                if (
                        Objects.equals(key, "passPhraseStorage")
                                || Objects.equals(key, "urlApiStorage")
                ) {
                    Objects.equals(getConfigValue("storageMode"), "json");
                } else {
                    throw new IOException("Structural Configuration ERROR : value " + key + " can't be empty or null");
                }
            }
        }

        boolean apiValidity = (
                !Objects.equals(getConfigValue("passPhraseStorage"), "")
                        && !Objects.equals(getConfigValue("urlApiStorage"), "")
        );
    }

}
