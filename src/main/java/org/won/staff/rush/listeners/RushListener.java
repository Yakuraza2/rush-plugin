package org.won.staff.rush.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.won.staff.rush.timers.AutoStart;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;

import java.io.File;

public class RushListener implements Listener {

    private Rush main;
    public RushListener(Rush main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        File file = main.getFile("rush");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "rush.";
        final ConfigurationSection configSection = config.getConfigurationSection(key);

        if(configSection == null){
            main.debug("Pas de jeu en config !");
            return;
        }

        if(!(main.isState(GState.WAITING) || main.isState(GState.STARTING)) || main.getPlayers().size() >= cache.slots()){
            main.debug("Le jeu n'est pas en WAITING");
            main.spawnSpectator(player);
            player.sendMessage(main.prefix() + main.getConfig().getString("spectator-join"));
            event.setJoinMessage(null);
            return;
        }
        main.debug("Le jeu est en WAITING");
        main.spawnPlayer(player);

        if(!main.getPlayers().contains(player)) main.getPlayers().add(player);
        event.setJoinMessage(main.getConfigMessage("join-message", player));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if((main.isState(GState.STARTING) || main.isState(GState.WAITING)) && main.getPlayers().contains(player)) main.getPlayers().remove(player);
        if( main.isState(GState.PLAYING) && main.getPlayers().contains(player)) PlayingListener.eliminatePlayer(player);
    }

}
