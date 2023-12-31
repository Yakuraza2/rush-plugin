package org.won.staff.rush.shops;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.won.staff.rush.Rush;

public class Inventories {

    private static Rush main;
    public Inventories(Rush main) {
        this.main = main;}
    private Inventory shop = Bukkit.createInventory(null, 9, (String) main.getConfig().get("shops.categories.gui-name"));

    public Inventory Categories(){
        return shop;
    }
}
