package org.won.staff.rush.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.won.staff.rush.timers.AutoFinish;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;

import static org.bukkit.Material.PURPLE_BED;
import static org.bukkit.Material.YELLOW_BED;

public class PlayingListener implements Listener {

    private static Rush main;
    public PlayingListener(Rush main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            main.debug("Damage détéctés sur " + player.getName());

            if(!main.isState(GState.PLAYING) && main.getPlayers().contains(player)){
                e.setCancelled(true);
                return;
            }

            if(player.getHealth()<=e.getDamage()){
                main.debug("Mort de " + player.getName() + " d'origine inconnue");
                e.setDamage(0);
                Bukkit.broadcastMessage(main.getConfigMessage("death", player));
                killPlayer(player);
            }
        }
    }
    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e){
        Entity damaged = e.getEntity();
        if(damaged instanceof Player) {
            Player victim = (Player) damaged;

            Entity damager = e.getDamager();
            Player killer = victim;

            main.debug("PVP détéctés sur " + victim.getName());

            if (victim.getHealth() <= e.getDamage()) {

                main.debug("Mort par PVP détéctés sur " + victim.getName());
                if (damager instanceof Player) {
                    killer = (Player) damager;
                }
                if (damager instanceof Arrow) {
                    Arrow arrow = (Arrow) damager;
                    if (arrow.getShooter() instanceof Player) {
                        killer = ((Player) arrow.getShooter());
                    }
                }
                if(!(main.isState(GState.PLAYING) && main.getPlayers().contains(killer))){
                    e.setCancelled(true);
                    killer.sendMessage(main.getConfigMessage("no-pvp", killer));
                    return;
                }

                main.debug("Le tueur de " + victim.getName() + " est : " + killer.getName());

                killer.sendMessage(main.getConfigMessage("killer-message", victim));
                killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2, 1);
                Bukkit.broadcastMessage(main.getConfigMessage("killed-by-player", victim).replaceAll("<killer>", killer.getName()));
                e.setDamage(0);
                killPlayer(victim);
            }
        }


    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();

        if(!(main.isState(GState.PLAYING) && main.getPlayers().contains(player)) && !player.hasPermission("rush.admin")) {
            player.sendMessage(main.getConfigMessage("no-break", player));
            e.setCancelled(true);
            return;
        }
        if(e.getBlock().getBlockData().getMaterial() == YELLOW_BED){
            if(main.jaune().contains(player)){
                e.setCancelled(true);
                player.sendMessage(main.getConfigMessage("ally-bed-destroy", player));
                return;
            }else {
                main.setYellowBedAlive(false);
                main.debug(player.getName() + " a détruit le lit JAUNE");
                Bukkit.broadcastMessage(main.getConfigMessage("bed-destroy", player).replaceAll("<bed>",   ChatColor.YELLOW + "jaune"));
            }
        }else if(e.getBlock().getBlockData().getMaterial() == PURPLE_BED){
            if(main.violet().contains(player)){
                e.setCancelled(true);
                player.sendMessage(main.getConfigMessage("ally-bed-destroy", player));
                return;
            } else {
                main.setPurpleBedAlive(false);
                main.debug(player.getName() + " a détruit le lit VIOLET");
                Bukkit.broadcastMessage(main.getConfigMessage("bed-destroy", player).replaceAll("<bed>",   ChatColor.LIGHT_PURPLE + "violet"));
            }
        }else{return;}

        for(Player joueurs : main.getPlayers()){
            joueurs.playSound(player, Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 1, 1);
        }
    }

    //CREER UN KILLPLAYER -> Si le lit est détruit : eliminer le joueur, sinon, le faire respawn + clear + heal etc.
    public static void killPlayer(Player player){
        main.debug("Le joueur " + player.getName() + " est mort.");

        if(main.jaune().contains(player)){
            if(main.isYellowBedAlive()){
                main.debug("L'équipe jaune a encore son lit, " + player.getName() + " peut respawn !");
                main.spawnPlayer(player);
            }else{
                main.debug("L'équipe jaune n'a plus son lit, " + player.getName() + " est éliminé !");
                eliminatePlayer(player);
            }
        }else if(main.violet().contains(player)){
            if(main.isPurpleBedAlive()){
                main.debug("L'équipe violette a encore son lit, " + player.getName() + " peut respawn !");
                main.spawnPlayer(player);
            } else {
                main.debug("L'équipe violette n'a plus son lit, " + player.getName() + " est éliminé !");
                eliminatePlayer(player);
            }
        }else{
            main.debug(player.getName() + " est dans aucune équipe, il respawn en spectateur !");
            main.spawnSpectator(player);
        }
    }
    public static void eliminatePlayer(Player player){
        if(main.getPlayers().contains(player)) main.getPlayers().remove(player);
        if(main.jaune().contains(player)) main.jaune().remove(player);
        if(main.violet().contains(player)) main.violet().remove(player);

        main.spawnSpectator(player);

        checkWin();
    }

    private static void checkWin() {
        AutoFinish finish = new AutoFinish(main);

        if(main.getPlayers().size()>2) return;
        else if(main.getPlayers().size()==1){
            Player winner = main.getPlayers().get(0);

            if(main.jaune().contains(winner)){
                Bukkit.broadcastMessage(main.getConfigMessage("winning-solo-broadcast", winner).replaceAll("<team>", main.getConfigMessage("yellow-team",null)));
            }else{
                Bukkit.broadcastMessage(main.getConfigMessage("winning-solo-broadcast", winner).replaceAll("<team>", main.getConfigMessage("yellow-team",null)));
            }
        }

        else if(main.getPlayers().size()==2){
            Player winner = main.getPlayers().get(0);
            Player winner2 = main.getPlayers().get(1);

            if(main.jaune().contains(winner) && main.jaune().contains(winner2)){
                Bukkit.broadcastMessage(main.getConfigMessage("winning-broadcast", winner).replaceAll("<team>", main.getConfigMessage("yellow-team",null)));
            }else if(main.violet().contains(winner) && main.violet().contains(winner2)){
                Bukkit.broadcastMessage(main.getConfigMessage("winning-broadcast", winner).replaceAll("<team>", main.getConfigMessage("yellow-team",null)));
            }else {return;}

        }else{
            System.out.println("Il semble n'y avoir aucun gagnant !");

            Bukkit.broadcastMessage(main.getConfigMessage("no-winner", null));
        }

        finish.runTaskTimer(main, 0, 20);

    }
}
