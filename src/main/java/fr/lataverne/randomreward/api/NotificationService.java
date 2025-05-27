package fr.lataverne.randomreward.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.ConfigManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static fr.lataverne.randomreward.EnvironmentDetector.log;

public class NotificationService {

    private NotificationService() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void sendNotificationVotePost(String uuid, ConfigManager config) throws Exception {
        log("enter notif");
        // Création de l'objet JSON
        Map<String, String> data = new HashMap<>();
        data.put("uuid", uuid);

        // Conversion en JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(data);

        // Connexion HTTP
        URL obj = new URL(config.getUrlNotification());
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
        if(config.getDebug()) {
            int responseCode = con.getResponseCode();
            log("Réponse du serveur : " + responseCode);
        }
        log("out notif : "+obj);
    }

    public static int getVotesForUuid(String uuid,  ConfigManager config) throws Exception {
        System.out.println("uuid : "+uuid);
        return getVotesForUuid( uuid, null, config);
    }

    public static int getVotesForUuid(String uuid, String date, ConfigManager config) {
        String url = config.getUrlNotification() + "?uuid=" + uuid;
        if (date != null) {
            url += "&nbMonth=" + date;
        }

        //log("Requête GET vers : " + url); // Log de l'URL complète

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            String token = config.getTokenNotification();
            con.setRequestProperty("Authorization", "Bearer " + token);

            //log("Header Authorization: Bearer " + token);

            int responseCode = con.getResponseCode();
            if (config.getDebug()) log("GET getVotesForUuid -> Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);

                    String respStr = response.toString();
                    if (config.getDebug()) log("Réponse: " + respStr);

                    // Si la réponse contient une erreur explicite → retour -1
                    if (respStr.contains("\"error\"")) {
                        if (config.getDebug()) log("Aucun vote trouvé pour UUID: " + uuid);
                        return -1;
                    }

                    JSONArray jsonArray;

                    if (respStr.charAt(0) != '[') {
                        jsonArray = new JSONArray("[" + respStr + "]");
                    } else {
                        jsonArray = new JSONArray(respStr);
                    }

                    int totalVotes = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        if (uuid.equals(obj.optString("uuid"))) {
                            totalVotes += Integer.parseInt(obj.optString("nbVotes", "0"));
                        }
                    }

                    return totalVotes;
                }
            } else {
                if (config.getDebug()) log("Réponse HTTP non OK: " + responseCode);
                return -1;
            }

        } catch (Exception e) {
            log("Erreur lors de la récupération des votes : " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }





    public static Map<String, Integer> getAllVotes(ConfigManager config) throws Exception{
        return getAllVotes(getCurrentMonth(), config);
    }

    public static Map<String, Integer> getAllVotes(String date,  ConfigManager config) throws Exception {
        String url = config.getUrlNotification();
        if (date != null) {
            url += "?nbMonth=" + date;
        }

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + config.getTokenNotification());

        int responseCode = con.getResponseCode();
        if (config.getDebug()) log("GET getAllVotes -> Code: " + responseCode);

        Map<String, Integer> votesMap = new HashMap<>();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) response.append(line);

                log("DEBUG: " + config.getDebug() +" resp: "+response);
                if (config.getDebug()) log("Réponse: " + response);
                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String uuid = obj.getString("uuid");
                    int nbVotes = obj.getInt("nbVotes");
                    votesMap.put(uuid, nbVotes);
                }

                return votesMap;
            }
        } else {
            throw new RuntimeException("Erreur HTTP: " + responseCode);
        }
    }

    // Optionnel : Méthode pour générer automatiquement la date actuelle au format yyyymm
    public static String getCurrentMonth() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return String.valueOf(now.getYear() * 100 + now.getMonthValue());
    }

}