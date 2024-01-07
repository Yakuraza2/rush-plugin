package org.won.staff.rush;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.won.staff.rush.listeners.PlayingListener;
import org.won.staff.rush.listeners.RushListener;
import org.won.staff.rush.listeners.cache;
import org.won.staff.rush.showing.shops.InventoriesListener;
import org.won.staff.rush.timers.AutoFinish;
import org.won.staff.rush.timers.AutoStart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rush extends JavaPlugin {

    private GState state;

    private List<Player> players = new ArrayList<>();

    private HashMap<String, String> cacheHM = new HashMap<>();
    private List<Player> violet = new ArrayList<>();
    private List<Player> jaune = new ArrayList<>();

    private List<Location> itemSpawnersLoc = new ArrayList<>();
    private HashMap<String, Integer> zonesLoc = new HashMap<>();

    private boolean purpleBed = false;
    private boolean yellowBed = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        AutoStart start = new AutoStart(this);

        debug("JEU MIS PAR DEFAUT EN: WAITING");
        setState(GState.WAITING);
        createFile("rush");

        PluginManager pm = getServer().getPluginManager();
        debug("Lancement du Listener: Rush");
        pm.registerEvents(new RushListener(this), this);
        debug("Lancement du Listener: Playing");
        pm.registerEvents(new PlayingListener(this), this);
        debug("Lancement du Listener: Inventories");
        pm.registerEvents(new InventoriesListener(this), this);
        debug("Lancement du Listener: Cache");
        pm.registerEvents(new cache(this), this);

        debug("Initialisation des commandes");
        getCommand("rush").setExecutor(new CommandRush(this));

        System.out.println("Initialisation du cache...");
        cache.load();

        start.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        System.out.println("Sauvegarde du cache...");
        cache.save();
    }

    public void setState(GState state){
        this.state = state;
    }

    public boolean isState(GState state){
        return this.state == state;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void debug(String debug){
        System.out.println("[DEBUG] " + debug);
    }

    private void createFile(String fileName){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
            System.out.println("[FILES] Le dossier " + getDataFolder().getName() + " n'existe pas.");
            System.out.println("[FILES] Création du dossier " + getDataFolder().getName());
        }else{debug("Le dossier 'rush' existe");}

        File file = new File(getDataFolder(), fileName + ".yml");

        if(!file.exists()){
            System.out.println("[FILES] Le fichier " + fileName + ".yml n'existe pas.");
            try {
                file.createNewFile();
                System.out.println("[FILES] Création du fichier " + fileName + ".yml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{debug("Le fichier 'rush' existe");}
    }

    public File getFile(String fileName){return new File(getDataFolder(), fileName + ".yml");}

    public void setCache(String id, String value){
        cacheHM.put(id, value);
        debug("Donnée en cache changée: " + id + " to " + value);
    }

    public String takeCache(String id){
        return cacheHM.get(id);
    }

    public HashMap Cache(){
        return cacheHM;
    }

    public Location spawnPoint(String key){
        Location lobby = new Location(cache.world(), Double.parseDouble(takeCache(key + "x")), Double.parseDouble(takeCache(key + "y")), Double.parseDouble(takeCache(key + "z")) , Float.parseFloat(takeCache(key + "yaw")), Float.parseFloat(takeCache(key + "pitch")));
        return lobby;
    }

    public List<Player> jaune(){
        return jaune;
    }
    public List<Player> violet(){
        return violet;
    }

    public boolean isPurpleBedAlive(){
        return purpleBed;
    }

    public boolean isYellowBedAlive(){
        return yellowBed;
    }

    public void setPurpleBedAlive(boolean value){
        purpleBed = value;
    }
    public void setYellowBedAlive(boolean value){
        yellowBed = value;
    }

    public void addSpawnerLoc(Location loc){
        itemSpawnersLoc.add(loc);
        debug("Ajout de : " + loc.getX() + " , " + loc.getY() + " , " + loc.getZ() + " aux items-spawners.");
    }

    public List getSpawnersLocs(){
        return itemSpawnersLoc;
    }

    public String prefix(){
        return this.getConfig().getString("messages.prefix") + " ";
    }

    public String getConfigMessage(String path, Player player){
        String message = "";

        if(path.equalsIgnoreCase("yellow-team") || path.equalsIgnoreCase("purple-team")){
            message = getConfig().getString("messages." + path);
        }else{message = prefix() + getConfig().getString("messages." + path);}

        if(player != null) message = message.replaceAll("<player>", player.getName());
        message = message.replaceAll("<time>", AutoStart.getTime() + AutoFinish.getTime() + "");
        message = message.replaceAll("<slots>", cache.slots() + "");
        message = message.replaceAll("<onlines>", getPlayers().size() + "");
        return message;
    }
    public HashMap getZoneLoc() { return zonesLoc;}
}
