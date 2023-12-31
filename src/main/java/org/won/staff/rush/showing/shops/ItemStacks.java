package org.won.staff.rush.showing.shops;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.won.staff.rush.Rush;

import java.util.List;

public class ItemStacks {

    private static Rush main;
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

    public static void armorGiving(Player p, ItemStack item){
        if(item.getType().name().toLowerCase().contains("helmet")) p.getInventory().setHelmet(item);
        else if(item.getType().name().toLowerCase().contains("chestplate")) p.getInventory().setChestplate(item);
        else if(item.getType().name().toLowerCase().contains("legging")) p.getInventory().setLeggings(item);
        else if(item.getType().name().toLowerCase().contains("boots")) p.getInventory().setBoots(item);
        else p.getInventory().addItem(item);
    }

    public static void giveSpawnKit(Player p){
        int[] rgb = new int[]{255, 255, 255};
        if(main.jaune().contains(p)) {
            rgb[2] = 0;
            giveColoredArmor(p, rgb);
        }
        else if(main.violet().contains(p)) {
            rgb[1] = 0;
            giveColoredArmor(p, rgb);
        } else main.debug("ERROR : " + p.getName() + " isn't in a team !");

    }

    public static void giveColoredArmor(Player p, int[] rgb){
        Material[] lArmor = new Material[]{Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS};
        for(Material LeatherPiece : lArmor) {
            ItemStack piece = new ItemStack(LeatherPiece, 1);
            LeatherArmorMeta lch = (LeatherArmorMeta) piece.getItemMeta();
            lch.setColor(Color.fromRGB(rgb[0], rgb[1], rgb[2]));
            piece.setItemMeta(lch);
            armorGiving(p, piece);
        }
    }

}
