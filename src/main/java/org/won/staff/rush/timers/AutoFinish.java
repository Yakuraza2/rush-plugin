package org.won.staff.rush.timers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;

public class AutoFinish extends BukkitRunnable {

    private static int timer = 0;

    private Rush main;
    public AutoFinish(Rush main) {
        this.main = main;
    }

    @Override
    public void run() {
        int alives = main.getPlayers().size();
        Player winner = null;
        Player winner2 = null;

        if(alives > 1) winner2 = main.getPlayers().get(1);
        if(alives >= 1) winner = main.getPlayers().get(0);

        if(main.isState(GState.PLAYING)){
            main.setState(GState.FINISH);
            timer = (int) main.getConfig().get("timers.finishing");

            if(winner2 != null) winner2.sendMessage(main.getConfigMessage("winners-message", winner2));
            if(winner != null) winner.sendMessage(main.getConfigMessage("winners-message", winner));

        }

        if(timer==(int)main.getConfig().get("timers.finishing")-5){
            if(winner2 != null)winner2.setGameMode(GameMode.SPECTATOR);
            if(winner != null) winner.setGameMode(GameMode.SPECTATOR);

            Bukkit.broadcastMessage(main.getConfigMessage("last-seconds", null));
        }

        if(timer <=0){
            main.getServer().spigot().restart();
            cancel();
        }
        timer--;
    }

    public static int getTime(){
        return timer;
    }
}
