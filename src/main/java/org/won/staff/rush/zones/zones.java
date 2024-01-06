package org.won.staff.rush.zones;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.won.staff.rush.Rush;

public class zones {

    private Rush main;
    public zones(Rush main) {this.main = main;}

    public boolean isPlayerInZone(Player p){
        Location loc = p.getLocation();

        for(int i=0;i<64;i++){
            if(main.takeCache("rush.zones." + i + ".x1") != null){
                int x1 = Integer.parseInt(main.takeCache("rush.zones." + i + ".x1"));
                int z1 = Integer.parseInt(main.takeCache("rush.zones." + i + ".z1"));
                int x2 = Integer.parseInt(main.takeCache("rush.zones." + i + ".x2"));
                int z2 = Integer.parseInt(main.takeCache("rush.zones." + i + ".z2"));

                if(loc.getX() > x1 && loc.getX() < x2){
                    if(loc.getZ() > z1 && loc.getZ() < z2){
                        return true;
                    }
                }


            }else return false;
        }
        return false;
    }
}
