package org.won.staff.rush.showing.shops;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.won.staff.rush.Rush;

public class entities {

    private Rush main;
    public entities(Rush main) {
        this.main = main;}

    public void spawnCategories(Player p){
        World world = p.getWorld();
        Location loc = p.getLocation();

        Villager categoriesEntity = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
        categoriesEntity.setProfession(Villager.Profession.ARMORER);
        categoriesEntity.setCustomNameVisible(true);
        categoriesEntity.setAI(false);
        categoriesEntity.setInvulnerable(true);
        categoriesEntity.addScoreboardTag("shops.categories");
        categoriesEntity.addScoreboardTag("rush");
        categoriesEntity.setCustomName((String) main.getConfig().get("shops.categories.entity-name"));
    }
}
