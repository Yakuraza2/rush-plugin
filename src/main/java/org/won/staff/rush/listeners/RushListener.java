package org.won.staff.rush.listeners;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.won.staff.rush.showing.chat;
import org.won.staff.rush.timers.AutoStart;
import org.won.staff.rush.GState;
import org.won.staff.rush.Rush;
import org.won.staff.rush.zones.zones;

import java.io.File;

import static org.bukkit.Material.YELLOW_BED;

public class RushListener implements Listener {

    private Rush main;

    public RushListener(Rush main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        File file = main.getFile("rush");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = "rush.";
        final ConfigurationSection configSection = config.getConfigurationSection(key);

        if (configSection == null) {
            main.debug("Pas de jeu en config !");
            return;
        }

        if (!(main.isState(GState.WAITING) || main.isState(GState.STARTING)) || main.getPlayers().size() >= cache.slots()) {
            main.debug("Le jeu n'est pas en WAITING");
            PlayingListener.spawnSpectator(player);
            player.sendMessage(main.prefix() + main.getConfig().getString("spectator-join"));
            event.setJoinMessage(null);
            return;
        }
        main.debug("Le jeu est en WAITING");
        PlayingListener.spawnPlayer(player);

        if (!main.getPlayers().contains(player)) main.getPlayers().add(player);
        event.setJoinMessage(main.getConfigMessage("join-message", player));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if ((main.isState(GState.STARTING) || main.isState(GState.WAITING)) && main.getPlayers().contains(player))
            main.getPlayers().remove(player);
        if (main.isState(GState.PLAYING) && main.getPlayers().contains(player)) PlayingListener.eliminatePlayer(player);
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
            PlayingListener.YellowBedBreak(player, e);

            for(Player joueurs : main.getPlayers()){
                joueurs.playSound(player, Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 1, 1);
            }
        }

        if(e.getBlock().getBlockData().getMaterial() == Material.PURPLE_BED){
            PlayingListener.PurpleBedBreak(player, e);

            for(Player joueurs : main.getPlayers()){
                joueurs.playSound(player, Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 1, 1);
            }
        }

        if(e.getBlock().getBlockData().getMaterial() == Material.BROWN_BED){
            PlayingListener.BrownBedBreak(player, e);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String msg = e.getMessage();

        chat c = new chat(main);

        if(main.isState(GState.WAITING) || main.isState(GState.STARTING)) return;
        else{
            if(msg.startsWith("@")){
                c.sendMessage(p, msg);
            }else{
                c.sendTeamMessage(p, msg);
            }
        }
    }

}
