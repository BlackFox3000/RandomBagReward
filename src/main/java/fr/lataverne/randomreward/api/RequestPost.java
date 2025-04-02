package fr.lataverne.randomreward.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.models.Reward;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestPost {
    static boolean DEBUG = true;
    public static ConfigManager config;

    public RequestPost() {
        config = RandomReward.getInstance().getConfigManager();
    }

    /***
     * Transform data to Json object and send it to url.
     * @param url
     * @param data
     * @throws Exception
     */
    public static void envoyerRequetePOST(String url, Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(data);
        if (DEBUG) {
            System.out.println(jsonBody);
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Paramètres de la requête
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Envoi des données JSON
        OutputStream os = con.getOutputStream();
        os.write(jsonBody.getBytes());
        os.flush();
        os.close();

        // Lecture de la réponse
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (DEBUG) {
            // Affichage de la réponse
            System.out.println(response.toString());
        }
    }

    public static boolean sendRewardPost(String pseudo, Reward reward) {
        String url = config.getUrlStorage();
        try {
            envoyerRequetePOST(url, reward);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendNotificationVotePost(String pseudo) throws Exception {
        // Création de l'objet JSON
        Map<String, String> data = new HashMap<>();
        data.put("pseudo", pseudo);

        // Conversion en JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(data);

        // Connexion HTTP
        URL obj = new URL(config.getUrlNotification()+"/voteNotification.php");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Configuration de la requête
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + config.getTokenNotification()); // Ajout du token sécurisé
        con.setDoOutput(true);

        // Envoi des données
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }

        // Lecture de la réponse
        int responseCode = con.getResponseCode();
        System.out.println("Réponse du serveur : " + responseCode);
    }

    public static void sendGet(String url) throws Exception {

        // Connexion HTTP
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Configuration de la requête
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + config.getTokenNotification()); // Ajout du token sécurisé
        con.setDoOutput(false);

        // Lecture de la réponse
        int responseCode = con.getResponseCode();
        System.out.println("Réponse du serveur : " + responseCode);

        // Si la réponse est 200 (OK), on peut récupérer le nombre de votes
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Afficher la réponse, qui peut contenir le nombre de votes
                System.out.println("Réponse du serveur : " + response.toString());
            }
        } else {
            System.out.println("Erreur lors de la récupération des votes");
        }
    }
}