package org.won.staff.rush.timers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import org.won.staff.rush.listeners.PlayingListener;
import org.won.staff.rush.shops.ItemStacks;
import org.won.staff.rush.zones.zones;

import java.util.HashMap;

public class PlayingTimer extends BukkitRunnable {

    private final Rush main;
    public PlayingTimer(Rush main) {
        this.main = main;
    }

    private int timer = 1;

    HashMap<Player, Integer> playerTimer = new HashMap<>();

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

        if(timer%main.getConfig().getInt("timers.player-zone-verif")==0){
            zones Zones = new zones(main);
            for(Player p : main.getPlayers()){
                if(Zones.isPlayerInZone(p)){
                    if(playerTimer.containsKey(p) && playerTimer.get(p) > 0){
                        playerTimer.put(p, 0);
                    }
                }else{

                    if(!playerTimer.containsKey(p)){
                        playerTimer.put(p, 1);
                    }else if(playerTimer.get(p) < 5){
                        playerTimer.put(p, playerTimer.get(p) + 1);
                        p.sendMessage("§cRevenez dans la zone de jeu !");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, 1);
                    }else{
                        PlayingListener.killPlayer(p);
                        playerTimer.put(p, 0);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.5F, 1);
                    }

                }
            }
        }

        timer++;
    }

    private void spawnItem(Material material, String name){
        for(Object loc : main.getSpawnersLocs()){
            Location Loc = (Location)loc;
            Loc.getWorld().dropItem(Loc, ItemStacks.create(material, main.getConfig().getString("shops.display-names." + name), 1));
        }
    }
}
