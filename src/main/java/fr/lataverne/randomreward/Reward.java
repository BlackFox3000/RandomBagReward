package fr.lataverne.randomreward;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;

public class Reward {
    @JsonProperty("plugin")
    String plugin ;
    @JsonProperty("nomItem")
    String nomItem;
    @JsonProperty("count")
    int count;
    @JsonProperty("chance")
    double chance;
    @JsonProperty("index")
    int index;
    @Deprecated
    //Il faut nettoyer les JSON existant
    // sans quoi une incompatibilité de lire le fichier existe
    @JsonProperty("isCustomItem")
    boolean isCustomItem;
    @JsonProperty("otherArg")
    ArrayList<String> otherArg = new ArrayList<>();
    @JsonProperty("id")
    int id;

    /**
     *
     */
    @JsonCreator
    public Reward(){}


    public Reward(String[] words) {
        this.plugin = words[0];
        this.nomItem = words[1];
        //System.out.println(word[1]);
        this.count = Integer.parseInt(words[2]);
        this.chance = Double.parseDouble(words[3]);
        this.otherArg.addAll(Arrays.asList(words).subList(4, words.length));
    }


    /** private boolean isCustomItem(String word) {
        return (word.equalsIgnoreCase("GoblinPickaxe" ) ||
                word.equalsIgnoreCase("GiantBoots"    ) ||
                word.equalsIgnoreCase("UnbreakableHoe") ||
                word.equalsIgnoreCase("RawBear"       ) ||
                word.equalsIgnoreCase("RawHorse"      ) ||
                word.equalsIgnoreCase("CookedBear"    ) ||
                word.equalsIgnoreCase("CookedHorse"   ) ||
                word.equalsIgnoreCase("ULU"           ) ||
                word.equalsIgnoreCase("IndianSpear"   ) ||
                word.equalsIgnoreCase("BaseballBat"   ) ||
                word.equalsIgnoreCase("FlyPotion"     ) ||
                word.equalsIgnoreCase("PhantomPotion" ) ||
                word.equalsIgnoreCase("MiningPotion"  ) ||
                word.equalsIgnoreCase("CreeperPotion" ) ||
                word.equalsIgnoreCase("SwimmingPotion"));
    } **/

    public double getChance() {
        return this.chance;
    }

    public void print() {
        Bukkit.getConsoleSender().sendMessage(this.nomItem +" "+ this.count +" " + this.chance +"% ");

    }
    public void printTest() {
        System.out.println(this.nomItem +" "+ this.count +" " + this.chance +"% ");

    }

    public String getName() {
        return this.nomItem;
    }

    public String getString() {
        return (this.nomItem +" "+ this.count +" " + this.chance +"% ");
    }

    public int getCount() {
        return count;
    }

    public String getPlugin() {
        return this.plugin;
    }
}
