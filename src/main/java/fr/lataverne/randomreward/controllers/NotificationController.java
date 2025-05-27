package fr.lataverne.randomreward.controllers;

import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.api.NotificationService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static fr.lataverne.randomreward.api.NotificationService.sendNotificationVotePost;
import static fr.lataverne.randomreward.controllers.DateController.formatYYYYMMToMonthYear;

public class NotificationController {


    public static void getVote(CommandSender sender, ConfigManager config) throws Exception {
        Map<String, Integer> list =  NotificationService.getAllVotes(config);
    }

    public void sendNotification(String args, ConfigManager config) {
        try {
            sendNotificationVotePost("MonPseudo", config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sender Le receveur retour du message
     * @param uuid L'uuid du joueur cible à qui on souhaite connaitre ses votes.
     * @param pseudo Le pseudo du joueur cible à qui on souhaite connaitre ses votes.
     * @throws Exception
     */
    public static void getCurrentMonthVoteForOnePlayer(CommandSender sender, String uuid, String pseudo) throws Exception {
        int nbVote = ApiRequestManager.getCurrentMonthVoteForOnePlayer(uuid);
        footerPrintVote(sender,pseudo,nbVote, null);
    }

    /**
     *
     * @param sender Le receveur retour du message
     * @param uuid L'uuid du joueur cible à qui on souhaite connaitre ses votes.
     * @param pseudo Le pseudo du joueur cible à qui on souhaite connaitre ses votes.
     * @param date mois souhaité au format YYYYMM
     * @throws Exception
     */
    public static void getSelectedMonthVoteForOnePlayer(CommandSender sender, String uuid, String pseudo, String date) throws Exception {
        int nbVote = ApiRequestManager.getSelectedMonthVoteForOnePlayer(uuid,date);
        footerPrintVote(sender,pseudo,nbVote, date);
    }

    private static void footerPrintVote(CommandSender sender, String pseudo, int nbVote, String date){
        if(date!=null){
            //reformatage pour une lecture plus agréable
            date = formatYYYYMMToMonthYear(date);
            if(nbVote == -1) {
                if (sender instanceof Player player) {
                    player.sendMessage("[RR] " + pseudo + " ne dispose d'aucun vote à la date du "+ date);
                } else {
                    sender.sendMessage("[RR] " + pseudo +  " ne dispose d'aucun vote à la date du "+ date);
                }
            }
            else{
                if (sender instanceof Player player) {
                    player.sendMessage("[RR] " + pseudo + " dispose de " + nbVote+" à la date du "+ date);
                } else {
                    sender.sendMessage("[RR] " + pseudo + " dispose de " + nbVote+" à la date du "+ date);
                }
            }
        }else {
            if (nbVote == -1) {
                if (sender instanceof Player player) {
                    player.sendMessage("[RR] " + pseudo + " ne dispose d'aucun vote ce mois ci");
                } else {
                    sender.sendMessage("[RR] " + pseudo + " ne dispose d'aucun vote ce mois ci");
                }
            } else {
                if (sender instanceof Player player) {
                    player.sendMessage("[RR] " + pseudo + " dispose de " + nbVote);
                } else {
                    sender.sendMessage("[RR] " + pseudo + " dispose de " + nbVote);
                }
            }
        }
    }

}
