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

public class CommandManager implements CommandExecutor {

    HashMap<String, Bag> bags;

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

            if(args[0].equalsIgnoreCase("bag") && sender.hasPermission("rr.bag.read.gui")){
                Bag bag = new Bag();
                System.out.println("test");
                bag.open((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("baglist")) {
                printListBag((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("space")) {
                sender.sendMessage(ChatColor.AQUA + "Quel place incroyable " + invSpace(((Player) sender).getInventory()) + " de libre !");
                return true;
            }
        }

        if (sender instanceof ConsoleCommandSender || sender instanceof Player && sender.hasPermission("rr.bag.get")){
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED +"Mauvaise utilisation !");
                return false;
            }

            if(args[0].equalsIgnoreCase("get")) {
                if (args[1] != null)
                    giveRewardToPlayer((Player) sender, args[1]);
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
                    System.out.println("add in bag");
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

        if(sender instanceof Player) sender.sendMessage(ChatColor.RED+"Vous n'avez pas la permission d'executer cette commande");
        return false;
    }

    private void giveRewardToPlayer(Player player, String arg1) {
        int index = Integer.parseInt(arg1);
        if (player!=null)
            if(bags.containsKey(player.getName())) {
                bags.get(player.getName()).get(player,index);
            }else
                player.sendMessage(ChatColor.DARK_PURPLE+"Vous ne possédez aucun sac");
    }

    private void printListBag(Player player) {
        System.out.println("lecture du sac");
        String playerName = player.getName();
            if(bags.containsKey(playerName)) {
                    bags.get(playerName).printList(player);
                System.out.println("lecture sac");
            }else {
                player.sendMessage(ChatColor.DARK_PURPLE + "Vous ne possédez aucun sac");
                System.out.println("lecture annulée");
            }
            
    }

    private void addRewardInBag(String playerName) {
        System.out.println("add bag 1");
        if(bags == null)
            System.out.println("Bagg nulll !!");
        if(! bags.containsKey(playerName)) {
            System.out.println("bagor");
            Bag bag = new Bag();
            System.out.println("bagy");
            bags.put(playerName, bag);
        }
        System.out.println("add bag 2");
        Player player = Bukkit.getPlayer(playerName);
        bags.get(playerName).addReward(player);
    }

    private void listItems(CommandSender sender) {
        ArrayList<String> list = RandomReward.getInstance().getList();
        sender.sendMessage(ChatColor.AQUA + "================== RandomReward ==================");
        for(String string : list){
            sender.sendMessage(ChatColor.LIGHT_PURPLE + string);
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
}
