package fr.lataverne.randomreward;

import fr.lataverne.randomreward.Stock.Bag;
import fr.lataverne.randomreward.Stock.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RandomReward extends JavaPlugin {

    private static RandomReward instance;
    public String debug = "disabled";
    private RandomBuilder randomBuilder;

    private final boolean DEBUG = true;

    private CommandManager commandManager;

    public String passPhrase = "";
    public String urlVoteSite = "";

    @Override
    public void onEnable() {
        /* Fichier : rewards.txt */

        File fileReward = createIfNotexistRewardFile();
        createOrReadConfSendRequest("/RandomReward/config.yml");


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

        randomBuilder = new RandomBuilder(fileReward);
        Bukkit.getConsoleSender().sendMessage("Plugin RandomReward activé");

        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
    }

    private void createOrReadConfSendRequest(String filePath) {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                debug("creation du fichier conf!! ");
                debug("! FICHIER A CONFIGURER POUR TOP VOTE !");

                file.getParentFile().mkdirs(); // Créer les répertoires parents si nécessaire
                file.createNewFile(); // Créer le fichier

                // Écrire les valeurs par défaut dans le fichier YAML
                FileWriter writer = new FileWriter(file);

                // Écrire les valeurs par défaut dans le fichier YAML
                writer.write("passPhrase: mapassphrase:8000\n");
                writer.write("urlVoteSite: monurl\n");
                writer.write("#debug (enabled/disabled)\n");
                writer.write("debug: enabled\n");
                writer.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readConf(file);
    }

    private File createIfNotexistRewardFile() {
        File file = new File(getDataFolder(), "rewards.txt");
        if (!file.exists()) {// saves it to your plugin's data folder if it doesn't exist already
            file.getParentFile().mkdirs();
            try {
                debug("creation du fichier reward!! ");
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else
            debug("le fichier existe ! ");
        return file;
    }

    private void readConf(File file){
        // Appeler la méthode pour lire le fichier YAML
        Map<String, String> data = readYaml(file.getPath());

        // Récupérer la valeur associée à la clé 'passPhrase'
        this.passPhrase = data.get("passPhrase");
        this.urlVoteSite = data.get("urlVoteSite");
        this.debug = data.get("debug");
        System.out.println("debug::"+debug);

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
    public static Map<String, String> readYaml(String filePath) {
        try {
            // Créer un InputStream à partir du fichier YAML
            InputStream input = new FileInputStream(filePath);

            // Créer un objet Yaml
            Yaml yaml = new Yaml();

            // Charger les données YAML dans une structure de données Map
            Map<String, String> data = yaml.load(input);

            // Retourner la Map contenant les données YAML
            return data;

        } catch (FileNotFoundException e) {
            // Gérer l'erreur si le fichier n'est pas trouvé
            e.printStackTrace();
            return null;
        }
    }

}
