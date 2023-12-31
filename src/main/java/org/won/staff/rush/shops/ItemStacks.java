package org.won.staff.rush.shops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.won.staff.rush.Rush;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ItemStacks {

    private Rush main;
    public ItemStacks(Rush main) {
        this.main = main;}

    public static ItemStack create(Material material, String name, int quantity){
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        if(name != null) meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(Material material, String name, int quantity, List Lore){
        ItemStack item = new ItemStack(material, quantity);
        ItemMeta meta = item.getItemMeta();
        if(name != null) meta.setDisplayName(name);
        if(Lore != null) meta.setLore(Lore);

        item.setItemMeta(meta);
        return item;
    }
}
