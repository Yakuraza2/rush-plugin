package org.won.staff.rush.shops;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.won.staff.rush.Rush;

import java.util.ArrayList;
import java.util.List;

public class Inventories {

    private Rush main;
    public Inventories(Rush main) {
        this.main = main;}

    public Inventory Shop(){
        ItemStacks item = new ItemStacks(main);

        Inventory shop = Bukkit.createInventory(null, main.getConfig().getInt("shops.size"), main.getConfig().getString("shops.gui-name"));
        for(int i=0; i<shop.getSize(); i++){
            if(main.getConfig().get("shops.categories.cat-" + i) != null){
                shop.setItem(main.getConfig().getInt("shops.categories.cat-" + i + ".slot"), item.create(Material.valueOf(main.getConfig().getString("shops.categories.cat-" + i + ".item")), main.getConfig().getString("shops.categories.cat-" + i + ".name"), 1));
            }
        }

        return shop;
    }

    public Inventory Categorie(String ID){
        ItemStacks item = new ItemStacks(main);

        String name = main.getConfig().getString(ID + ".name");
        int size = main.getConfig().getInt(ID + ".size");

        Inventory shop = Bukkit.createInventory(null, size, name);

        for(int i=0; i<size; i++){
            String itemID = ID + ".items.item-" + i;
            main.debug("Recherche de l'item : " + itemID);

            if(main.getConfig().get(itemID) != null){
                main.debug(itemID + " exists ! Adding it...");
                String itemName = main.getConfig().getString(itemID + ".display-name");
                Material material = Material.valueOf(main.getConfig().getString(itemID + ".item-id"));
                int quantity = main.getConfig().getInt(itemID + ".quantity");
                int slot = main.getConfig().getInt(itemID + ".item-slot");
                int price = main.getConfig().getInt(itemID + ".price");;
                String priceItem = main.getConfig().getString("shops.display-names."+ main.getConfig().getString(itemID + ".price-item").toLowerCase());

                List<String> lore = new ArrayList<>();
                lore.add(main.getConfig().getString("shops.display-names.price-prefix")+" " + price + "x " + priceItem);

                shop.setItem(slot, item.create(material, itemName, quantity, lore));
                main.debug(itemID + " (" + quantity + "x "+ itemName + ") added in the slot " + slot);
            } else break;
        }

        return shop;
    }
}
