package fr.lataverne.randomreward.commands;

import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.controllers.BagController;
import fr.lataverne.randomreward.controllers.DateController;
import fr.lataverne.randomreward.controllers.NotificationController;
import fr.lataverne.randomreward.controllers.PlayerController;
import fr.lataverne.randomreward.models.Reward;
import fr.lataverne.randomreward.models.RewardDB;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fr.lataverne.randomreward.EnvironmentDetector.log;
import static fr.lataverne.randomreward.controllers.ConvertJsonFileToDB.mainTransefertJsonToDB;
import static fr.lataverne.randomreward.controllers.PlayerController.getPseudoFromUUID;
import static net.kyori.adventure.text.Component.text;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(
                            text("Mauvaise utilisation ! /rr help").color(NamedTextColor.RED)
                    );
                    break;
                case 1:
                    try {
                        onCommandSize1(sender, command, s, args);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        onCommandSize2(sender, command, s, args);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    try {
                        onCommandSize3(sender, command, s, args);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    return false;
            }

        }
        //error logger consol sender not found
        return false;
    }

    private boolean onCommandSize1(CommandSender sender, Command command, String s, String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "transfert":
                return mainTransefertJsonToDB();
            case "help":
                return help(sender);
            case "bag":
                return bag(sender);
            case "getall":
                return bagGetAll(sender);
            case "adminhelp":
                return adminHelp(sender);
            case "space":
                return space(sender);
            case "reload":
                return reload(sender, false);
            case "reloadconfirm":
                return reload(sender, true);
            case "list":
            case "listitem":
            case "stat":
            case "statistique":
                return listItems(sender);
            case "get":
                sender.sendMessage(
                        text("Mauvaise utilisation ! /rr get [id] ou /rr getall").color(NamedTextColor.RED)
                );
                break;
            case "give":
                sender.sendMessage(
                        text("Mauvaise utilisation ! /rr give [pseudo] [nombre]").color(NamedTextColor.RED)
                );
                break;
            case "getvote":
                return getVote(sender,null,null);
            case "gettopvotes":
            case "getvotes":
                return getVotes(sender, null,"0");
        }
        //error logger consol command not found
        return false;
    }

    private boolean getVote(CommandSender sender, String date, String cibleName) throws Exception {

        if(date!=null) {
            if (DateController.isNotValideDate(sender, date)) {
                return false;
            }
        }

        Player ciblePlayer = null;
        if(cibleName!=null){
            ciblePlayer = PlayerController.getPlayertoPseudo(sender, cibleName);
        }

        //si le sender = joueur
        if(sender instanceof Player player) {
            //si pas de nom
            if(cibleName==null) {
                if (date == null) {
                    // s'il n'y a pas de date ni de nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(player,player);
                }else{
                    //s'il y a une date mais pas de nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(player,player,date);
                }
            }else {
                if (date == null) {
                    // s'il n'y a pas de date mais un nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(player, ciblePlayer);
                }else{
                    // s'il y a une date et un nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(player, ciblePlayer, date);
                }
            }
        }
        //si le sender = console
        else{
            //si pas de nom
            if(cibleName==null) {
                sender.sendMessage("La console ne dispose pas de point de vote.");
            }else {
                if (date == null) {
                    // s'il n'y a pas de date mais un nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(sender, ciblePlayer);
                }else{
                    // s'il y a une date et un nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(sender, ciblePlayer, date);
                }
            }
        }
        return true;
    }

    private boolean getVotes(CommandSender sender, String date, String index) throws Exception {
        // Récupération des votes
        Map<String, Integer> votes;
        if (date == null) {
            votes = ApiRequestManager.getCurrentMonthVoteForAll();
        } else {
            if (!dateFormatIsValide(sender, date, true)) {
                return false;
            }
            votes = ApiRequestManager.getSelectedMonthVoteForAll(date);
        }

        // Pagination
        int indexInt = 0;
        if (index != null) {
            try {
                indexInt = Integer.parseInt(index);
                if (indexInt != 0) {
                    indexInt--;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("[RR] Index invalide. Utilisez un nombre positif (ex: 1, 2, 3...)").color(NamedTextColor.RED));
                return false;
            }
        }

        List<Map.Entry<String, Integer>> voteList = new ArrayList<>(votes.entrySet());

        sender.sendMessage(
                text("================== RandomReward ==================").color(NamedTextColor.AQUA)
        );
        sender.sendMessage(
                Component.text("=----------------- " +
                                       "================= ", NamedTextColor.AQUA)
                        .append(Component.text(getCurrentDateMmmmYyyy(), NamedTextColor.WHITE))
                        .append(Component.text(" ==================", NamedTextColor.AQUA))
        );

        int start = indexInt * 5;
        int end = Math.min(start + 5, voteList.size());

        for (int i = start; i < end; i++) {
            Map.Entry<String, Integer> entry = voteList.get(i);
            String formattedIndex = String.format("%03d", i + 1);
            String pseudo = getPseudoFromUUID(UUID.fromString(entry.getKey()));
            sender.sendMessage(formattedIndex + ") " + pseudo + " " + entry.getValue());
        }

        int totalPages = (int) Math.ceil(voteList.size() / 5.0);
        sendPaginationFooter(sender, indexInt + 1, totalPages); // On renvoie en base 1 ici
        return true;
    }

    private String getCurrentDateMmmmYyyy() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);
        return today.format(formatter);
    }

    public static void sendPaginationFooter(CommandSender sender, int index, int max) {
        TextComponent.Builder footer = Component.text();

        if (max == 0) max = 1; // Pour éviter un max à 0

        // Page précédente
        if (index > 1) {
            String space = index < 10 ? "  " : index < 100 ? " " : "";
            footer.append(Component.text("<< Page " + index + space, NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr getVotes " + (index - 1))));
        } else {
            footer.append(Component.text("=======", NamedTextColor.AQUA));
        }

        // Centre
        String indexCenter = index < 10 ? ".." + index + ".." : index < 100 ? "." + index + "." : String.valueOf(index);
        String maxCenter = max < 10 ? ".." + max + ".." : max < 100 ? "." + max + "." : String.valueOf(max);
        footer.append(Component.text("=========== ", NamedTextColor.AQUA));
        footer.append(Component.text("(" + indexCenter + "/" + maxCenter + ")", NamedTextColor.WHITE));
        footer.append(Component.text(" ===========", NamedTextColor.AQUA));

        // Page suivante (corrigé)
        if (index < max) {
            String space = (index + 1) < 10 ? "  " : (index + 1) < 100 ? " " : "";
            footer.append(Component.text("Page " + (index + 1) + space + " >>", NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr getVotes " + (index + 1))));
        } else {
            footer.append(Component.text("===========", NamedTextColor.AQUA));
        }

        sender.sendMessage(footer.build());
    }


    private boolean dateFormatIsValide(CommandSender sender, String date, boolean debug) {
        if (date.matches("^\\d{6}$")) {
            String monthStr = date.substring(4, 6);
            int month = Integer.parseInt(monthStr);
            if (month >= 1 && month <= 12) {
                return true;
            }
        }
        if(debug) {
            sender.sendMessage(Component.text("[RR] Format de date invalide. Utilisez 'yyyymm' (ex: 202504)", NamedTextColor.RED));
        }
        return false;
    }

    private boolean listItems(CommandSender sender){
        return listItems(sender, 0);
    }

    private boolean listItems(CommandSender sender, int indexPage) {
        List<Reward> listItems = RandomReward.getInstance().getRewardList();
        sender.sendMessage(
                text("================== RandomReward ==================").color(NamedTextColor.AQUA));
        sender.sendMessage(
                text("=== [N°] Nom quantité chance ===").color(NamedTextColor.AQUA));
        for(int i=indexPage; i<indexPage+10 && i<listItems.size() ; i++){
            Reward reward = listItems.get(i);

            String spaceIndex ="";
            if(i<99)
                spaceIndex =" ";
            if(i<9)
                spaceIndex = "  ";

            Component message;
            if(sender.hasPermission("rr.give")) {
                message = text()
                        .append(text("[" + spaceIndex + i + "]-- ", NamedTextColor.AQUA))
                        .append(text(reward.getString(), NamedTextColor.WHITE))
                        .clickEvent(ClickEvent.runCommand("/rr give " + reward.getName() + " " + sender.getName()))
                        .hoverEvent(HoverEvent.showText(text("Clique pour obtenir !", NamedTextColor.AQUA)))
                        .build();
            }else{
                message = text()
                        .append(text("[" + spaceIndex + i + "]-- ", NamedTextColor.AQUA))
                        .append(text(reward.getName(), NamedTextColor.WHITE))
                        .build();
            }
            sender.sendMessage(message);
        }
        sender.sendMessage(
                text("================== RandomReward ==================").color(NamedTextColor.AQUA));

        return true;
    }

    private boolean reload(CommandSender sender, boolean confirm) {
        if (confirm) {
            RandomReward.getInstance().reload();
            sender.sendMessage("§aRandomReward rechargé avec succès !");
        } else {
            sender.sendMessage(
                    text("reload ? Write /reloadConfirm for confirm reload ?").color(NamedTextColor.AQUA)
            );
        }
        return true;
    }

    private boolean bagGetAll(CommandSender sender) throws Exception {
        Player player= (Player) sender;
        List<RewardDB> bag = ApiRequestManager.getBag(String.valueOf(player.getUniqueId()));
        boolean invSpaceValide = invSpace(player.getInventory(),bag.getFirst().getMaterial())>0;
        for(int i=0; i<bag.size() && invSpaceValide; i++) {
            giveItem(player, bag.get(i), 1);
            if(bag.size()>i+1){
                invSpaceValide = invSpace(player.getInventory(),bag.get(i+1).getMaterial())>0;
            } else invSpaceValide = false;
        }
        return true;
    }

    private boolean bag(CommandSender sender) throws Exception {
        if(sender instanceof Player player) {
            return BagController.getBag(sender, 1, player.getUniqueId().toString());
        }
        log("La console ne possède pas de bag. Essayer /bag [pseudo]");
        return false;
    }

    private boolean adminHelp(CommandSender sender) {
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

    public boolean help(CommandSender sender) {
        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(text("/rr [commande] [argument1] [argument2]  ")
                .color(NamedTextColor.AQUA));
        sender.sendMessage(text("bag    ")
                .color(NamedTextColor.AQUA)
                .append(text("    : Détail du contenu du sac")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("getAll     ")
                .color(NamedTextColor.AQUA)
                .append(text(": Vide son sac dans l'inventaire")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("get [id]   ")
                .color(NamedTextColor.AQUA)
                .append(text(": Récupère la récompense à la Ième place dans le bag")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("help       ")
                .color(NamedTextColor.AQUA)
                .append(text(": Informations sur les commandes disponibles")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("adminhelp ")
                .color(NamedTextColor.AQUA)
                .append(text(" : Information pour les admins")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("space     ")
                .color(NamedTextColor.AQUA)
                .append(text(" : Do you love kitty?")
                        .color(NamedTextColor.WHITE)));
        sender.sendMessage(text("==============RandomReward==============")
                .color(NamedTextColor.AQUA));
        return true;
    }

    private boolean space(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }
        sender.sendMessage(
                text("space inventaire : "+ invSpace(player.getInventory()))
        );
        return true;
    }

    public boolean onCommandSize2(CommandSender sender, Command command, String s, String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "bag":
                return bag2Arg(sender, args[1]);
            case "reload":
                if(args[1].equalsIgnoreCase("confirm"))
                    return reload(sender, true);
            case "get":
                return getItem(sender, args);
            case "list":
                return listItems(sender, Integer.parseInt(args[1]));
            case "sendnotificationvote":
                return sendNotificationVote(sender, args);
            case "getvote":
                return getVote(sender,null, args[1]);
            case "gettopvotes":
            case "getvotes":
                if(dateFormatIsValide(sender,args[1], false)) {
                    return getVotes(sender, args[1], "0");
                }else {
                    return getVotes(sender, null,args[1]);
                }
            default:
                return false;
        }
    }

    private boolean bag2Arg(CommandSender sender, String indexString) throws Exception {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }

        int index;

        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            player.sendMessage("L'index doit être un nombre !");
            return false;
        }

        return BagController.getBag(sender, index, player.getUniqueId().toString());
    }

    public boolean onCommandSize3(CommandSender sender, Command command, String s, String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "bag":
                return bag3arg(sender, args);
            case "give":
                try {
                    return getItem(sender, args);
                }catch (Exception e){
                    System.out.println("error : "+e);
                }
            case "get":
                return getItem(sender, args);
            case "topvotes":
                return getVotes(sender,args[1],args[2]);
            default:
                return false;
        }
    }

    private boolean bag3arg(CommandSender sender, String[] args) throws Exception {
        return BagController.bagOther(sender,args);
    }

    private boolean getItem(CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }

        int indexItem = 0;

        try {
            if(args.length>1) {
                indexItem = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("L'index doit être un nombre !");
            return false;
        }
        RewardDB reward = ApiRequestManager.getReward(indexItem);

        return giveOneItem(player, reward);
    }

    /**
     * Methode pas très utile.. nbItem toujours = 1. Intéressante si utilisée pour donner X fois
     * la même récompense (intéressant pour une utilisation via siteWeb)
     * @param player Player
     * @param reward RewardDB
     * @param nbItem nbItem
     * @return boolean
     */
    private boolean giveItem(Player player, RewardDB reward, int nbItem) {
        if (player != null && nbItem!=0 && reward!=null) {
            for (int i = 0; i < nbItem; i++) {
                giveOneItem(player, reward);
            }
            player.sendMessage(
                    text("give item "+nbItem+" "+reward.getName()).color(NamedTextColor.AQUA)
            );
        }else{
            log("player or reward not found.");
        }
        return false;
    }

    public boolean giveOneItem(Player player, RewardDB reward) {
        String itemName = reward.getName();
        log("ir give " + player.getName() + " " + reward.getName());

        String command = switch (reward.getPlugin()) {
            case "minecraft" -> "give " + player.getName() + " " + itemName + " " + reward.count;
            case "itemreward" -> getStringCommandIR(player,reward);
            case "itemsadder" -> "iagive " + player.getName() + " " + itemName + " " + reward.count;
            case "ecoitems" -> "ecoitems give " + player.getName() + " " + itemName + " " + reward.count;
            default -> "say [error] RandomBagReward - plugin inconnu.";
        };
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
        return true;
    }

    private String getStringCommandIR(Player player, RewardDB reward) {
        String itemName = reward.getName();
        int itemNumber = reward.getCount();
        String commande = "ir give " + player.getName() ;
        String suiteCommande = switch(itemName){
            case "FlyPotion_1" ->  " FlyPotion " + itemNumber + " 1";
            case "FlyPotion_2" ->  " FlyPotion " + itemNumber + " 2";
            case "FlyPotion_3" ->  " FlyPotion " + itemNumber + " 3";
            case "FlyPotion_4" ->  " FlyPotion " + itemNumber + " 4";
            default -> " "+ itemName +" "+ itemNumber;
        };
        return commande + suiteCommande;
    }

    private boolean sendNotificationVote(CommandSender sender, String[] args) throws Exception {
        if(sender.hasPermission("rr.vnotif")) {
            ApiRequestManager.sendNotificationVote(sender, args[2]);
            sender.sendMessage(
                    text("send notification vote").color(NamedTextColor.AQUA)
            );
        }
        return true;
    }

    public int invSpace(PlayerInventory inv, Material m) {
        int count = 0;
        int maxStack = m.getMaxStackSize();

        for (int slot = 0; slot < 36; slot++) { // 0 à 35 uniquement
            ItemStack is = inv.getItem(slot);

            if (is == null || is.getType() == Material.AIR) {
                count += maxStack;
            } else if (is.getType() == m) {
                count += (maxStack - is.getAmount());
            }
        }
        return count;
    }

    public int invSpace (PlayerInventory inv) {
        int count = 0;
        for (int slot = 0; slot < 36; slot ++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) {
                count++;
            }

        }
        return count;
    }


    /*
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
