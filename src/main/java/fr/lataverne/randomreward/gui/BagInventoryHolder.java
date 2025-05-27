package fr.lataverne.randomreward.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.lataverne.randomreward.models.RewardDB;

import java.util.HashMap;
import java.util.Map;

public class BagInventoryHolder implements InventoryHolder {

    private final String ownerUUID;
    private final int page;
    private final Map<Integer, RewardDB> slotRewardMap = new HashMap<>();

    public BagInventoryHolder(String ownerUUID, int page) {
        this.ownerUUID = ownerUUID;
        this.page = page;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public int getPage() {
        return page;
    }

    public void setRewardForSlot(int slot, RewardDB reward) {
        slotRewardMap.put(slot, reward);
    }

    public RewardDB getRewardForSlot(int slot) {
        return slotRewardMap.get(slot);
    }

    @Override
    public Inventory getInventory() {
        return null; // Ne sert que si tu gères l’inventaire manuellement
    }
}

