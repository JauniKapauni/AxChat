package de.jaunikapauni.axchat;

import de.jaunikapauni.axchat.command.MessageCommand;
import de.jaunikapauni.axchat.command.ReloadCommand;
import de.jaunikapauni.axchat.listener.ChatListener;
import de.jaunikapauni.axchat.listener.PlayerJoinListener;
import de.jaunikapauni.axchat.listener.PlayerQuitListener;
import de.jaunikapauni.axchat.manager.ChatManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class AxChat extends JavaPlugin {
    private File langFile;
    private FileConfiguration langConfig;
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
        createLangFile();
        host = getConfig().getString("redis.host");
        port = getConfig().getInt("redis.port");
        chatManager = new ChatManager(host, port);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        chatManager.subscribe("global_chat");
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("msg").setExecutor(new MessageCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createLangFile(){
        langFile = new File(getDataFolder(), "lang.yml");
        if(!langFile.exists()){
            saveResource("lang.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public String getMessage(String path){
        return langConfig.getString(path);
    }
}
