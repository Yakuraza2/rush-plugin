package org.won.staff.rush.shops;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.won.staff.rush.Rush;

public class InventoriesListener implements Listener {

    private Rush main;
    public InventoriesListener(Rush main) {this.main = main;}

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e){

        Inventories inv = new Inventories(main);

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if(!(entity.getScoreboardTags().contains("rush"))) return;

        e.setCancelled(true);
        if(entity.getScoreboardTags().contains("shops.categories"))  player.openInventory(inv.Categories());
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e){

    }
}
