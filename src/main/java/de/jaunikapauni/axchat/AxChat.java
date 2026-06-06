package de.jaunikapauni.axchat;

import de.jaunikapauni.axchat.command.MessageCommand;
import de.jaunikapauni.axchat.command.MessageTabCompleter;
import de.jaunikapauni.axchat.command.ReloadCommand;
import de.jaunikapauni.axchat.listener.ChatListener;
import de.jaunikapauni.axchat.listener.PlayerJoinListener;
import de.jaunikapauni.axchat.listener.PlayerQuitListener;
import de.jaunikapauni.axchat.manager.ChatManager;
import de.jaunikapauni.axchat.manager.DatabaseManager;
import de.jaunikapauni.axchat.manager.PlayerManager;
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
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    PlayerManager playerManager;
    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try{
            databaseManager = new DatabaseManager(this);
            playerManager = new PlayerManager(this);
            if(databaseManager.initDatabaseTable1() == false){
                getLogger().severe("Failed to create db table");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        saveDefaultConfig();
        createLangFile();
        host = getConfig().getString("redis.host");
        port = getConfig().getInt("redis.port");
        chatManager = new ChatManager(host, port);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        chatManager.subscribe("global_chat");
        chatManager.subscribePrivateMessages();
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("msg").setExecutor(new MessageCommand(this));
        getCommand("msg").setTabCompleter(new MessageTabCompleter(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
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
