package fr.lataverne.randomreward;

import fr.lataverne.randomreward.Stock.Bag;
import fr.lataverne.randomreward.Stock.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RandomReward extends JavaPlugin {

    private static RandomReward instance;
    private RandomBuilder randomBuilder;

    private final boolean DEBUG = true;

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        /* Fichier : rewards.txt */

        File file = new File(getDataFolder(), "rewards.txt");
        if (!file.exists()) {// saves it to your plugin's data folder if it doesn't exist already
            file.getParentFile().mkdirs();
            try {
                debug("creation du fichier !! ");
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else
            debug("le fichier existe ! ");

        instance = this;

        /* Chargement des Bags */
        HashMap<String, Bag> initialBags = null;
        try {
            initialBags = getSaveFile();
            if(initialBags==null)
                debug("echec du chargements des bags save");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(initialBags == null)
            initialBags = new HashMap<String, Bag>();

        commandManager = new CommandManager(initialBags);
        try {
            Objects.requireNonNull(this.getCommand("randomreward")).setExecutor(commandManager);
        } catch (NullPointerException ex) {
            Bukkit.getConsoleSender().sendMessage("Error !!!!");
            this.setEnabled(false);
        }

        randomBuilder = new RandomBuilder(file);
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward activé");

        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
    }

//    public static void main(String[] args) {
//        Reward ULU = new Reward(("itemreward ULU 1 1.6").split(" "));
//        Reward dirt = new Reward(("minecraft grass_block 64 3.4").split(" "));
//
//        Reward dirt2 = new Reward(("minecraft grass_blocko 64 3.4").split(" "));
//        Bag bag = new Bag();
//        bag.add(ULU);
//        bag.add(dirt);
//        bag.add(dirt2);
//
//        bag.updateBag("ocemay");
//       // Bag ocemay = new Bag("[{\"plugin\":\"itemreward\",\"nomItem\":\"ULU\",\"count\":1,\"chance\":1.6,\"index\":0,\"isCustomItem\":true,\"otherArg\":[]},{\"plugin\":\"minecraft\",\"nomItem\":\"grass_block\",\"count\":64,\"chance\":3.4,\"index\":0,\"isCustomItem\":false,\"otherArg\":[]}]");
//        //System.out.println("==============");
//       // ocemay.print();
//      //  RandomReward.getSaveFile();
//    }

    public HashMap<String, Bag> getSaveFile() throws IOException {
        File folder = new File("plugins/RandomReward/players/");
        HashMap<String, Bag> bags = new HashMap<>();

        if (folder.listFiles() == null) {
            debug("[RandomReward] create folder 'player/'");
            Path path = Paths.get("plugins/RandomReward/players/");
            folder.mkdirs();
            Files.createDirectories(path);
            folder = new File("plugins/RandomReward/players/");
    }
        else
            debug("[RandomRewar] Fichier '/player/' existant" );

        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                FileReader fr = new FileReader(fileEntry);
                BufferedReader br = new BufferedReader(fr);
                String line;
                StringBuilder json = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    json.append(line);
                }
                Bag bag = new Bag(json.toString());
                    String name = fileEntry.getName().replace(".txt", "");
                    bags.put(name, bag);
            }
        }
        return bags;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward désactivé");
    }

    public static RandomReward getInstance() {
        return instance;
    }

    public RandomBuilder getRandomBuilder() {
        return randomBuilder;
    }

    public ArrayList<String> getList() {
        return  randomBuilder.getList();
    }

    public void debug(String string){
        if(DEBUG)
            Bukkit.getConsoleSender().sendMessage(string);
    }
}
