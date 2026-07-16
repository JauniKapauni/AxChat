package de.jaunikapauni.axchat.manager;

import de.jaunikapauni.axchat.AxChat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private String host;
    private int port;
    private String username;
    private String password;
    AxChat reference;

    Jedis publisher;
    Map<UUID, Long> lastMessageTime = new ConcurrentHashMap<>();

    public ChatManager(String host, int port, String username, String password, AxChat reference) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        publisher = new Jedis(host, port);
        publisher.auth(username, password);
        this.reference = reference;
    }

    public void publishMessage(String channel, String message) {
        publisher.publish(channel, message);
    }

    public void subscribe(String channel) {
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            try (Jedis subscriber = new Jedis(host, port)) {
                subscriber.auth(username, password);
                subscriber.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        Bukkit.getScheduler().runTask(reference, () -> {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                String parsed = ChatColor.translateAlternateColorCodes('&', message);
                                p.sendMessage(parsed);
                            }
                        });
                    }
                }, channel);
            }
        });
    }

    public void subscribePrivateMessages() {
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            try (Jedis subscriber = new Jedis(host, port)) {
                subscriber.auth(username, password);
                subscriber.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        String[] messageParts = message.split(";", 3);
                        if (messageParts.length < 3) {
                            return;
                        }
                        String sourcePlayer = messageParts[0];
                        String targetName = messageParts[1];
                        String msg = messageParts[2];
                        Bukkit.getScheduler().runTask(reference, () -> {
                            Player targetPlayer = Bukkit.getPlayerExact(targetName);
                            if (targetPlayer != null) {
                                targetPlayer.sendMessage(sourcePlayer + " - " + targetPlayer.getName() + " : " + msg);
                            }
                        });
                    }
                }, "private_messages");
            }
        });
    }

    public Map<UUID, Long> getLastMessageTime() {
        return lastMessageTime;
    }

    public void close() {
        if (publisher != null) {
            publisher.close();
        }
    }
}
