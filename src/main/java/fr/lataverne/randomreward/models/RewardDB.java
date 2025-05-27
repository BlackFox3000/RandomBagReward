package fr.lataverne.randomreward.models;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public ItemStack getItemStack() {
        Material material = getMaterial();

        if (material != null) {
            // Cas des items Minecraft vanilla
            ItemStack itemStack = new ItemStack(material);
            itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§f" + this.getName());
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        // Cas des plugins tiers : on génère un ItemStack de substitution selon le plugin
        return getPlaceholderItem();
    }

    private ItemStack getPlaceholderItem() {
        ItemStack itemStack;

        switch (plugin.toLowerCase()) {
            case "itemreward":
                itemStack = new ItemStack(Material.GOLD_INGOT);
                break;

            case "itemsadder":
                itemStack = new ItemStack(Material.LIME_DYE);
                break;

            case "ecoitems":
                itemStack = new ItemStack(Material.NETHER_STAR);
                break;

            default:
                itemStack = new ItemStack(Material.BARRIER); // plugin inconnu
                break;
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§c" + this.plugin + ": " + this.item);
        itemStack.setItemMeta(meta);

        itemStack.setAmount(1); // toujours 1 visuellement, ou adapter selon contexte

        return itemStack;
    }


}
