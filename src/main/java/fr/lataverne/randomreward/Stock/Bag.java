package fr.lataverne.randomreward.Stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;

import fr.lataverne.randomreward.CommandManager;
import fr.lataverne.randomreward.RandomBuilder;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.Reward;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Bag {

    public ArrayList<Reward> rewards;

    public Bag(){
        rewards = new ArrayList<>();
    }

    public  Bag(String json) throws JsonProcessingException{

        System.out.println("json: "+json);
        final ObjectMapper objectMapper = new ObjectMapper();
        //Bukkit.getConsoleSender().sendMessage("construction bag d'un joueur");
        System.out.println("construction bag d'un joueur");
        rewards = (ArrayList<Reward>) objectMapper.readValue(json, new TypeReference<List<Reward>>(){});
       // rewards = new ArrayList<>(rewards1);
    }

    public void open(Player player){
        Inventory gui = Bukkit.createInventory(player,27, ChatColor.AQUA+"Reward bag");
         setRewardIntoGui(gui,0);
        //ajouter book précedent et next
        player.openInventory(gui);
    }

    public void open(Player player,int index){
        Inventory gui = Bukkit.createInventory(player,27, ChatColor.AQUA+"Reward bag");
        setRewardIntoGui(gui,index);
        //ajouter book précedent et next
        player.openInventory(gui);
    }

    private void setRewardIntoGui(Inventory gui, int index) {
        List<Reward> rewardsPage =rewards.subList(index,rewards.size());

        for(Reward reward : rewardsPage){
            if(gui.getSize() == 18) {
                if(index==0)
                    gui.addItem((ItemStack) null);
                else {
                    ItemStack prec = new ItemStack(Objects.requireNonNull(Material.BOOK));
                    ItemMeta prec_meta = prec.getItemMeta();
                    assert prec_meta != null;
                    prec_meta.setDisplayName(ChatColor.RED+"Page précédente");
                    prec.setItemMeta(prec_meta);
                    gui.addItem(prec);
                }
            }
            if(gui.getSize() == 26 ) {
                if(index==rewardsPage.size())
                    gui.addItem((ItemStack) null);
                else {
                    ItemStack prec = new ItemStack(Objects.requireNonNull(Material.BOOK));
                    ItemMeta prec_meta = prec.getItemMeta();
                    assert prec_meta != null;
                    prec_meta.setDisplayName(ChatColor.RED+"Page suivante");
                    prec.setItemMeta(prec_meta);
                    gui.addItem(prec);
                }
            }
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(reward.getName())));
            gui.addItem(itemStack);
        }

    }

    public void addReward(Player player, String uuid){
        RandomBuilder rb = RandomReward.getInstance().getRandomBuilder();
        Reward reward = rb.getRandomReward();
        rewards.add(reward);
        if(player!=null) {
            TextComponent text = new TextComponent(ChatColor.GREEN + "[RécompenseVote] Merci d'avoir voté ! " + reward.getName() + " ajouté à ton sac. ");
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/rr bag"));
            player.spigot().sendMessage(text);
            updateBag(uuid);
        }
    }

    public void printList(Player player, int index){
        int max = (int) Math.ceil((double)rewards.size()/7);

        //controle anti-dépassement
        if(index>max)
            index = max;
        player.sendMessage("Tu possède "+rewards.size() +" items dans ton sac");

        //Affichage contenue bag

        if(rewards.size()!=0){
            int start = 7 * index-7;

            TextComponent head = new TextComponent(ChatColor.AQUA + "=================Bag==================");
            player.spigot().sendMessage(head);

            for (int i = start; start + 7 > i; i++) {
                if (rewards.size() > i) {
                    Reward reward = rewards.get(i);
                    String spaceQuantity ="";
                    if(rewards.get(i).getCount()<100)
                        spaceQuantity =" ";
                    if(rewards.get(i).getCount()<10)
                        spaceQuantity = "  ";
                    //===============================
                    String spaceIndex ="";
                    if(i<99)
                        spaceIndex =" ";
                    if(i<9)
                        spaceIndex = "  ";
                    TextComponent text = new TextComponent(
                            ChatColor.AQUA + "["+spaceIndex+(i+1)+"]-- " +
                                    ChatColor.WHITE + spaceQuantity + reward.getCount() + ChatColor.AQUA + " -- " + ChatColor.WHITE + reward.getName());
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rr get " + (i+1)));
                    text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "Clique pour obtenir !").create()));
                    player.spigot().sendMessage(text);
                }
            }
            ComponentBuilder foot1 = new ComponentBuilder();

            //Page precedente
            String spaceStringPred = "";
            if (index > 1 ) {
                if (index < 100)
                    spaceStringPred = " ";
                if (index < 10)
                    spaceStringPred = "  ";
                foot1.append(ChatColor.WHITE + "<< Page " + (index - 1) + spaceStringPred);
                foot1.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rr baglist " + (index - 1)));
            } else
                foot1.append(ChatColor.AQUA + "========");

            foot1.append(ChatColor.AQUA + "=========");
            foot1.append("(" + (index) + "/" + max + ")");
            foot1.append(ChatColor.AQUA + "=====");

            //Page suivant
            String spaceStringSuiv = "";
            if (index*7 < rewards.size()  ) {
                if (index < 100)
                    spaceStringSuiv = " ";
                if (index < 10)
                    spaceStringSuiv = "  ";
                foot1.append(ChatColor.AQUA + "==== "+ChatColor.WHITE +"Page " + (index + 1) + spaceStringSuiv + ">>");
                foot1.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rr baglist " + (index + 1)));
            } else
                foot1.append(ChatColor.AQUA + "===========");

            player.spigot().sendMessage(foot1.create());
        }
    }

    public void updateBag(String pseudo){
        //create bag if not exist
        try {
             File myObj = new File("plugins/RandomReward/players/"+pseudo+".txt");
            if (myObj.createNewFile())
                Bukkit.getConsoleSender().sendMessage("création file.txt");
            //else
                //System.out.println("file already exist");

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("plugins/RandomReward/players/"+pseudo+".txt");
            //Write in file
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String json = ow.writeValueAsString(rewards);
            Gson gson = new Gson();
            String jsonArray = gson.toJson(rewards);
            myWriter.write(jsonArray);
            myWriter.close();
            Bukkit.getConsoleSender().sendMessage("Successfully wrote to the file.");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("An error occurred.");
            e.printStackTrace();
        }

    }

    public void get(Player player,int index, boolean print){
        if(rewards.size()>=index && index>0) {
            if(! testSpace(player))
                player.sendMessage(ChatColor.DARK_PURPLE+"Vide ton inventaire !");
            else
                if (rewards.get(index-1) != null) {
                    Reward reward = rewards.get(index-1);
                    rewards.remove(index-1);

//                    String command = reward.isCustomItem()
//                            ? "ir give " + player.getDisplayName() + " " + reward.getName()
//                            : "give " + player.getDisplayName() + " " + reward.getName() + " " + reward.getCount();
                    String command = switch (reward.getPlugin()) {
                        case "minecraft" -> "give " + player.getDisplayName() + " " + reward.getName() + " " + reward.getCount();
                        case "itemreward" -> getStringCommandIR(reward, player);
                        case "itemsadder" -> "iagive " + player.getDisplayName() + " " + reward.getName() + " " + reward.getCount() ;
                        case "ecoitems" -> "ecoitems give " + player.getDisplayName() + " " + reward.getName() + " " + reward.getCount();
                        default -> "say [error] RandomBagReward - plugin inconnu.";
                    };

                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    Bukkit.dispatchCommand(console, command);

                    updateBag(player.getUniqueId().toString());

                    if(print) {
                        //ré-affichage du sac actualisé
                        String command2 = "rr baglist ";
                        Bukkit.dispatchCommand(player, command2);
                    }
                }
        }
        else{
            player.sendMessage(ChatColor.DARK_PURPLE+"Il n'existe pas de récompense à cette emplacement");
        }
    }

    private String getStringCommandIR(Reward reward,Player player) {
        String commande = "ir give " + player.getDisplayName() ;
        String suiteCommande = switch(reward.getName()){
            case "FlyPotion_1" ->  " FlyPotion " + reward.getCount() + " 1";
            case "FlyPotion_2" ->  " FlyPotion " + reward.getCount() + " 2";
            case "FlyPotion_3" ->  " FlyPotion " + reward.getCount() + " 3";
            case "FlyPotion_4" ->  " FlyPotion " + reward.getCount() + " 4";
            default -> " "+ reward.getName() +" "+ reward.getCount();
        };
        return commande + suiteCommande;
    }

    private boolean testSpace(Player player) {
            return true;
    }

    public void add(Reward reward) {
        rewards.add(reward);
    }

    public void print(){
        Bukkit.getConsoleSender().sendMessage("bag contain");
        for(Reward reward:rewards)
            Bukkit.getConsoleSender().sendMessage(reward.getName());
    }
}
