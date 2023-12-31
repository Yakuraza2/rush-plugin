package org.won.staff.rush.timers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import org.won.staff.rush.shops.ItemStacks;

public class itemSpawners extends BukkitRunnable {

    private final Rush main;
    public itemSpawners(Rush main) {
        this.main = main;
    }

    private int timer = 1;

    @Override
    public void run() {
        if(!main.isState(GState.PLAYING)) cancel();

        if(timer%(int)main.getConfig().get("timers.bronze") == 0){
            spawnItem(Material.COPPER_INGOT, "bronze");
        }
        if(timer%(int)main.getConfig().get("timers.iron") == 0){
            spawnItem(Material.IRON_INGOT, "iron");
        }
        if(timer%(int)main.getConfig().get("timers.gold") == 0){
            spawnItem(Material.GOLD_INGOT, "gold");
        }
        if(timer%(int)main.getConfig().get("timers.diamond") == 0){
            spawnItem(Material.DIAMOND, "diamond");
        }

        timer++;
    }

    private void spawnItem(Material item, String name){
        for(Object loc : main.getSpawnersLocs()){
            Location Loc = (Location)loc;
            Loc.getWorld().dropItem(Loc, ItemStacks.create(item, main.getConfig().getString("shops.display-names." + name), 1));
        }
    }
}
