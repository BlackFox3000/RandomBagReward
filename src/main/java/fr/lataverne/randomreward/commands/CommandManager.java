package fr.lataverne.randomreward.commands;

import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.api.ApiMojang;
import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.api.RewardService;
import fr.lataverne.randomreward.controllers.*;
import fr.lataverne.randomreward.gui.BagInterfaceCommand;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
                        return onCommandSize1(sender, command, s, args);
                    } catch (Exception e) {
                        sender.sendMessage(text("[RR cm1] ❌ Une erreur est survenue : " + e.getMessage()).color(NamedTextColor.RED));
                        e.printStackTrace();
                        return true;
                    }
                case 2:
                    try {
                        return onCommandSize2(sender, command, s, args);
                    } catch (Exception e) {
                        sender.sendMessage(text("[RR cm2] ❌ Une erreur est survenue : " + e.getMessage()).color(NamedTextColor.RED));
                        e.printStackTrace();
                        return true;
                    }
                case 3:
                    try {
                        return onCommandSize3(sender, command, s, args);
                    } catch (Exception e) {
                        sender.sendMessage(text("[RR cm3] ❌ Une erreur est survenue : " + e.getMessage()).color(NamedTextColor.RED));
                        e.printStackTrace();
                        return true;
                    }
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
                return HelpCommand.help(sender);
            case "bag":
                return bag(sender);
            case "bigbag":
                return bigbag(sender);
            case "getall":
                return bagGetAll(sender);
            case "adminhelp":
                return HelpCommand.adminHelp(sender);
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
                        text("[RR] Mauvaise utilisation ! /rr get [id] ou /rr getall").color(NamedTextColor.RED)
                );
                break;
            case "give":
                sender.sendMessage(
                        text("[RR] Mauvaise utilisation ! /rr give [pseudo] [nombre]").color(NamedTextColor.RED)
                );
                break;
            case "vote":
                return getVote(sender,null,null);
            case "topvotes":
                return getVotes(sender, null,"0");
            default:
                sender.sendMessage(
                        text("[RR] Mauvaise utilisation ! /rr "+args[0]+" inconnue").color(NamedTextColor.RED)
                );
                break;
        }
        return false;
    }

    public boolean onCommandSize2(CommandSender sender, Command command, String s, String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "bag": //index page
                return bag2Arg(sender, args[1]);
            case "bigbag":
                return bigbag2arg(sender, args[1]);
            case "reload": //confirm
                if(args[1].equalsIgnoreCase("confirm"))
                    return reload(sender, true);
            case "get": //indexItem
                return bagGetOneItem(sender, args);
            case "give": //Player pseudo
                return give(sender, args);
            case "list": //
                return listItems(sender, Integer.parseInt(args[1]));
            case "sendnotificationvote":
                return sendNotificationVote(sender, args);
            case "vote":
                if(DateController.dateFormatIsValid(null, args[1]) && (sender instanceof Player player) ) {
                    return getVote(sender, args[1], player.getName());
                }else{
                    return getVote(sender, null, args[1]);
                }
            case "topvotes":
                if(DateController.dateFormatIsValid(null,args[1])) {
                    return getVotes(sender, args[1], "0");
                }else {
                    return getVotes(sender, null,args[1]);
                }
            default:
                return false;
        }
    }

    public boolean onCommandSize3(CommandSender sender, Command command, String s, String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "bag":
                return bag3arg(sender, args);
            case "give":
                if (args.length < 3) {
                    sender.sendMessage(
                            text("[RR] ❌ Utilisation : /commande give <pseudo> <nombre>")
                                    .color(NamedTextColor.RED)
                    );
                    return true;
                }

                String pseudo = args[1];
                int nbGive;

                try {
                    nbGive = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(
                            text("[RR] ❌ '" + args[2] + "' n'est pas un nombre valide.")
                                    .color(NamedTextColor.RED)
                    );
                    return true;
                }

                try {
                    return giveMoreRandomItem(sender, pseudo, nbGive);
                } catch (Exception e) {
                    sender.sendMessage(
                            text("[RR] ❌ Une erreur est survenue : " + e.getMessage())
                                    .color(NamedTextColor.RED)
                    );
                    e.printStackTrace(); // utile en dev
                    return true;
                }
            case "get":
                return bagGetOneItem(sender, args);
            case "vote":
                return getVote(sender, args[2], args[1]);
            case "topvotes":
                return getVotes(sender,args[1],args[2]);
            default:
                return false;
        }
    }

    private boolean getVote(CommandSender sender, String date, String cibleName) throws Exception {
        boolean debug = RandomReward.getInstance().getConfigManager().getDebug();

        if(date!=null) {
            if (! DateController.dateFormatIsValid(sender, date)) {
                return false;
            }
        }

        Player ciblePlayer = null;
        String uuid = "";

        if(cibleName != null) {
            // Essaye d'obtenir un joueur en ligne avec ce pseudo
            ciblePlayer = PlayerController.getPlayertoPseudo(sender, cibleName);

            if(ciblePlayer == null) {
                // Aucun joueur en ligne trouvé, message d'erreur
                if(debug){sender.sendMessage("[RR] no cibleplayer found");}

                // Essaye de récupérer l'UUID depuis l'API Mojang (joueur hors ligne)
                UUID uuid1 = PlayerController.fetchUUIDFromMojang(cibleName);

                if(uuid1 == null) {
                    // Aucun UUID trouvé pour ce pseudo : pseudo probablement inexistant
                    sender.sendMessage("[RR] no uuid found with this pseudo ERROR_UUID_NOT_FOUND");
                    return false;
                } else {
                    // UUID récupéré avec succès depuis Mojang
                    uuid = uuid1.toString();
                }

            } else {
                // Le joueur est en ligne → récupérer son UUID via Bukkit
                uuid = ciblePlayer.getUniqueId().toString();
            }
        }



        //si le sender = joueur
        if(sender instanceof Player player) {
            //si pas de nom
            if(cibleName==null) {
                if (date == null) {
                    if(debug){sender.sendMessage("[RR] debug : 11");}
                    // s'il n'y a pas de date ni de nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(player,player.getUniqueId().toString(), player.getName());
                }else{
                        if(debug){sender.sendMessage("[RR] debug : 12");}
                    //s'il y a une date mais pas de nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(player,player.getUniqueId().toString(), player.getName(), date);
                }
            }else {
                if (date == null) {
                    if(debug){sender.sendMessage("debug rr : 21");}
                    // s'il n'y a pas de date mais un nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(player, uuid, cibleName);
                }else{
                        if(debug){sender.sendMessage("[RR] debug : 22");}
                    // s'il y a une date et un nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(player, uuid, cibleName, date);
                }
            }
        }
        //si le sender = console
        else{
            //si pas de nom
            if(cibleName==null) {
                sender.sendMessage("[RR] La console ne dispose pas de point de vote.");
            }else {
                if (date == null) {
                    if(debug){sender.sendMessage("[RR] debug : 31");}
                    // s'il n'y a pas de date mais un nom
                    NotificationController.getCurrentMonthVoteForOnePlayer(sender, uuid, cibleName);
                }else{
                    if(debug){sender.sendMessage("[RR] debug : 32");}
                    // s'il y a une date et un nom
                    NotificationController.getSelectedMonthVoteForOnePlayer(sender, uuid, cibleName, date);
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
            if (! DateController.dateFormatIsValid(sender, date)) {
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
                sender.sendMessage(text("[RR] Index invalide. Utilisez un nombre positif (ex: 1, 2, 3...)").color(NamedTextColor.RED));
                return false;
            }
        }

        List<Map.Entry<String, Integer>> voteList = new ArrayList<>(votes.entrySet());

        sender.sendMessage(
                text("================== RandomReward ==================").color(NamedTextColor.AQUA)
        );
        sender.sendMessage(
                text("================= ", NamedTextColor.AQUA)
                        .append(text(DateController.getCurrentDateMmmmYyyy(), NamedTextColor.WHITE))
                        .append(text(" ==================", NamedTextColor.AQUA))
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

    public static void sendPaginationFooter(CommandSender sender, int index, int max) {
        TextComponent.Builder footer = text();

        if (max == 0) max = 1; // Pour éviter un max à 0

        // Page précédente
        if (index > 1) {
            String space = index < 10 ? "  " : index < 100 ? " " : "";
            footer.append(text("<< Page " + index + space, NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr getVotes " + (index - 1))));
        } else {
            footer.append(text("=======", NamedTextColor.AQUA));
        }

        // Centre
        String indexCenter = index < 10 ? ".." + index + ".." : index < 100 ? "." + index + "." : String.valueOf(index);
        String maxCenter = max < 10 ? ".." + max + ".." : max < 100 ? "." + max + "." : String.valueOf(max);
        footer.append(text("=========== ", NamedTextColor.AQUA));
        footer.append(text("(" + indexCenter + "/" + maxCenter + ")", NamedTextColor.WHITE));
        footer.append(text(" ===========", NamedTextColor.AQUA));

        // Page suivante (corrigé)
        if (index < max) {
            String space = (index + 1) < 10 ? "  " : (index + 1) < 100 ? " " : "";
            footer.append(text("Page " + (index + 1) + space + " >>", NamedTextColor.WHITE)
                    .clickEvent(ClickEvent.runCommand("/rr getVotes " + (index + 1))));
        } else {
            footer.append(text("===========", NamedTextColor.AQUA));
        }

        sender.sendMessage(footer.build());
    }

    /**
     * Affiche la première page des statistiques des récompenses
     * @param sender console ou player
     * @return vrai si affichage réussi
     */
    private boolean listItems(CommandSender sender){
        return listItems(sender, 0);
    }

    /**
     * Affiche la page (indexPage) des statistiques des récompenses
     * @param sender console ou player
     * @param indexPage index par defaut 1
     * @return vrai si affichage réussi
     */
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

    /**
     * Recharge le plugin
     * @param sender player (admin) ou console
     * @param confirm vrai si confirme faux sinon
     * @return vrai si rechargement autorisé
     */
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

    /**
     * Distribue des items du bag à son propriétaire tant que son inventaire a de la place ou que son bag n'est pas vide
     * @param sender player
     * @return vrai si le don d'items à bien était effectué
     * @throws Exception bag inaccessible
     */
    private boolean bagGetAll(CommandSender sender) throws Exception {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }
        List<RewardDB> bag = ApiRequestManager.getBag(String.valueOf(player.getUniqueId()));
        boolean invSpaceValide = invSpace(player.getInventory())>0;
        for(int i=0; i<bag.size() && invSpaceValide; i++) {
            giveOneItem(player, bag.get(i));
            if(bag.size()>i+1){
                invSpaceValide = invSpace(player.getInventory())>0;
            } else invSpaceValide = false;
        }
        return true;
    }

    /**
     * Affiche la première page du sac du joueur si existant
     * @param sender player
     * @return vrai si sender = joueur, false sinon
     * @throws Exception bag inaccessible
     */
    private boolean bag(CommandSender sender) throws Exception {
        if(sender instanceof Player player) {
            return BagController.getBag(sender, 1, player.getUniqueId().toString());
        }
        log("[RR] La console ne possède pas de bag. Essayer /bag [pseudo]");
        return false;
    }

    /**
     * Affiche la première page du sac du joueur si existant
     * @param sender player
     * @return vrai si sender = joueur, false sinon
     * @throws Exception bag inaccessible
     */
    private boolean bigbag(CommandSender sender) throws Exception {
        if(sender instanceof Player player) {
            BagInterfaceCommand.bagInterface(player,player.getUniqueId().toString(),1);
            return true;
            //return BagController.getBag(sender, 1, player.getUniqueId().toString());
        }
        log("[RR] La console ne possède pas de bag. Essayer /bag [pseudo]");
        return false;
    }

    private boolean space(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[RR] Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }
        sender.sendMessage(
                text("space inventaire : "+ invSpace(player.getInventory()))
        );
        return true;
    }

    private boolean bigbag2arg(CommandSender sender, String indexPage) throws Exception {
             if(sender instanceof Player player) {
                int index = Integer.parseInt(indexPage);
                BagInterfaceCommand.bagInterface(player,player.getUniqueId().toString(),index);
                return true;
                //return BagController.getBag(sender, 1, player.getUniqueId().toString());
            }
            log("[RR] La console ne possède pas de bag. Essayer /bag [pseudo]");
            return false;
        }

    private boolean giveMoreRandomItem(CommandSender sender, String pseudo, int nbGive) throws Exception {
        if ((sender instanceof Player) && !sender.hasPermission("rr.vnotif")) {
            sender.sendMessage(text("[RR] Seuls les admin peuvent utiliser cette commande.").color(NamedTextColor.RED));
            return false;
        }
        String uuid = ApiMojang.getUUIDFromPseudo(pseudo).toString();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger totalFinished = new AtomicInteger(0);

        for (int i = 0; i < nbGive; i++) {
            Bukkit.getScheduler().runTaskAsynchronously(RandomReward.getInstance(), () -> {
                try {
                    Reward reward = RewardListController.getRandomReward();

                    RewardService.addReward(uuid, reward.getPlugin(), reward.getName(), reward.getCount(),
                            RandomReward.getInstance().getConfigManager());

                    successCount.incrementAndGet();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Incrément après succès ou échec
                int finished = totalFinished.incrementAndGet();

                if (finished == nbGive) {
                    Bukkit.getScheduler().runTask(RandomReward.getInstance(), () -> {
                        if (successCount.get() == nbGive) {
                            sender.sendMessage(text("[RR] " + nbGive + " récompenses données à " + pseudo)
                                    .color(NamedTextColor.GREEN));
                        } else {
                            sender.sendMessage(text("[RR] Seulement " + successCount.get() + "/" + nbGive +
                                    " récompenses données à " + pseudo + ". Certaines ont échoué.")
                                    .color(NamedTextColor.RED));
                        }
                    });
                }
            });
        }
        return true;
    }

    private boolean give(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("rr.vnotif")) {
            if (args.length < 2) {
                sender.sendMessage(text("[RR] Utilisation : /rr give <pseudo>").color(NamedTextColor.RED));
                return false;
            }

            String pseudo = args[1];
            giveOneRandomItem(sender, pseudo); // async call
            return true;

        } else {
            sender.sendMessage(text("[RR] Seuls les administrateurs peuvent utiliser cette commande.")
                    .color(NamedTextColor.RED));
            return false;
        }
    }

    private void giveOneRandomItem(CommandSender sender, String pseudo) {
        Bukkit.getScheduler().runTaskAsynchronously(RandomReward.getInstance(), () -> {
            try {
                String uuid = ApiMojang.getUUIDFromPseudo(pseudo).toString();
                Reward reward = RewardListController.getRandomReward();

                RewardService.addReward(uuid, reward.getPlugin(), reward.getName(), reward.getCount(),
                        RandomReward.getInstance().getConfigManager());

                // Message de succès (sur thread principal)
                Bukkit.getScheduler().runTask(RandomReward.getInstance(), () -> {
                    sender.sendMessage(text("[RR] Récompense ")
                            .append(text(reward.getName()).color(NamedTextColor.GOLD))
                            .append(text(" donnée à "))
                            .append(text(pseudo).color(NamedTextColor.AQUA))
                            .color(NamedTextColor.GRAY));
                });

            } catch (Exception e) {
                Bukkit.getScheduler().runTask(RandomReward.getInstance(), () -> {
                    sender.sendMessage(text("[RR] Aucune récompense donnée à ")
                            .append(text(pseudo).color(NamedTextColor.RED))
                            .append(text(" : pseudo inconnu ou erreur."))
                            .color(NamedTextColor.GRAY));
                });
                e.printStackTrace(); // utile en développement
            }
        });
    }

    /**
     * Retourne la page du bag [indexString]
     * @param sender le joueur ou consol (erreur)
     * @param indexString numéro de page
     * @return print page n°index du bag
     * @throws Exception index n'est pas un nombre
     */
    private boolean bag2Arg(CommandSender sender, String indexString) throws Exception {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[RR] Seuls les joueurs peuvent utiliser cette commande.");
            return false;
        }

        int index;

        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            player.sendMessage("[RR] L'index doit être un nombre !");
            return false;
        }

        return BagController.getBag(sender, index, player.getUniqueId().toString());
    }

    /**
     * Retourne le bag du joueur demandé
     * @param sender le joueur ou consol (erreur)
     * @throws Exception le pseudo ou le sac n'est pas trouvé
     */
    private boolean bag3arg(CommandSender sender, String[] args) throws Exception {
        return BagController.bagOther(sender,args);
    }

    /**
     * Récupérer un item de son sac à [index] si exist.
     * @param sender joueur ou console
     * @param args arguments
     * @return GiveOneItem to player
     */
    private boolean bagGetOneItem(CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[RR] Seuls les joueurs peuvent utiliser cette commande.ERROR_SENDER_CONSOLE");
            return false;
        }

        int indexItem = 0;

        try {
            if(args.length>1) {
                indexItem = Integer.parseInt(args[1]);
                System.out.println("index ="+indexItem);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("[RR] L'index doit être un nombre ! ERROR_INVALIDE_INT_INDEX_BAG");
            return false;
        }

        List<RewardDB> bag =  ApiRequestManager.getBag(player.getUniqueId().toString());
        if(bag.size()>indexItem) {
            int idItem = bag.get(indexItem).getId();
            RewardDB reward = ApiRequestManager.getReward(idItem);


            if(reward == null){
                sender.sendMessage("Il n'existe pas de récompense à cette index ! ERROR_INVALIDE_ID_ITEM_DB");
                return false;
            }

            if(giveOneItem(player, reward)){
                ApiRequestManager.deleteReward(idItem);
                return true;
            }
            return false;

        }else{
            sender.sendMessage("Ton sac n'est pas si grand ! ERROR_INVALIDE_INDEX_ITEM_BAG");
            return false;
        }
    }

    /**
     * Récupérer plusieurs items fois un item de vote
     * * Methode pas très utile pour le bag (nbItem toujours = 1). Utile pour donner X fois
     * * la même récompense (utilisation via siteWeb)
     * @param player Player
     * @param reward RewardDB
     * @param nbItem nbItem
     * @return boolean
     */
    private boolean giveSeveralRewardItem(Player player, RewardDB reward, int nbItem) {
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

    /**
     * Give un RexardDB à un joueur en fonction du plugin du reward.
     * @param player joueur receveur
     * @param reward récompense à donner
     * @return vraie si objet donné faux di item inconnu.
     */
    public static boolean giveOneItem(Player player, RewardDB reward) {
        String itemName = reward.getName();
        log("ir give " + player.getName() + " " + reward.getName());

        String command = switch (reward.getPlugin().toLowerCase(Locale.ROOT)) {
            case "minecraft" -> "give " + player.getName() + " " + itemName + " " + reward.count;
            case "itemreward" -> getStringCommandIR(player,reward);
            case "itemsadder" -> "iagive " + player.getName() + " " + itemName + " " + reward.count;
            case "ecoitems" -> "ecoitems give " + player.getName() + " " + itemName + " " + reward.count;
            default -> "say [error] RandomBagReward - plugin inconnu.";
        };
        System.out.println("give récompense : "+reward.getName() +"plugin : "+reward.getPlugin() + " à "+player.getName());
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
        return true;
    }

    /**
     * Give pour IR give conversion suite à la spécificité des FlyPotion_X
     * @param player player cible
     * @param reward reward ir
     * @return
     */
    private static String getStringCommandIR(Player player, RewardDB reward) {
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

    /**
     * Envoie une notification de vote au siteweb cible (config)
     * @param sender console ou player (rr.vnotif)
     * @param args arguments /rr vnotif [pseudo]
     * @return vrai si requête envoyée
     * @throws Exception retour de requête invalide
     */
    private boolean sendNotificationVote(CommandSender sender, String[] args) throws Exception {
        if(sender.hasPermission("rr.vnotif")) {
            ApiRequestManager.sendNotificationVote(sender, args[1]);
            sender.sendMessage(
                    text("send notification vote").color(NamedTextColor.AQUA)
            );
        }
        return true;
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
