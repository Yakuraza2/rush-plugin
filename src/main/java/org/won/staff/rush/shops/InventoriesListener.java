package org.won.staff.rush.shops;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;

public class InventoriesListener implements Listener {

    private final Rush main;
    public InventoriesListener(Rush main) {this.main = main;}

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e){
        Inventories inv = new Inventories(main);

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if(!(main.isState(GState.PLAYING) || player.hasPermission("rush.admin")) || !(entity.getScoreboardTags().contains("rush"))) return;

        e.setCancelled(true);
        if(entity.getScoreboardTags().contains("shops.categories"))  player.openInventory(inv.Shop());
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e){
        Inventories inv = new Inventories(main);

        main.debug("Inventory click");

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(e.getView().getTitle().equalsIgnoreCase(main.getConfig().getString("shops.gui-name"))){
            main.debug("Inventory Click dans le shop");
            e.setCancelled(true);
            if(item==null) {
                main.debug("Clicked Item == null, on fait rien.");
                return;
            }
            main.debug("Clicked Item != null, Debut de la boucle.");
            for(int i=0; i<main.getConfig().getInt("shops.size"); i++){
                String categoriesID = "shops.categories.cat-"+ i;
                main.debug("Searching for " + categoriesID + " ...");

                if(main.getConfig().get(categoriesID) != null){
                    main.debug(categoriesID + " exists !");
                     if(item.getItemMeta().getDisplayName().equals(main.getConfig().getString(categoriesID + ".name")) ){
                         main.debug(categoriesID + " is the item clicked, opening the inventory !");
                         player.openInventory(inv.Categorie(categoriesID));
                         return;
                    } else { main.debug(categoriesID + " is not the item clicked !"); }
                } else {
                    main.debug(categoriesID + " doesn't exists !");
                    return;
                }
            }
        }
    }
}
