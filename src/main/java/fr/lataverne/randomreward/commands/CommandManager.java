package fr.lataverne.randomreward.commands;

import com.sun.jdi.event.ExceptionEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(
                            Component.text("Mauvaise utilisation ! /rr help").color(NamedTextColor.RED)
                    );
                    break;
                case 1:
                    onCommandSize1(sender, command, s, args);
                    break;
                case 2:
                    onCommandSize2(sender, command, s, args);
                    break;
                case 3:
                    onCommandSize3(sender, command, s, args);
                    break;
                default:
                    return false;
            }

        }
        //error logger consol sender not found
        return false;
    }

    private boolean onCommandSize1(CommandSender sender, Command command, String s, String[] args) {
        switch (args[0].toLowerCase()) {
            case "help":
                return help(sender);
            case "bag":
                return bag(sender, command, s, args);
            case "getall":
                return bagGetAll(sender, command, s, args);
            case "adminhelp":
                return adminHelp(sender);
            case "space":
                return space(sender);
            case "reload":
                return reload(sender, false);
            case "reloadConfirm":
                return reload(sender, true);
            case "list":
            case "listItem":
            case "stat":
            case "statistique":
                return listItems(sender);
            case "get":
                sender.sendMessage(
                        Component.text("Mauvaise utilisation ! /rr get [id] ou /rr getall").color(NamedTextColor.RED)
                );
                break;
            case "give":
                sender.sendMessage(
                        Component.text("Mauvaise utilisation ! /rr give [pseudo] [nombre]").color(NamedTextColor.RED)
                );
                break;
        }
        //error logger consol command not found
        return false;
    }

    private boolean listItems(CommandSender sender) {
        sender.sendMessage(
                Component.text("listItem").color(NamedTextColor.AQUA)
        );
        return true;
    }

    private boolean reload(CommandSender sender, boolean confirm) {
        if (confirm) {
            sender.sendMessage(
                    Component.text("reload confirm").color(NamedTextColor.AQUA)
            );
        } else {
            sender.sendMessage(
                    Component.text("reload ? Write /reloadConfirm for confirm reload ?").color(NamedTextColor.AQUA)
            );
        }
        return true;
    }

    private boolean bagGetAll(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(
                Component.text("bagGetAll").color(NamedTextColor.AQUA)
        );
        return true;
    }

    private boolean bag(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(
                Component.text("bag").color(NamedTextColor.AQUA)
        );
        return true;
    }

    private boolean adminHelp(CommandSender sender) {
        sender.sendMessage(Component.text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.text("/rr [commande] [argument1] [argument2]  ")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.text("give [nom_player]          ")
                .color(NamedTextColor.AQUA).append(Component.text(": Donne une récompense dans le SAC du joueur")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("give [nom_player] [nombre] ")
                .color(NamedTextColor.AQUA).append(Component.text(": Donne X récompenses dans l'INVENTAIRE du joueur")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("list                       ")
                .color(NamedTextColor.AQUA).append(Component.text(": Affiche les récompenses et les % associés ")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("get [id]                   ")
                .color(NamedTextColor.AQUA).append(Component.text(": Récupère la récompense à la Ième place dans le bag")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        return true;
    }

    public boolean help(CommandSender sender) {
        sender.sendMessage(Component.text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.text("/rr [commande] [argument1] [argument2]  ")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(Component.text("bag    ")
                .color(NamedTextColor.AQUA)
                .append(Component.text("    : Détail du contenu du sac")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("getAll     ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(": Vide son sac dans l'inventaire")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("get [id]   ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(": Récupère la récompense à la Ième place dans le bag")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("help       ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(": Informations sur les commandes disponibles")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("adminhelp ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(" : Information pour les admins")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("space     ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(" : Do you love kitty?")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        return true;
    }

    private boolean space(CommandSender sender) {
        sender.sendMessage(
                Component.text("space").color(NamedTextColor.AQUA)
        );
        return true;
    }

    public boolean onCommandSize2(CommandSender sender, Command command, String s, String[] args) {
        switch (args[0].toLowerCase()) {
            case "get":
                return getItem(sender, command, s, args);
            case "seebag":
                //send=> player to found
                return bag(sender, command, s, args);
            default:
                return false;
        }
    }

    public boolean onCommandSize3(CommandSender sender, Command command, String s, String[] args) {
        switch (args[0].toLowerCase()) {
            case "give":
                try {
                    //send player
                    return giveItem(sender, command, s, args, args[1], Integer.parseInt(args[2]));
                }catch (Exception e){
                    System.out.println("error : "+e);
                }
            case "get":
                return getItem(sender, command, s, args);
            case "topvote":
                return sendNotificationVote(sender, command, s, args);
            default:
                return false;
        }
    }

    private boolean giveItem(CommandSender sender, Command command, String s, String[] args, String name, int nbItem) {
        System.out.println("name : " + name);
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            for (int i = 0; i < nbItem; i++) {
                player.sendMessage(
                        Component.text("give item random at " + args[1] + " : " + nbItem).color(NamedTextColor.AQUA)
                );
            }
        }else{
            System.out.println("player not found -> save bdd");
        }
        return false;
    }

    private boolean sendNotificationVote(CommandSender sender, Command command, String s, String[] args) {
        if (args[0] == "senNotiVote") {
            sender.sendMessage(
                    Component.text("send notification vote").color(NamedTextColor.AQUA)
            );
        }
        return true;
    }

    public boolean getItem(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(
                Component.text("get item n°" + args[1]).color(NamedTextColor.AQUA)
        );
        return true;
    }

    /*
    //Reload
    //SeeBag (opt)

    //Api ←appel de cmi
    //Send Notification Vote :  /rr topvote [pseudo] [score]
    //Test BDD



     Console (event)
     //
     //SaveBag  -> quand le joueur se déconnecte
     //LoadBag -> joueur se connecte

    */
}
