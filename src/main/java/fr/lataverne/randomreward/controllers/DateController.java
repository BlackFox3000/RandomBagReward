package fr.lataverne.randomreward.controllers;

import org.bukkit.command.CommandSender;

public class DateController {
    public static boolean isNotValideDate(CommandSender sender, String date) {
        if(dateFormatIsValid(date)){
            return true;
        }else{
            sender.sendMessage("[RR] Le format de date est invalide ! YYYYMM uniquement.");
            return false;
        }
    }

    public static boolean dateFormatIsValid(String date) {
        // Vérifie si la chaîne est exactement 6 chiffres
        return date != null && date.matches("\\d{6}");
    }
}
