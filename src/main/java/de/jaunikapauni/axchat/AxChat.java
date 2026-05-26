package de.jaunikapauni.axchat;

import de.jaunikapauni.axchat.command.ReloadCommand;
import de.jaunikapauni.axchat.listener.ChatListener;
import de.jaunikapauni.axchat.manager.ChatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxChat extends JavaPlugin {
    private String host;
    private int port;
    private ChatManager chatManager;
    public ChatManager getChatManager(){
        return chatManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        host = getConfig().getString("redis.host");
        port = getConfig().getInt("redis.port");
        chatManager = new ChatManager(host, port);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        chatManager.subscribe("global_chat");
        getCommand("reload").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
