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

            if(args[0].equalsIgnoreCase("bag") && sender.hasPermission("rr.bag.read.gui") && ! args[1].equalsIgnoreCase("list")){
                Bag bag = new Bag();
                bag.open((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("baglist") || (args[0].equalsIgnoreCase("bag") && args[1].equalsIgnoreCase("list"))) {
                if(args.length>1)
                    if(!Objects.equals(args[1], "list"))
                        if(Integer.parseInt(args[1])>0) {
                            printListBag((Player) sender, Integer.parseInt(args[1]));
                            return true;
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
                while (! bags.get(sender.getName()).rewards.isEmpty()) {
                    assert sender instanceof Player;
                    if (invSpace(((Player) sender).getInventory()) == 0) break;
                        giveRewardToPlayer((Player) sender, String.valueOf(0), false);
                }

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
                    Player player = Bukkit.getPlayer(args[1]);
                    if(player!=null) {
                        for (int i = 0; i < Integer.parseInt(args[2]); i++)
                            this.giveCommand(player);
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
            if(bags.containsKey(player.getName())) {
                bags.get(player.getName()).get(player,index, print);
            }else
                player.sendMessage(ChatColor.DARK_PURPLE+"Vous ne possédez aucun sac");
    }

    private void printListBag(Player player,int index) {
        debug("lecture du sac");
        String playerName = player.getName();
            if(bags.containsKey(playerName)) {
                    bags.get(playerName).printList(player, index);
                debug("lecture sac");
            }else {
                player.sendMessage(ChatColor.DARK_PURPLE + "Vous ne possédez aucun sac");
                debug("lecture annulée: sac du joueur innexistant");
            }
            
    }

    private void addRewardInBag(String playerName) {
        if(! bags.containsKey(playerName)) {
            Bag bag = new Bag();
            bags.put(playerName, bag);
        }
        Player player = Bukkit.getPlayer(playerName);
        bags.get(playerName).addReward(player);
        bags.get(playerName).updateBag(playerName);
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
        String command = reward.isCustomItem
                ? "ir give " + player.getDisplayName() + " " + reward.nomItem
                : "give " + player.getDisplayName() + " " + reward.nomItem+" "+reward.count;

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
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
        if(! bags.containsKey(playerName))
            bags.put(playerName,bag);
    }

    private void debug(String string) {
        if (DEBUG)
            System.out.println(string);
    }

}
