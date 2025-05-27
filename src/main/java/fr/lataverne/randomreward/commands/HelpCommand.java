package fr.lataverne.randomreward.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;

public class HelpCommand {

    public static boolean help(CommandSender sender) {
        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(text("/rr [commande] [argument1] [argument2]")
                .color(NamedTextColor.AQUA));

        sender.sendMessage(text("bag")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche le contenu du sac (bag) du joueur")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("bag [page]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche la page n° du sac")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("getAll")
                .color(NamedTextColor.AQUA)
                .append(text(" : Vide le sac dans l'inventaire")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("get [id]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Récupère la récompense à la Ième position du sac")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("list")
                .color(NamedTextColor.AQUA)
                .append(text(" : Liste les récompenses disponibles")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("list [page]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche la page n° de la liste des récompenses")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("give [pseudo]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Donne une récompense aléatoire dans le sac du joueur")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("give [pseudo] [nombre]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Donne X récompenses dans l'inventaire du joueur")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("vote")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche vos votes du mois en cours")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("vote [aaaa-mm]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche vos votes pour un mois donné")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("vote [pseudo]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche les votes d'un joueur pour ce mois")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("vote [aaaa-mm] [pseudo]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche les votes d'un joueur pour un mois donné")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("topvotes")
                .color(NamedTextColor.AQUA)
                .append(text(" : Classement général des votes du mois courant")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("topvotes [aaaa-mm]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Classement général des votes pour un mois donné")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("topvotes [page]")
                .color(NamedTextColor.AQUA)
                .append(text(" : Page suivante du classement")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("space")
                .color(NamedTextColor.AQUA)
                .append(text(" : Vérifie l’espace libre dans votre inventaire")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("reload")
                .color(NamedTextColor.AQUA)
                .append(text(" : Recharge la configuration du plugin")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("reloadConfirm")
                .color(NamedTextColor.AQUA)
                .append(text(" : Confirme le rechargement du plugin")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("transfert")
                .color(NamedTextColor.AQUA)
                .append(text(" : Transfert les données JSON vers la DB")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("adminhelp")
                .color(NamedTextColor.AQUA)
                .append(text(" : Affiche les commandes d'administration")
                        .color(NamedTextColor.WHITE)));

        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        return true;
    }

    public static boolean adminHelp(CommandSender sender) {
        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(text("/rr [commande] [argument1] [argument2]  ")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(text("give [nom_player]          ")
                .color(NamedTextColor.AQUA).append(text(": Donne une récompense dans le SAC du joueur")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("give [nom_player] [nombre] ")
                .color(NamedTextColor.AQUA).append(text(": Donne X récompenses dans l'INVENTAIRE du joueur")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("list                       ")
                .color(NamedTextColor.AQUA).append(text(": Affiche les récompenses et les % associés ")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("get [id]                   ")
                .color(NamedTextColor.AQUA).append(text(": Récupère la récompense à la Ième place dans le bag")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        return true;
    }
}
