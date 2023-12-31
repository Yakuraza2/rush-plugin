package org.won.staff.rush.timers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;

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
            spawnItem(Material.ACACIA_WOOD);
        }
        if(timer%(int)main.getConfig().get("timers.iron") == 0){
            spawnItem(Material.IRON_INGOT);
        }
        if(timer%(int)main.getConfig().get("timers.gold") == 0){
            spawnItem(Material.GOLD_INGOT);
        }
        if(timer%(int)main.getConfig().get("timers.diamond") == 0){
            spawnItem(Material.DIAMOND);
        }

        timer++;
    }

    private void spawnItem(Material item){
        for(Object loc : main.getSpawnersLocs()){
            Location Loc = (Location)loc;
            Loc.getWorld().dropItem(Loc, new ItemStack(item));
        }
    }
}
