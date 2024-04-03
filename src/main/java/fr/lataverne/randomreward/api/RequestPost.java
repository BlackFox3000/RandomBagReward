package fr.lataverne.randomreward.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.Reward;
import fr.lataverne.randomreward.SendRequestTopVote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestPost{

    public static void envoyerRequetePOST(String url, Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(data);
        System.out.println(jsonBody);

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

        // Affichage de la réponse
        System.out.println(response.toString());
    }

    public static void sendPost(String pseudo, int score) {
        String url = "VOTRE-URL-ICI"; // Modifier avec votre URL cible

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

    public static void main(String[] args) {
        sendPost("bob3",25);
    }
}