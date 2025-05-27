package fr.lataverne.randomreward.gui;

import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.api.RewardService;
import fr.lataverne.randomreward.commands.CommandManager;
import fr.lataverne.randomreward.models.RewardDB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BagInterfaceListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof BagInventoryHolder holder)) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;

        try {
            if (slot == 45 && holder.getPage() > 1) {
                new BagInterfaceCommand().bagInterface(player, holder.getOwnerUUID(), holder.getPage() - 1);
            } else if (slot == 53) {
                List<List<ItemStack>> pages = BagInterfaceCommand.getBagPages(holder.getOwnerUUID());
                if (holder.getPage() < pages.size()) {
                    new BagInterfaceCommand().bagInterface(player, holder.getOwnerUUID(), holder.getPage() + 1);
                }
            } else if (slot < 45 && player.getUniqueId().toString().equals(holder.getOwnerUUID())) {
                RewardDB reward = holder.getRewardForSlot(slot);
                if (reward != null) {
                    CommandManager.giveOneItem(player, reward);
                    RewardService.deleteReward(reward.id, RandomReward.getInstance().getConfigManager());
                    new BagInterfaceCommand().bagInterface(player, holder.getOwnerUUID(), holder.getPage());
                    //player.closeInventory();
                }

                //player.closeInventory();
            }

        } catch (Exception e) {
            player.sendMessage("Erreur : " + e.getMessage());
        }
    }
}
