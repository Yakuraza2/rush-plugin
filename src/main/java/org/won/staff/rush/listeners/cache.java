package org.won.staff.rush.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.won.staff.rush.Rush;

import java.io.File;
import java.io.IOException;

public class cache implements Listener {

    private static Rush main;
    public cache(Rush main) {this.main = main;}

    public static void load(){
        System.out.println("Chargement du CACHE...");
        File file = main.getFile("rush");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(config.getConfigurationSection("rush") == null){
            System.out.println("Il n'y a aucune donnée à mettre en CACHE.");
            return;
        }

        if(config.getConfigurationSection("rush.lobby") != null){
            String key = "rush.lobby.";
            loadCache(key);

            main.setCache("rush.world", config.get("rush.world").toString());

            main.debug("CACHE LOBBY chargé !");
        }else{main.debug("rush.lobby doesn't exist !");}
        if(config.getConfigurationSection("rush.spect") != null){
            String key = "rush.spect.";
            loadCache(key);

        }else{main.debug("rush.spect doesn't exist !");}

        if(config.getConfigurationSection("rush.yellow") != null){
            String key = "rush.yellow.";
            loadCache(key);

        }else{main.debug("rush.yellow doesn't exist !");}

        if(config.getConfigurationSection("rush.purple") != null){
            String key = "rush.purple.";
            loadCache(key);

        }else{main.debug("rush.purple doesn't exist !");}

        if(config.get("rush.slots") != null){
            main.setCache("rush.slots", config.get("rush.slots").toString());
        }else{
            main.debug("rush.slots doesn't exist ! addind it to '2' into cache...");
            main.setCache("rush.slots", "2");
        }

        for(int i=0; i<16; i++){
            String key = "rush.spawners." + i + ".";
            if(config.get(key) != null){
                main.debug(key + " exists, adding it !");
                main.debug("Adding to cache");
                loadCache(key);
                main.debug("Adding to ArrayList");
                main.addSpawnerLoc(new Location(Bukkit.getWorld(main.takeCache("rush.world")), Double.parseDouble(main.takeCache(key + "x")), Double.parseDouble(main.takeCache(key + "y")), Double.parseDouble(main.takeCache(key + "z")), 0, 0));
            }else{
                main.debug(key + " doesn't exist !");
                break;
            }
        }

        for(int i=0; i<64; i++) {
            String key = "rush.zones." + i + ".";
            if (config.get(key) != null) {
                main.debug(key + " exists, adding it !");
                main.setCache(key+"x1", config.get(key+"x1").toString());
                main.setCache(key+"z1", config.get(key+"z1").toString());
                main.setCache(key+"x2", config.get(key+"x2").toString());
                main.setCache(key+"z2", config.get(key+"z2").toString());

            } else {
                main.debug(key + " doesn't exist !");
                break;
            }
        }

        System.out.println("Chargement du cache terminé !");
    }

    public static void save(){
        System.out.println("Sauvegarde du CACHE...");

        File file = main.getFile("rush");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        main.Cache().forEach((key, value) -> {
            main.debug("Sauvegarde de : " + key + " -> " + main.takeCache(key + ""));
            config.set(key + "", main.takeCache(key + ""));
        });

        try {
            config.save(file);
            System.out.println("Sauvegarde terminée !");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static World world(){
        World world = Bukkit.getWorld(main.takeCache("rush.world"));
        return world;
    }

    public static int slots(){
        return Integer.parseInt(main.takeCache("rush.slots"));
    }

    public static void loadCache(String key){
        File file = main.getFile("rush");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        main.setCache(key+"x", config.get(key+"x").toString());
        main.setCache(key+"y", config.get(key+"y").toString());
        main.setCache(key+"z", config.get(key+"z").toString());
        main.setCache(key+"yaw", config.get(key+"yaw").toString());
        main.setCache(key+"pitch", config.get(key+"pitch").toString());
    }
}
