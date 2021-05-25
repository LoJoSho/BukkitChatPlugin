package me.lojosho.aichatbot;

import io.paradaux.ai.MarkovMegaHal;
import me.lojosho.aichatbot.database.SQLite;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {

    private Logger log;
    public MarkovMegaHal hal = new MarkovMegaHal();
    public Random rand = new Random();
    public ArrayList<String> wordlist = SQLite.checkMessages(getDataFolder());

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        new SQLite(this);
        SQLite.DBConnect(getDataFolder());
        SQLite.DBSend("CREATE TABLE IF NOT EXISTS MESSAGEDB " +
                        "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                        "MESSAGE TEXT NOT NULL);"
                , this.getDataFolder());
        for (String line : wordlist) {
            hal.add(line);
        }
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String fluffCheck = event.getMessage();
                // sets up a temp variable that has the string which doesn't have a '. This is so the sql doesn't complain when it saves to db
                String a = fluffCheck.replaceAll("'", " ");
                if (event.getMessage().toLowerCase().contains("fluffbot") && event.getPlayer().hasPermission("ChatBot.Always")) {
                    //worldlist.add(splitSentenceByWords(fluffCheck));
                    getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&bFluff Bot &8» &r" + hal.getSentence(event.getMessage())));
                    //worldlist.clear();
                } else {
                    if (rand.nextInt(200) == 1) {
                        getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&bFluff Bot &8» &r" + hal.getSentence(event.getMessage())));
                    }
                    hal.add(event.getMessage());
                    SQLite.DBSend("INSERT INTO MESSAGEDB (MESSAGE) VALUES ('" + a + "');", getDataFolder());
                    /* List<String> List = getConfig().getStringList("Strings");
                    }
                    String message = event.getMessage();
                    hal.add(message);
                    List.add(message);
                    getConfig().set("Strings", List);
                    saveConfig();*/
                }
            }
        };
        task.runTaskLaterAsynchronously(this, 1);
    }

/*    private static String[] splitSentenceByWords(String str){

        //if string is empty or null, return empty array
        if(str == null || str.equals(""))
            return new String[0];

        String[] words = str.split(" ");

        return words;
    } */
}
