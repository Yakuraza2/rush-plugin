package org.won.staff.rush.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import org.won.staff.rush.listeners.PlayingListener;
import org.won.staff.rush.listeners.cache;
import org.won.staff.rush.showing.tablist;

public class AutoStart extends BukkitRunnable {

    private static int timer = 0;
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
            if(main.getPlayers().size() < 1)return;
            if(timer==0) Bukkit.broadcastMessage(main.getConfigMessage("slots-not-full", null));
            timer++;
            return;
        }
        if(main.isState(GState.WAITING)) {
            timer = (int) main.getConfig().get("timers.starting");
            main.setState(GState.STARTING);
        }

        main.debug("compteur: " + timer + "s");
        for(Player joueurs : main.getPlayers()){
            joueurs.setLevel(timer);
        }
        if(timer>=60 && timer%60==0) Bukkit.broadcastMessage(main.prefix() + "Le jeu demarre dans " + ChatColor.YELLOW + timer/60 + ChatColor.GOLD+ " minutes !");
        else if(timer%5==0 || timer == 3 || timer == 2 || timer == 1){
            Bukkit.broadcastMessage(main.getConfigMessage("countdown", null));
            for(Player joueurs : main.getPlayers()){
                joueurs.playSound(joueurs.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        }

        if(timer<=0){
            PlayingTimer spawners = new PlayingTimer(main);

            main.debug("Passage en PLAYING");
            main.setState(GState.PLAYING);

            main.debug("Lits mis sous le statut Alive=true");
            main.setYellowBedAlive(true);
            main.setPurpleBedAlive(true);

            main.debug("Lancement des spawners à items");
            spawners.runTaskTimer(main,0,20);


            char team = 'y';
            for(Player joueurs : main.getPlayers()){

                joueurs.sendMessage(main.getConfigMessage("starting", joueurs));
                joueurs.playSound(joueurs.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                tablist tabList = new tablist(main);

                if(team == 'y'){
                    main.debug(joueurs.getName() + " a rejoint l'équipe jaune !");
                    main.jaune().add(joueurs);
                    tabList.setplayer(joueurs, team);
                    team = 'p';

                }else{
                    main.debug(joueurs.getName() + " a rejoint l'équipe violette !");
                    main.violet().add(joueurs);
                    tabList.setplayer(joueurs, team);
                    team = 'y';
                }
                main.debug("Téléportation des joueurs...");
                PlayingListener.spawnPlayer(joueurs);
            }
            cancel();
        }

        timer--;
    }

    public static int getTime(){
        return timer;
    }
}
