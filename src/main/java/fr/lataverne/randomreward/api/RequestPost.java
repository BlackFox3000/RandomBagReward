package fr.lataverne.randomreward.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.api.model.VotreObjetDonnees;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestPost{
    static boolean DEBUG = true;

    public static void envoyerRequetePOST(String url, Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(data);
        if(DEBUG) {
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

        if(DEBUG) {
            // Affichage de la réponse
            System.out.println(response.toString());
        }
    }

    public static void sendPost(String pseudo, int score) {
        FileConfiguration fileConfiguration = RandomReward.getInstance().getConfig();
        String url = fileConfiguration.getString("passPhraseVote");
        //String url = "http://tintorri.com/tamagochi/api.php"; // Modifier avec votre URL cible
        // Création de l'objet avec les données à envoyer
        VotreObjetDonnees data = new VotreObjetDonnees();
        data.setPseudo(pseudo);
        data.setScore(score);

        try {
            envoyerRequetePOST(url, data);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}