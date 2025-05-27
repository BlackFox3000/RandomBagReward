package fr.lataverne.randomreward.controllers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateController {

    public static boolean dateFormatIsValid(CommandSender sender, String date) {
        if (date.matches("^\\d{6}$")) {
            String monthStr = date.substring(4, 6);
            int month = Integer.parseInt(monthStr);
            if (month >= 1 && month <= 12) {
                return true;
            }
        }
        if(sender!= null) {
            sender.sendMessage(Component.text("[RR] Format de date invalide. Utilisez 'yyyymm' (ex: 202504)", NamedTextColor.RED));
        }
        return false;
    }

    public static String getCurrentDateMmmmYyyy() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);
        return today.format(formatter);
    }

    public static String formatYYYYMMToMonthYear(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) return "Format invalide";

        try {
            int year = Integer.parseInt(yyyymm.substring(0, 4));
            int month = Integer.parseInt(yyyymm.substring(4, 6));

            // Nom des mois en français
            String[] moisFrancais = {
                    "janvier", "février", "mars", "avril", "mai", "juin",
                    "juillet", "août", "septembre", "octobre", "novembre", "décembre"
            };

            if (month < 1 || month > 12) return "Mois invalide";

            return moisFrancais[month - 1] + " " + year;
        } catch (NumberFormatException e) {
            return "Format invalide";
        }
    }

}
