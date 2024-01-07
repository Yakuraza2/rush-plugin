package org.won.staff.rush.zones;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.won.staff.rush.Rush;

import java.util.HashMap;

public class effects {

    private Rush main;

    public effects(Rush main) {this.main = main;}

    HashMap<Character, Integer> HealBoost = new HashMap<>();

    public int getHealBoost(char a){
        if(HealBoost.get(a) != null) return HealBoost.get(a);
        else return 0;
    }

    public void addHealBoost(char a, int b){
        HealBoost.put(a, getHealBoost(a) + b);
        putHealBoost();
    }

    public void putHealBoost(){
        for(Player p : main.getPlayers()){
            putHealBoost(p);
        }
    }

    public void putHealBoost(Player p){
        if(main.jaune().contains(p)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999, HealBoost.get('y'), false, false, true));
        }else if(main.violet().contains(p)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999, HealBoost.get('p'), false, false, true));
        }
    }
}
