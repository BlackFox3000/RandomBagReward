package fr.lataverne.randomreward.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordApiController {

    static String urlChannelDiscord =
            "";

    public static boolean sendMessages(CommandSender sender, String[] args) throws Exception {
        if(args.length != 4) {
            sender.sendMessage(ChatColor.AQUA +"Mauvaise utilisation ! /rr api sendDiscord [pseudo] [website]");
            return false;
        }

        String playerName = args[2], webSiteUrl = args[3], uuid;

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
        }
        else {
            uuid = player.getUniqueId().toString();
        }

        DiscordApiController.sendDiscordPostRequest(DiscordApiController.urlChannelDiscord,uuid,playerName,webSiteUrl);
        return true;
    }

    public static void main(String[] args) throws Exception {
        DiscordApiController.sendDiscordPostRequest(
                urlChannelDiscord,
                "uuid",
                "pseudo",
                "addr"
        );
    }


    public static void sendDiscordPostRequest(String url, String uuid, String pseudo, String webSite) throws Exception {
        String jsonBody = "{\"content\":\""+uuid+","+pseudo+","+webSite+"\"}";


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
    }

}
