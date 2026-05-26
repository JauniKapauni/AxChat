package de.jaunikapauni.axchat.listener;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.List;

public class ChatListener implements Listener {
    AxChat reference;
    public ChatListener(AxChat reference){
        this.reference = reference;
    }
    @EventHandler
    public void onChatMessage(PlayerChatEvent e){
        List<String> forbiddenWords = reference.getConfig().getStringList("forbidden-words");
        String message = e.getMessage();
        Player p = e.getPlayer();
        boolean containsForbidden = false;
        for(String w : forbiddenWords){
            if(message.toLowerCase().contains(w)){
                containsForbidden = true;
                break;
            }
        }
        if(containsForbidden){
            p.sendMessage("Your message was blocekd!");
            e.setCancelled(true);
        } else {
            String formatPlayer = reference.getMessage("chat.prefix");
            String formatSeparator = reference.getMessage("chat.separator");
            String formatMessage = reference.getMessage("chat.suffix");
            String formattedMessage = formatPlayer.replace("player", p.getName() + " " + formatSeparator + " " + formatMessage.replace("message", message));
            reference.getChatManager().publishMessage("global_chat", formattedMessage);
            e.setCancelled(true);
        }
    }
}
