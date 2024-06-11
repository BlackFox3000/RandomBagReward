package fr.lataverne.randomreward;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;

public class SendRequestTopVote {

    private Config config = (Config) Config.getInstance();

    public void send(CommandSender sender, String playerName, String webSite) {
        String uuid;
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
             uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
        }
        else {
             uuid = player.getUniqueId().toString();
        }
        String urlSiteNotification = config.getConfigValue("urlSiteNotification");
        String secretPassword = config.getConfigValue("passPhraseNotification");
        webSite = webSite.replace(".","");
        webSite = webSite.replace("-","");

        try {
            // Construire l'URL avec les variables
            String url = urlSiteNotification +
                    URLEncoder.encode(uuid, "UTF-8") + "/" +
                    URLEncoder.encode(playerName, "UTF-8") + "/" +
                    URLEncoder.encode(webSite, "UTF-8") + "/" +
                    URLEncoder.encode(secretPassword, "UTF-8");

            if(config.isDebug()) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "Send :" + url);
            }
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Spécifier la méthode de requête
            con.setRequestMethod("GET");

            // Obtenir la réponse
            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Afficher la réponse
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
