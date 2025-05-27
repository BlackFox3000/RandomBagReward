package fr.lataverne.randomreward.gui;

import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.commands.CommandManager;
import fr.lataverne.randomreward.models.RewardDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BagInterfaceCommand {

    public static void bagInterface(Player player, String ownerUUID, int page) throws Exception {
        List<RewardDB> rewards = ApiRequestManager.getBag(ownerUUID);

        if (rewards == null || rewards.isEmpty()) {
            player.sendMessage("Le sac est vide.");
            return;
        }

        int totalPages = (int) Math.ceil(rewards.size() / 45.0);
        if (page < 1 || page > totalPages) {
            player.sendMessage("Page invalide.");
            return;
        }

        BagInventoryHolder holder = new BagInventoryHolder(ownerUUID, page);
        Inventory inv = Bukkit.createInventory(holder, 54,
                "Sac de " + Bukkit.getOfflinePlayer(java.util.UUID.fromString(ownerUUID)).getName() + " - Page " + page);

        int start = (page - 1) * 45;
        int end = Math.min(start + 45, rewards.size());

        for (int i = start; i < end; i++) {
            RewardDB reward = rewards.get(i);
            ItemStack item = reward.getItemStack();
            item.setAmount(Math.min(reward.getCount(), item.getMaxStackSize()));
            int slot = i - start;
            inv.setItem(slot, item);
            holder.setRewardForSlot(slot, reward); // 🔥 C’est ici qu’on associe bien le slot au RewardDB
        }

        // Navigation
        if (page > 1) {
            ItemStack previous = new ItemStack(Material.WRITTEN_BOOK);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Page n°" + (page - 1));
            previous.setItemMeta(meta);
            inv.setItem(45, previous);
        }

        if (page < totalPages) {
            ItemStack next = new ItemStack(Material.WRITTEN_BOOK);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Page n°" + (page + 1));
            next.setItemMeta(meta);
            inv.setItem(53, next);
        }

        player.openInventory(inv);
    }

    public static List<List<ItemStack>> getBagPages(String ownerUUID) throws Exception {
        List<RewardDB> rewards = ApiRequestManager.getBag(ownerUUID);
        List<List<ItemStack>> pages = new ArrayList<>();

        if (rewards == null || rewards.isEmpty()) {
            return pages;
        }

        List<ItemStack> currentPage = new ArrayList<>();
        for (RewardDB reward : rewards) {
            ItemStack item = reward.getItemStack();
            item.setAmount(Math.min(reward.getCount(), item.getMaxStackSize()));
            currentPage.add(item);

            if (currentPage.size() == 45) {
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }
        }

        if (!currentPage.isEmpty()) {
            pages.add(currentPage);
        }

        return pages;
    }
}
