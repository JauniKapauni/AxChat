package de.jaunikapauni.axchat.listener;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    AxChat reference;
    public PlayerQuitListener(AxChat reference){
        this.reference = reference;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.quitMessage(null);
        boolean quitMessageEnabled = reference.getConfig().getBoolean("quit_message_enabled");
        if(quitMessageEnabled){
            String quitMessage = reference.getConfig().getString("quit_message");
            Player p = e.getPlayer();
            Bukkit.broadcastMessage(ChatColor.RED + p.getName() + quitMessage);
        }
    }
}
