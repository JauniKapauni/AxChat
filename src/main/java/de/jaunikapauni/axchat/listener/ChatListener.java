package de.jaunikapauni.axchat.listener;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {
    AxChat reference;
    public ChatListener(AxChat reference){
        this.reference = reference;
    }
    @EventHandler
    public void onChatMessage(PlayerChatEvent e){
        String msg = e.getPlayer().getName() + " : " + e.getMessage();
        reference.getChatManager().publishMessage("global_chat", msg);
        e.setCancelled(true);
    }
}
