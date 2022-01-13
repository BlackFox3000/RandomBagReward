package fr.lataverne.randomreward.Stock;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClickEvent implements Listener {

    @EventHandler
    public void clickEvent(InventoryClickEvent e){
//        if(e != null) {
//            List<String> a = new ArrayList<>();
//            a.add("Page");
//            if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getLore() == a)
//                e.setCancelled(true);
//        }
    }
}
