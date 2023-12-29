package org.won.staff.rush.timers;

import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.Rush;

public class AutoFinish extends BukkitRunnable {

    private int timer = 15;

    private Rush main;
    public AutoFinish(Rush main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(timer <=0){
            main.getServer().spigot().restart();
        }
        timer--;
    }
}
