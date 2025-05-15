package fr.lataverne.randomreward.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;

import java.util.Objects;

public class Reward {

    @JsonProperty("id")
    int id;
    @JsonProperty("uuid")
    String uuid;
    @JsonProperty("plugin")
    String plugin;
    @JsonProperty("nomItem")
    String nomItem;
    @JsonProperty("count")
    int count;
    @JsonProperty("chance")
    double chance;
    @JsonProperty("index")
    int index;
    @JsonProperty("isCustomItem")
    boolean isCustomItem;

    // Constructeur sans arguments pour la sérialisation JSON (Jackson)
    @JsonCreator
    public Reward() {}

    // Constructeur personnalisé pour le parsing strict du fichier
    public Reward(String[] words) {
        if (words.length != 4) {
            throw new IllegalArgumentException("Format invalide pour Reward : " + String.join(" ", words));
        }

        this.plugin = words[0];
        this.nomItem = words[1];

        // Parsing de la quantité
        try {
            this.count = Integer.parseInt(words[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantité invalide : " + words[2]);
        }

        // Parsing de la chance avec gestion des virgules en français
        try {
            this.chance = Double.parseDouble(words[3].replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Chance invalide : " + words[3]);
        }
    }

    // Getters
    public double getChance() {
        return this.chance;
    }

    public int getCount() {
        return this.count;
    }

    public String getPlugin() {
        return this.plugin;
    }

    public String getName() {
        return this.nomItem;
    }

    public String getString() {
        return (this.nomItem + " " + this.count + " " + this.chance + "%");
    }

    // Méthode pour afficher la récompense dans la console
    public void print() {
        Bukkit.getConsoleSender().sendMessage(this.nomItem + " " + this.count + " " + this.chance + "% ");
    }

    // Méthode pour l'affichage en mode test (console standard)
    public void printTest() {
        System.out.println(this.nomItem + " " + this.count + " " + this.chance + "% ");
    }

    // Méthode pour vérifier si l'item est un custom (éventuellement basée sur une liste pré-définie)
    private boolean isCustomItem(String word) {
        return (word.equalsIgnoreCase("GoblinPickaxe") ||
                word.equalsIgnoreCase("GiantBoots") ||
                word.equalsIgnoreCase("UnbreakableHoe") ||
                word.equalsIgnoreCase("RawBear") ||
                word.equalsIgnoreCase("RawHorse") ||
                word.equalsIgnoreCase("CookedBear") ||
                word.equalsIgnoreCase("CookedHorse") ||
                word.equalsIgnoreCase("ULU") ||
                word.equalsIgnoreCase("IndianSpear") ||
                word.equalsIgnoreCase("BaseballBat") ||
                word.equalsIgnoreCase("FlyPotion") ||
                word.equalsIgnoreCase("PhantomPotion") ||
                word.equalsIgnoreCase("MiningPotion") ||
                word.equalsIgnoreCase("CreeperPotion") ||
                word.equalsIgnoreCase("SwimmingPotion"));
    }
}
