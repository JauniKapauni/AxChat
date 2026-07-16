package de.jaunikapauni.axchat;

import de.jaunikapauni.axchat.command.MailCommand;
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
    private String username;
    private String password;
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
        saveDefaultConfig();
        createLangFile();
        try{
            databaseManager = new DatabaseManager(this);
            playerManager = new PlayerManager(this);
            if(!databaseManager.initDatabaseTable1() || !databaseManager.initDatabaseTable2()){
                getLogger().severe("Failed to create db table");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        host = getConfig().getString("redis.host");
        port = getConfig().getInt("redis.port");
        username = getConfig().getString("redis.username");
        password = getConfig().getString("redis.password");
        chatManager = new ChatManager(host, port, username, password, this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        chatManager.subscribe("global_chat");
        chatManager.subscribePrivateMessages();
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("msg").setExecutor(new MessageCommand(this));
        getCommand("msg").setTabCompleter(new MessageTabCompleter(this));
        getCommand("mail").setExecutor(new MailCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getLogger().info("");
        getLogger().info("----------------------------------------");
        getLogger().info("Name: " + getName());
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info(String.join("Authors: " + ", ", getDescription().getAuthors()));
        getLogger().info("----------------------------------------");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
        chatManager.close();
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
