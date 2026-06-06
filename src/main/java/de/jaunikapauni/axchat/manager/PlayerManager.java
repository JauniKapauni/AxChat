package de.jaunikapauni.axchat.manager;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    AxChat reference;
    public PlayerManager(AxChat reference){
        this.reference = reference;
    }

    public List<String> getOnlinePlayers(){
        List<String> list = new ArrayList<>();
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM players WHERE online = TRUE")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String uuid = rs.getString("uuid");
                    Player p = Bukkit.getPlayer(uuid);
                    list.add(p.getName());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void updatePlayerStatus(UUID uuid, boolean online){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("REPLACE players(uuid, online) VALUES (?, ?)")){
                ps.setString(1, uuid.toString());
                ps.setBoolean(2, online);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
