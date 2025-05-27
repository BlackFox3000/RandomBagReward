package fr.lataverne.randomreward.controllers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.json.JSONObject;

public class PlayerController {

    public static Player getPlayertoPseudo(CommandSender sender, String pseudo) throws IOException {

        if(pseudo==null){
            sender.sendMessage("Pseudo null invalide.");
            return null;
        }
        // Vérifie si c'est un pseudo valide (alphanum + underscore, 3 à 16 caractères)
        if (!pseudo.matches("^[a-zA-Z0-9_]{3,16}$")) {
            sender.sendMessage("Pseudo impossible ! ERROR_PLAYER_NAME_INVALIDE");
            return null;
        }

        OfflinePlayer offline = Bukkit.getOfflinePlayer(pseudo);

        /*if (!offline.hasPlayedBefore()) {
            sender.sendMessage("Ce joueur n'a jamais joué sur ce serveur.ERROR_PLAYER_NOT_FOUND_SERVER");
            return null;
        }*/

        return offline.getPlayer();
    }

    public static String getPseudoFromUUID(UUID uuid) throws Exception {
        // Supprimer les tirets de l'UUID pour l'API Mojang
        String cleanUUID = uuid.toString().replace("-", "");

        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + cleanUUID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Vérifier si la requête a réussi (HTTP 200)
        if (connection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }
            in.close();

            // Parse JSON pour obtenir le nom
            JSONObject json = new JSONObject(responseBuilder.toString());
            return json.getString("name");

        } else {
            throw new Exception("Impossible de récupérer le pseudo pour UUID : " + uuid);
        }
    }

    public static UUID fetchUUIDFromMojang(String playerName) throws IOException, MalformedURLException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 204) {
            return null; // Le joueur n'existe pas
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        // Extraire l’UUID
        JSONObject json = new JSONObject(response.toString());
        String id = json.getString("id");
        return UUID.fromString(id.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        ));
    }


}
