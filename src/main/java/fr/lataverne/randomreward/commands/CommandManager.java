package fr.lataverne.randomreward.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            case "get":
                sender.sendMessage(
                        Component.text("Mauvaise utilisation ! /rr get [id] ou /rr getall").color(NamedTextColor.RED)
                );
                break;
        }
        //error logger consol command not found
        return false;
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
                .color(NamedTextColor.AQUA).append(Component.text(": donne une récompense dans le SAC du joueur")
                .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("give [nom_player] [nombre] ")
                .color(NamedTextColor.AQUA).append(Component.text(": donne X récompenses dans l'INVENTAIRE du joueur")
                .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("list                       ")
                .color(NamedTextColor.AQUA).append(Component.text(": Affiche les récompenses et les % associés ")
                .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("get [id]                   ")
                .color(NamedTextColor.AQUA).append(Component.text(": récupère la récompense à la Ième place dans le bag")
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
                .append(Component.text(": Détail du contenu du sac")
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
                .append(Component.text(": Information pour les admins")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("space     ")
                .color(NamedTextColor.AQUA)
                .append(Component.text(": Do you love kitty?")
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
        if(args[0] == "get"){
            sender.sendMessage(
                    Component.text("get item n°"+args[1]).color(NamedTextColor.AQUA)
            );
        }

        //error logger consol command not found
        return false;
    }

    public boolean onCommandSize3(CommandSender sender, Command command, String s, String[] args) {
        //error logger consol command not found
        return false;
    }

    /*
    //Helps

    //Player commands ?
    //Bag (list)
    //Get reward
    //Get All

    //AdminCommand
    //Give reward
    //Reload
    //SeeBag (opt)

    //Api   <-appel de cmi
    //Send Notification Vote  :  /rr api topvote [pseudo] [score]
    //Test BDD



     Console (event)
     //
     //SaveBag  -> quand le joueur se déconnecte
     //LoadBag -> joueur se connecte

    */
}
