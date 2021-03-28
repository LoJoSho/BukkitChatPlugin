package me.lojosho.aichatbot;

import io.paradaux.ai.MarkovMegaHal;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;

public final class main extends JavaPlugin implements Listener {

    public MarkovMegaHal hal = new MarkovMegaHal();

    @Override
    public void onEnable() {
        // Plugin startup logic
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
                    getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&bFluff Bot &8» &r" + hal.getSentence()));
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
}
