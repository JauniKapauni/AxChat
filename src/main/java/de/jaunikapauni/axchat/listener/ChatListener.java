package de.jaunikapauni.axchat.listener;

import de.jaunikapauni.axchat.AxChat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
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
        Long timestamp = reference.getChatManager().getLastMessageTime().get(e.getPlayer().getUniqueId());
        int cooldown = reference.getConfig().getInt("chat_cooldown");
        if(timestamp != null){
            long elapsed = System.currentTimeMillis() - timestamp;
            if(elapsed < cooldown){
                Long remainingMillis = cooldown - elapsed;
                Long seconds = remainingMillis / 1000;
                Long displaySeconds = seconds + 1;
                e.getPlayer().sendMessage("Please wait another " + displaySeconds + " seconds before sending a new message!");
                e.setCancelled(true);
                return;
            }
        }
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
            p.sendMessage("Your message was blocked!");
            e.setCancelled(true);
        } else {
            String prefix = reference.getMessage("chat.prefix");
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);

            String separator = reference.getMessage("chat.separator");
            separator = ChatColor.translateAlternateColorCodes('&', separator);

            String suffix = reference.getMessage("chat.suffix");
            suffix = ChatColor.translateAlternateColorCodes('&', suffix);

            String formattedMessage = prefix.replace("player", p.getName()) + " " + separator + " " + suffix.replace("message", message);
            formattedMessage = PlaceholderAPI.setPlaceholders(p, formattedMessage);
            reference.getChatManager().publishMessage("global_chat", formattedMessage);
            reference.getChatManager().getLastMessageTime().put(p.getUniqueId(), System.currentTimeMillis());
            e.setCancelled(true);
        }
    }
}
