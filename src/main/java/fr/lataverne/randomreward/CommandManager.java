package fr.lataverne.randomreward;

import fr.lataverne.randomreward.Stock.Bag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CommandManager implements CommandExecutor {

    HashMap<String, Bag> bags;
    public boolean DEBUG = false;

    public CommandManager(HashMap<String,Bag> initialBags){
        bags = initialBags;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if ( sender instanceof Player && sender.hasPermission("rr.bag.read")){
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED +"Mauvaise utilisation !");
                return false;
            }

//            if(args[0].equalsIgnoreCase("bag") && sender.hasPermission("rr.bag.read.gui") && ! args[1].equalsIgnoreCase("list")){
//                Bag bag = new Bag();
//                bag.open((Player) sender);
//                return true;
//            }


            if(args[0].equalsIgnoreCase("baglist")
                    || args[0].equalsIgnoreCase("bag")) {
                if(args.length>1) {
                    if (Objects.equals(args[1], "list") && args.length > 2)
                        args[1] = args[2];
                    if (Integer.parseInt(args[1]) > 0) {
                        printListBag((Player) sender, Integer.parseInt(args[1]));
                        return true;
                    }
                }
                printListBag((Player) sender, 1);
                return true;
            }

            if(args[0].equalsIgnoreCase("space")) {
                sender.sendMessage(ChatColor.AQUA + "Quelle place incroyable " + invSpace(((Player) sender).getInventory()) + " de libre !");
                return true;
            }
        }

        if (sender instanceof ConsoleCommandSender || sender instanceof Player && sender.hasPermission("rr.bag.get")){
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED +"Mauvaise utilisation !");
                return false;
            }
            if(args[0].equalsIgnoreCase("get")) {
                if (args.length == 2) {
                    assert sender instanceof Player;
                    giveRewardToPlayer((Player) sender, args[1], true);
                }
                else
                    sender.sendMessage(ChatColor.RED+"Id manquant : /rr get [index] ou utiliser /rr getall");

                return true;
            }
            if(args[0].equalsIgnoreCase("getAll")) {
                if(bags.containsKey(((Player)sender).getUniqueId().toString())) {
                    assert sender instanceof Player;
                    while (!bags.get(((Player) sender).getUniqueId().toString()).rewards.isEmpty() && invSpace(((Player) sender).getInventory()) != 0) {
                        giveRewardToPlayer((Player) sender, String.valueOf(1), false);
                }
                }else
                    sender.sendMessage(ChatColor.DARK_PURPLE+"Vous ne possédez aucun sac");

                return true;
            }

        }

        if (sender instanceof ConsoleCommandSender || sender instanceof Player && sender.hasPermission("rr.give")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED +"Mauvaise utilisation !");
                return false;
            }

            if(args[0].equalsIgnoreCase("list")) {
                this.listItems(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (args.length<3) {
                    addRewardInBag(args[1]);
                }else{
                    if(args.length == 3 ) {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                this.giveCommand(player);
                            }
                        }
                    }
                    if(args.length == 4){
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                this.giveCommand(player);
                            }
                            SendRequestTopVote.send(
                                   player,
                                    args[3]
                            );
                        }
                    }
                }
                return true;
            }

        }
        if(args[0].equalsIgnoreCase("help"))
        {
            sender.sendMessage(ChatColor.AQUA+"==============RandomReward==============");
            sender.sendMessage(ChatColor.AQUA+"/rr [commande] [argument1] [argument2]  ");
            sender.sendMessage(ChatColor.AQUA+"baglist    "+ChatColor.WHITE+": Détail du contenu du sac");
            sender.sendMessage(ChatColor.AQUA+"bag list   "+ChatColor.WHITE+": Voir baglist");
            sender.sendMessage(ChatColor.AQUA+"getAll     "+ChatColor.WHITE+": Vide son sac dans l'inventaire");
            sender.sendMessage(ChatColor.AQUA+"get [id]   "+ChatColor.WHITE+": Récupère la récompense  la Ième place dans le bag");
            sender.sendMessage(ChatColor.AQUA+"help       "+ChatColor.WHITE+": Informations sur les commandes disponibles");
            sender.sendMessage(ChatColor.AQUA+"adminhelp "+ChatColor.WHITE+": Information pour les admins");
            sender.sendMessage(ChatColor.AQUA+"space     "+ChatColor.WHITE+": Do you love kitty? ");
            sender.sendMessage(ChatColor.AQUA+"==============RandomReward==============");
            return true;
        }

        if(args[0].equalsIgnoreCase("adminhelp"))
            if(sender instanceof ConsoleCommandSender || sender instanceof Player && sender.hasPermission("rr.admin.help"))
            {
                sender.sendMessage(ChatColor.AQUA+"==============RandomReward==============");
                sender.sendMessage(ChatColor.AQUA+"/rr [commande] [argument1] [argument2]  ");
                sender.sendMessage(ChatColor.AQUA+"give [nom_player]          ");
                sender.sendMessage(ChatColor.WHITE+": donne une récompense dans le SAC du joueur");
                sender.sendMessage(ChatColor.AQUA+"give [nom_player] [nombre] ");
                sender.sendMessage(ChatColor.WHITE+": donne X récompenses dans l'INVENTAIRE du joueur");
                sender.sendMessage(ChatColor.AQUA+"list                       ");
                sender.sendMessage(ChatColor.WHITE+": Affiche les récompenses et les % associés ");
                sender.sendMessage(ChatColor.AQUA+"get [id]                   ");
                sender.sendMessage(ChatColor.WHITE+": récupère la récompense  la Ième place dans le bag");
                sender.sendMessage(ChatColor.AQUA+"==============RandomReward==============");
                return true;
            }

        if(sender instanceof Player) sender.sendMessage(ChatColor.RED+"Vous n'avez pas la permission d'executer cette commande ou commande inexistante");
        return false;
    }

    private void giveRewardToPlayer(Player player, String arg1, boolean print) {
        int index = Integer.parseInt(arg1);
        if (player!=null)
            if(bags.containsKey(player.getUniqueId().toString())) {
                bags.get(player.getUniqueId().toString()).get(player,index, print);
            }else
                player.sendMessage(ChatColor.DARK_PURPLE+"Vous ne possédez aucun sac");
    }

    private void printListBag(Player player,int index) {
        debug("lecture du sac");
            if(bags.containsKey(player.getUniqueId().toString())) {
                    bags.get(player.getUniqueId().toString()).printList(player, index);
                debug("lecture sac");
            }else {
                player.sendMessage(ChatColor.DARK_PURPLE + "Vous ne possédez aucun sac");
                debug("lecture annulée: sac du joueur innexistant");
            }
    }

    private void addRewardInBag(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        String uuid;
        if(player == null)
            uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
        else
         uuid = player.getUniqueId().toString();
        if(! bags.containsKey(uuid)) {
            Bag bag = new Bag();
            bags.put(uuid, bag);
        }

        bags.get(uuid).addReward(player, uuid);
        bags.get(uuid).updateBag(uuid);
    }

    private void listItems(CommandSender sender) {
        ArrayList<String> list = RandomReward.getInstance().getList();
        sender.sendMessage(ChatColor.AQUA + "================== RandomReward ==================");
        for(String string : list){
            sender.sendMessage(ChatColor.WHITE + string);
        }
        sender.sendMessage(ChatColor.AQUA + "================== RandomReward ==================");
    }

    private void giveCommand(Player player) {
        RandomBuilder rb = RandomReward.getInstance().getRandomBuilder();

        Reward reward = rb.getRandomReward();
        //System.out.println("ir give " + player.getDisplayName() + " " + reward.nomItem);

        String command = switch (reward.getPlugin()) {
            case "minecraft" -> "give " + player.getDisplayName() + " " + reward.nomItem + " " + reward.count;
            case "itemreward" -> getStringCommandIR(reward, player);
            case "itemsadder" -> "iagive " + player.getDisplayName() + " " + reward.nomItem + " " + reward.count ;
            case "ecoitems" -> "ecoitems give " + player.getDisplayName() + " " + reward.nomItem + " " + reward.count;
            default -> "say [error] RandomBagReward - plugin inconnu.";
        };


        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

    private String getStringCommandIR(Reward reward,Player player) {
        String commande = "ir give " + player.getDisplayName() ;
        String suiteCommande = switch(reward.nomItem){
            case "FlyPotion_1" ->  " FlyPotion " + reward.count + " 1";
            case "FlyPotion_2" ->  " FlyPotion " + reward.count + " 2";
            case "FlyPotion_3" ->  " FlyPotion " + reward.count + " 3";
            case "FlyPotion_4" ->  " FlyPotion " + reward.getCount() + " 4";
            default -> " "+ reward.nomItem +" "+ reward.count;
        };
        return commande + suiteCommande;
    }

    public int invSpace (PlayerInventory inv, Material m) {
        int count = 0;
        for (int slot = 0; slot < 36; slot ++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) {
                count += m.getMaxStackSize();
            }
            if (is != null) {
                if (is.getType() == m){
                    count += (m.getMaxStackSize() - is.getAmount());
                }
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

    public void add(String playerName, Bag bag) {
        Player player = Bukkit.getPlayer(playerName);
        String uuid = player.getUniqueId().toString();
        if(! bags.containsKey(uuid))
            bags.put(uuid,bag);
    }

    private void debug(String string) {
        if (DEBUG)
            Bukkit.getConsoleSender().sendMessage(string);
    }

}
