package me.lojosho.aichatbot;

import io.paradaux.ai.MarkovMegaHal;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public final class main extends JavaPlugin implements Listener {

    private Logger log;
    public MarkovMegaHal hal = new MarkovMegaHal();
    public Random rand = new Random();
    public ArrayList worldlist = new ArrayList<String>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        for (String line : getConfig().getStringList("Strings")) {
            hal.add(line);
        }
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String fluffCheck = event.getMessage().toLowerCase();
                if (fluffCheck.contains("fluffbot")) {
                    worldlist.add(splitSentenceByWords(fluffCheck));
                    getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&bFluff Bot &8» &r" + hal.getSentence(worldlist.get(rand.nextInt(worldlist.size())).toString())));
                    worldlist.clear();
                } else {
                    List<String> List = getConfig().getStringList("Strings");
                    String message = event.getMessage();
                    hal.add(message);
                    List.add(message);
                    getConfig().set("Strings", List);
                    saveConfig();
                }
            }
        };
        task.runTaskLater(this, 2);
    }

    private static String[] splitSentenceByWords(String str){

        //if string is empty or null, return empty array
        if(str == null || str.equals(""))
            return new String[0];

        String[] words = str.split(" ");

        return words;
    }
}
