package de.jaunikapauni.axchat.listener;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    AxChat reference;
    public PlayerJoinListener(AxChat reference){
        this.reference = reference;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.joinMessage(null);
        boolean joinMessageEnabled = reference.getConfig().getBoolean("join_message_enabled");
        if(joinMessageEnabled){
            String joinMessage = reference.getConfig().getString("join_message");
            Player p = e.getPlayer();
            Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + joinMessage);
        }
        reference.getPlayerManager().updatePlayerStatus(e.getPlayer().getUniqueId(), e.getPlayer().getName(), true);
    }
}
