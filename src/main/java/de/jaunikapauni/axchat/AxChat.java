package de.jaunikapauni.axchat;

import de.jaunikapauni.axchat.listener.ChatListener;
import de.jaunikapauni.axchat.manager.ChatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxChat extends JavaPlugin {
    ChatManager chatManager;
    public ChatManager getChatManager(){
        return chatManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        String host = getConfig().getString("redis.host");
        int port = getConfig().getInt("redis.port");
        chatManager = new ChatManager(host, port);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        chatManager.subscribe("global_chat");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
