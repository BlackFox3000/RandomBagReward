package fr.lataverne.randomreward.controllers;

import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.models.RewardDB;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

public class BagController {


    public static boolean getBag(CommandSender sender, int indexPage, String uuidOwner) throws Exception {
        if(indexPage!=0) {
            indexPage--;
        }
        if(indexPage<0){
            if(Math.random()>0.5) {
                sender.sendMessage(
                        text("[RandomReward] Mais oui ! Mais oui ! et toi tu es née a " + (indexPage + 1) + " an !").color(NamedTextColor.RED));
            }else {
                sender.sendMessage(
                        text("[RandomReward] Avé César! .. et non remonté dans le passé ça n'existe pas. Donc ton sac en page " + (indexPage + 1) + " ..non plu").color(NamedTextColor.RED));
            }
            return true;
        }
        List<RewardDB> listItems = ApiRequestManager.getBag(uuidOwner);
        if(listItems.isEmpty()){
            sender.sendMessage(
                    text("[RandomReward] Sac vide ou inexistant. Penses à voter !").color(NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(
                text("================== RandomReward ==================").color(NamedTextColor.AQUA));
        int startIndex = indexPage * 7;
        int endIndex = Math.min(startIndex + 7, listItems.size());

        for (int indexItem = startIndex; indexItem < endIndex; indexItem++) {
            //===============Espace entre crochet index "[___]================
            RewardDB reward = listItems.get(indexItem);
            String spaceQuantity ="";
            if(reward.getCount()<100)
                spaceQuantity ="0";
            if(reward.getCount()<10)
                spaceQuantity = "00";
            //===============================

            String spaceIndex ="";
            if(indexItem<99)
                spaceIndex ="0";
            if(indexItem<9)
                spaceIndex = "00";

            Component message;
            if((sender instanceof Player player) && Objects.equals(player.getUniqueId().toString(), uuidOwner)) {
                message = text()
                        .append(text("[", NamedTextColor.AQUA))
                        .append(text(spaceIndex + indexItem, NamedTextColor.WHITE))
                        .append(text("] -- ", NamedTextColor.AQUA))
                        .append(text(reward.getString(), NamedTextColor.WHITE))
                        .append(text(" -- ", NamedTextColor.AQUA))
                        .append(text(spaceQuantity+reward.getCount(), NamedTextColor.WHITE))
                        .clickEvent(ClickEvent.runCommand("/rr get " + reward.getId()))
                        .hoverEvent(HoverEvent.showText(text("Clique pour obtenir !", NamedTextColor.AQUA)))
                        .build();
            }else{
                message = text()
                        .append(text("[" + spaceIndex + indexItem + "] -- ", NamedTextColor.AQUA))
                        .append(text(reward.getString(), NamedTextColor.WHITE))
                        .append(text(" -- ", NamedTextColor.AQUA))
                        .append(text(spaceQuantity+reward.getCount(), NamedTextColor.WHITE))
                        .hoverEvent(HoverEvent.showText(text("On ne regarde qu'avec les yeux !", NamedTextColor.AQUA)))
                        .build();
            }
            sender.sendMessage(message);
        }

        int indexPageFooter = indexPage+1;
        int totalPages = (int) Math.ceil(listItems.size() / 7.0);
        sendPaginationFooter(sender, indexPageFooter, totalPages);

        return true;
    }

    public static boolean bagOther(CommandSender sender, String[] args) throws Exception {
        if (false /* permission missing */) {
            sender.sendMessage("Seuls les joueurs ayant la permission peuvent utiliser cette commande.");
            return false;
        }

        int index;
        try {
            index = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("L'index doit être un nombre !");
            return false;
        }

        String possiblePseudo = args[2];

        // Vérifie si le pseudo est valide (alphanumérique + underscore, 3 à 16 caractères)
        if (!possiblePseudo.matches("^[a-zA-Z0-9_]{3,16}$")) {
            sender.sendMessage("Pseudo invalide !");
            return false;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(possiblePseudo);

        if (offlinePlayer == null || (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline())) {
            sender.sendMessage(Component.text("Ce joueur est introuvable ou ne s'est jamais connecté.")
                    .color(NamedTextColor.RED));
            return false;
        }

        // Appel à getBag avec UUID
        return getBag(sender, index, offlinePlayer.getUniqueId().toString());
    }


    public static void sendPaginationFooter(CommandSender sender, int index, int max) {
        TextComponent.Builder footer = Component.text();

        if(max==0){
            max++;
        }
        // Page précédente
        if (index > 1) {
            String space = index < 10 ? "  " : index < 100 ? " " : "";
            footer.append(Component.text("<< Page " + index + space, NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr bag " + (index - 1))));
        } else {
            footer.append(Component.text("=======", NamedTextColor.AQUA));
        }

        // Centre (page actuelle)
        //String 18+13(6+1+6) + 18;
        String indexString = String.valueOf(index);
        String indexCenter = index < 10 ? ".."+indexString+".." : index < 100 ? "."+indexString+"." :
                indexString;
        String maxString = String.valueOf(max);
        String maxCenter = index < 10 ? ".."+maxString+".." : index < 100 ? "."+maxString+"." :
                maxString;
        footer.append(Component.text("=========== ", NamedTextColor.AQUA));
        footer.append(Component.text("(" + indexCenter + "/" + maxCenter + ")", NamedTextColor.WHITE));
        footer.append(Component.text(" ===========", NamedTextColor.AQUA));

        // Page suivante
        if (index * 7 < max) {
            String space = index < 10 ? "  " : index < 100 ? " " : "";
            footer //11
                    .append(Component.text("Page " + (index + 1) + space + " >>", NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr bag " + (index+1))));
        } else {
            footer.append(Component.text("===========", NamedTextColor.AQUA));
        }

        sender.sendMessage(footer.build());
    }
}
