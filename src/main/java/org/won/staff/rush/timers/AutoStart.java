package org.won.staff.rush.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import org.won.staff.rush.listeners.cache;

public class AutoStart extends BukkitRunnable {

    private int timer = 20;
    private boolean waiting = false;
    private Rush main;
    public AutoStart(Rush main) {
        this.main = main;
    }

    @Override
    public void run(){
        if(main.getPlayers().size() < cache.slots()){
            if(timer > 5) timer=0;
            if(!main.isState(GState.WAITING)) main.setState(GState.WAITING);
            if(timer==0) Bukkit.broadcastMessage(ChatColor.GRAY + "Il n'y a pas assez de joueurs !");
            timer++;
            return;
        }
        if(main.isState(GState.WAITING)) {
            timer = 10;
            main.setState(GState.STARTING);
        }

        main.debug("compteur: " + timer + "s");
        for(Player joueurs : main.getPlayers()){
            joueurs.setLevel(timer);
        }
        if(timer>=60 && timer%60==0) Bukkit.broadcastMessage("Le jeu demarre dans " + ChatColor.YELLOW + timer/60 + ChatColor.GOLD+ " minutes !");
        else if(timer%5==0 || timer == 3 || timer == 2 || timer == 1){
            Bukkit.broadcastMessage("Le jeu demarre dans " + ChatColor.YELLOW + timer + ChatColor.GOLD+ " secondes !");
            for(Player joueurs : main.getPlayers()){
                joueurs.playSound(joueurs.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        }

        if(timer<=0){

            main.debug("Passage en PLAYING");
            main.setState(GState.PLAYING);

            main.debug("Lits mis sous le statut Alive=true");
            main.setYellowBedAlive(true);
            main.setPurpleBedAlive(true);

            char team = 'y';
            for(Player joueurs : main.getPlayers()){

                joueurs.sendMessage("Le jeu demarre !");
                joueurs.playSound(joueurs.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                if(team == 'y'){
                    main.debug(joueurs.getName() + " a rejoint l'équipe jaune !");
                    main.jaune().add(joueurs);
                    team = 'v';
                }else{
                    main.debug(joueurs.getName() + " a rejoint l'équipe violette !");
                    main.violet().add(joueurs);
                    team = 'y';
                }
                main.debug("Téléportation des joueurs...");
                main.spawnPlayer(joueurs);
            }
            cancel();
        }

        timer--;
    }
}
