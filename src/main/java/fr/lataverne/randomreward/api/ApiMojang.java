package fr.lataverne.randomreward.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class ApiMojang {

    public static UUID getUUIDFromPseudo(String pseudo) throws Exception {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + pseudo);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            return UUID.fromString(extractUUID(connection));
        } else if (connection.getResponseCode() == 204) {
            throw new Exception("Aucun joueur trouvé avec le pseudo : " + pseudo);
        } else {
            throw new Exception("Erreur HTTP : " + connection.getResponseCode());
        }
    }

    private static @NotNull String extractUUID(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(responseBuilder.toString());
        String rawUUID = json.getString("id");

        return rawUUID.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        );
    }
}
