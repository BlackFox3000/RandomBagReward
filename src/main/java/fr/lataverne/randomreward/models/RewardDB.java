package fr.lataverne.randomreward.models;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class RewardDB {
    public int id;
    public String uuid;
    public String plugin;
    public String item;
    public int count;
    public String date; // au format YYYY-MM-DD

    public RewardDB(String uuid, String plugin, String item, int count, String date) {
        this.uuid = uuid;
        this.plugin = plugin;
        this.item = item;
        this.count = count;
        this.date = date;
    }

    public RewardDB() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return this.item;
    }

    public String getString() {
        return (this.item +" "+ this.count);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id=id;
    }

    // Méthode utilitaire pour obtenir le Material
    public Material getMaterial() {
        try {
            return Material.valueOf(item.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Material invalide: " + item);
            return null;
        }
    }
}
