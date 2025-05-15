package fr.lataverne.randomreward.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.models.RewardDB;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RewardService {

    /**
     * Get reward by uuid
     *
     * @param uuid   uuid player
     * @param config
     * @return List<Map < String, Object>>
     * @throws Exception error RewardService get
     */
    public static List<Map<String, Object>> getRewards(String uuid, ConfigManager config) throws Exception {
        // Définir l'URL de l'API
        //-> String urlString = "http://localhost/ApiRandomReward/getRewards.php"; // Remplacez par l'URL réelle
        //System.out.println("config.getUrlReward() : "+config.getUrlReward());
        URL url = new URL(config.getUrlReward());

        // Ouvrir la connexion
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+config.getTokenReward()); // Remplacez par votre token sécurisé
        con.setDoOutput(true);

        // Construire le JSON avec l'uuid
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
                "action","get",
                "uuid", uuid
        ));

        // Envoyer les données
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Lire la réponse du serveur
        int responseCode = con.getResponseCode();

        // Vérification du code de réponse HTTP
        //InputStream is = (responseCode >= 200 && responseCode < 300) ? con.getInputStream() : con.getErrorStream();
        //System.out.println(responseDebug(is));

        if (responseCode == HttpURLConnection.HTTP_OK) { // Vérifier si la requête a réussi (200 OK)
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Convertir la réponse JSON en liste d'objets Java
            return objectMapper.readValue(response.toString(), new TypeReference<List<Map<String, Object>>>() {});
        } else {
            throw new RuntimeException("Erreur HTTP : " + responseCode);
        }
    }

    /***
     * Delete Reward
     * @param id id reward
     * @param config
     * @throws Exception error RewardService delete
     */
    public static void deleteReward(int id, ConfigManager config) throws Exception {
        // Définir l'URL de l'API
        //-> String urlString = "http://localhost/ApiRandomReward/getRewards.php"; // Remplacez par l'URL réelle
        //System.out.println("config.getUrlReward() : "+config.getUrlReward());
        URL url = new URL(config.getUrlReward());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+config.getTokenReward());
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
                "action", "delete",
                "id", id
        ));

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInputString.getBytes());
            os.flush();
        }
        if(config.getDebug()) {
            int responseCode = con.getResponseCode();
            System.out.println("Réponse du serveur : " + responseCode);
        }
    }

    /**
     * @param uuid   player's uuid
     * @param plugin plugin's name of item
     * @param item   item's name
     * @param count  item's number
     * @param config
     * @throws Exception error RewardService add
     */
    public static void addReward(String uuid, String plugin, String item, int count, ConfigManager config) throws Exception {
        // Définir l'URL de l'API
        //-> String urlString = "http://localhost/ApiRandomReward/getRewards.php"; // Remplacez par l'URL réelle
        //System.out.println("config.getUrlReward() : "+config.getUrlReward());
        URL url = new URL(config.getUrlReward());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Configuration de la requête
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+config.getTokenReward());
        con.setDoOutput(true);

        // Création de l'objet JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
                "action", "add",
                "uuid", uuid,
                "plugin", plugin,
                "item", item,
                "count", count
        ));

        // Envoi des données
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInputString.getBytes());
            os.flush();
        }

        if(config.getDebug()) {
            // Lecture de la réponse
            int responseCode = con.getResponseCode();
            //System.out.println("Réponse du serveur : " + responseCode);

            // Vérification du code de réponse HTTP
            InputStream is = (responseCode >= 200 && responseCode < 300) ? con.getInputStream() : con.getErrorStream();
            //System.out.println(responseDebug(is));
        }
    }

    // not Use
    public static void main(String[] args, ConfigManager config) {
        //Exemple getReward (get bag)
        try {
            List<Map<String, Object>> rewards = getRewards("39c8a914-abc8-4c09-a736-deff9bcb6017", config);
            //System.out.println("Récompenses reçues : " + rewards);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            addReward("123e4567-e89b-12d3-a456-426614174000", "SuperPlugin", "Diamond Sword", 1, config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Exemple delete reward
        try {
            deleteReward(5, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read response if debug=on work project java !! Create warning minecraft console server
     * @param is the response HTTP
     * @return String get (php response)
     */
    private static String responseDebug(InputStream is) {
        // Lecture de la réponse
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addReward(RewardDB reward, ConfigManager config) throws Exception {
        addReward( reward.uuid, reward.plugin, reward.item, reward.count, config);
    }

    public static RewardDB getReward(int idReward, ConfigManager config) throws Exception {
        //System.out.println("config.getUrlReward() : " + config.getUrlReward());
        URL url = new URL(config.getUrlReward());

        // Connexion HTTP
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + config.getTokenReward());
        con.setDoOutput(true);

        // Création du JSON avec l'idReward
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
                "action", "getReward",
                "idReward", idReward
        ));

        // Envoi de la requête
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Lecture de la réponse
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Conversion JSON → RewardDB
            return objectMapper.readValue(response.toString(), RewardDB.class);

        } else {
            throw new RuntimeException("Erreur HTTP : " + responseCode);
        }
    }

}
