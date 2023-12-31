package org.won.staff.rush;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.won.staff.rush.listeners.cache;
import org.won.staff.rush.shops.entities;

public class CommandRush implements CommandExecutor {

    private final Rush main;
    public CommandRush(Rush main) {this.main = main;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length==0){
            sender.sendMessage("Invalid Command");
            main.debug("Few arguments");
            return false;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage("Invalid Sender");
            main.debug("Invalid Sender");
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        Location Loc = player.getLocation();

        boolean coordinates = false;
        String key = "rush";

        if(args[0].equalsIgnoreCase("setlobby")){
            key = "rush.lobby.";
            main.setCache("rush.world", "" + Loc.getWorld().getName());
            main.debug("AJOUT LOBBY: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            player.sendMessage("Vous venez de définir le lobby en: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            coordinates = true;
        }

        if(args[0].equalsIgnoreCase("setspectator")){
            key = "rush.spect.";
            main.debug("AJOUT SPECTATOR SPAWN: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            player.sendMessage("Vous venez de définir le spawn des spectateurs en: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            coordinates = true;
        }

        if(args[0].equalsIgnoreCase("setyellow")){
            key = "rush.yellow.";
            main.debug("AJOUT YELLOW SPAWN: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            player.sendMessage(main.getConfigMessage("admin.yellow", player));
            coordinates = true;
        }

        if(args[0].equalsIgnoreCase("setpurple")){
            key = "rush.purple.";
            main.debug("AJOUT PURPLE SPAWN: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            player.sendMessage(main.getConfigMessage("admin.purple", player));
            coordinates = true;
        }

        if(args[0].equalsIgnoreCase("additemspawner")){
            int i = main.getSpawnersLocs().size();
            key = "rush.spawners." + i + ".";
            main.addSpawnerLoc(Loc);
            main.debug("AJOUT ITEM-SPAWNER SPAWN: " + Loc.getX() + "," + Loc.getY() + "," + Loc.getZ());
            player.sendMessage(main.getConfigMessage("admin.additemspawner", player));
            coordinates = true;
        }

        if(coordinates){
            main.setCache(key+"x", "" + Loc.getX());
            main.setCache(key+"y", "" + Loc.getY());
            main.setCache(key+"z", "" + Loc.getZ());
            main.setCache(key+"yaw", "" + Loc.getYaw());
            main.setCache(key+"pitch", "" + Loc.getPitch());
        }
        else if(args[0].equalsIgnoreCase("reload")){
            player.sendMessage("Loading the config...");
            cache.load();
        }else if(args[0].equalsIgnoreCase("setslots")){
            if(args.length !=2) player.sendMessage(ChatColor.RED + "Invalid arguments. Usage: /rush setslots <number>");
            if(!(args[1].charAt(0) > 0 && args[1].charAt(0) > 9)) player.sendMessage(ChatColor.RED + "Invalid arguments. Usage: /rush setslots <number>");
            player.sendMessage("setting slots number to: " + args[1]);
            main.setCache("rush.slots", args[1]);
        }else if(args[0].equalsIgnoreCase("spawnshop")){
            entities entity = new entities(main);

            entity.spawnCategories(player);
            player.sendMessage(main.getConfigMessage("admin.shop-creation", player));
        }



        cache.save();

        return false;
    }
}
