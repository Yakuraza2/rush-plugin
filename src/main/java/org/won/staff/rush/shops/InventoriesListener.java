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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import sun.jvm.hotspot.debugger.cdbg.CDebugger;

public class InventoriesListener implements Listener {

    private final Rush main;

    public InventoriesListener(Rush main) {
        this.main = main;
    }

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e) {
        Inventories inv = new Inventories(main);

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if (!(main.isState(GState.PLAYING) || player.hasPermission("rush.admin")) || !(entity.getScoreboardTags().contains("rush")))
            return;

        e.setCancelled(true);
        if (entity.getScoreboardTags().contains("shops.categories")) player.openInventory(inv.Shop());
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        Inventories inv = new Inventories(main);
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;
        if(e.getRawSlot() != e.getSlot()) {
            if(item.getItemMeta() instanceof ArmorMeta) e.setCancelled(true);
            return;
        }


        if (e.getView().getTitle().equalsIgnoreCase(main.getConfig().getString("shops.gui-name"))) {
            e.setCancelled(true);

            for (int i = 0; i < main.getConfig().getInt("shops.size"); i++) {
                String categoriesID = "shops.categories.cat-" + i;
                main.debug("Searching for " + categoriesID + " ...");

                if (main.getConfig().get(categoriesID) != null) {
                    if (item.getItemMeta().getDisplayName().equals(main.getConfig().getString(categoriesID + ".name"))) {
                        player.openInventory(inv.Categorie(categoriesID));
                        return;
                    }
                } else {
                    main.debug(categoriesID + " doesn't exists !");
                    return;
                }
            }
        }

        for (int i = 0; i < main.getConfig().getInt("shops.size"); i++) {
            String categoriesID = "shops.categories.cat-" + i;
            main.debug("Searching for " + categoriesID + " ...");

            if (main.getConfig().get(categoriesID) != null) {
                if (e.getView().getTitle().equalsIgnoreCase(main.getConfig().getString(categoriesID + ".name"))) {
                    e.setCancelled(true);
                    verifyItem(categoriesID, player, item);
                    return;
                }
            } else {
                main.debug(categoriesID + " doesn't exists !");
                return;
            }
        }
    }

    public void verifyItem(String categorie, Player player, ItemStack item) {

        for (int i = 0; i < main.getConfig().getInt(categorie + ".size"); i++) {
            String itemID = categorie + ".items.item-" + i;
            main.debug("Searching for " + itemID + " ...");

            if (main.getConfig().get(itemID) != null) {
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(main.getConfig().getString(itemID + ".display-name"))) {

                    buyItem(itemID, player, item);
                    return;
                }
            } else {
                main.debug(itemID + " doesn't exists !");
                return;
            }
        }
    }

    public void buyItem(String id, Player player, ItemStack item) {
        Material money;
        int price = main.getConfig().getInt(id + ".price");
        String name;

        if (main.getConfig().getString(id + ".price-item").equalsIgnoreCase("bronze")) {
            money = Material.COPPER_INGOT;
            name = main.getConfig().getString("shops.display-names.bronze");
        } else if (main.getConfig().getString(id + ".price-item").equalsIgnoreCase("iron")) {
            money = Material.IRON_INGOT;
            name = main.getConfig().getString("shops.display-names.iron");
        } else if (main.getConfig().getString(id + ".price-item").equalsIgnoreCase("gold")) {
            money = Material.GOLD_INGOT;
            name = main.getConfig().getString("shops.display-names.gold");
        } else if (main.getConfig().getString(id + ".price-item").equalsIgnoreCase("diamond")) {
            money = Material.DIAMOND;
            name = main.getConfig().getString("shops.display-names.diamond");
        } else {
            main.debug("price-item error in the config file for : " + id);
            return;
        }

        ItemStack priceItem = ItemStacks.create(money, name, price);
        if (player.getInventory().contains(money, price)) {

            removeItemFromInventory(player.getInventory(), priceItem);
            main.debug(player.getName() + " just buy " + id);
            player.sendMessage(main.getConfigMessage("item-buy", player).replace("<item>", item.getItemMeta().getDisplayName()));

            if(item.getItemMeta() instanceof ArmorMeta){
                main.debug("ARMOR BUYING");
                ItemStacks.armorGiving(player, item);
            }else{
                player.getInventory().addItem(item);
            }
        } else {
            main.debug(player.getName() + " can't buy " + id + " : not enough money");
        }
    }

    public void removeItemFromInventory(Inventory inv, ItemStack item) {
        if(inv.contains(item)) { // contains the exact item
            inv.remove(item); // remove first time it find this item
        } else { // doesn't contains this item
            for(ItemStack invItem : inv.getContents()) {
                if(invItem != null && invItem.getType().equals(item.getType())) { // if it's this type of item.
                    // You can add other check specially for ItemMeta ...
                    int amount = invItem.getAmount(); // amount of actual item
                    int stay = item.getAmount(); // keep amount

                    if(amount >= stay) { // too many item, just change amount
                        invItem.setAmount(amount - stay); // change amount to remove it
                        break; // stop loop
                    } else if(amount < stay) { // not enough item
                        invItem.setAmount(0); // you can also remove the item by setting air to this slot
                        item.setAmount(stay - amount); // reduce amount of item to delete
                    }
                }
            }
        }
    }
}
