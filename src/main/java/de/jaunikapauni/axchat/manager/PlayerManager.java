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
            try(PreparedStatement ps = conn.prepareStatement("SELECT name FROM players WHERE online = TRUE")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void updatePlayerStatus(UUID uuid, String name, boolean online){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("REPLACE players(uuid, name, online) VALUES (?, ?, ?)")){
                ps.setString(1, uuid.toString());
                ps.setString(2, name);
                ps.setBoolean(3, online);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMail(UUID sender, UUID receiver, String message){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO mails(sender_uuid, receiver_uuid, content) VALUES (?, ?, ?)")){
                ps.setString(1, sender.toString());
                ps.setString(2, receiver.toString());
                ps.setString(3, message);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearMail(UUID receiver){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM mails WHERE receiver_uuid = ?")){
                ps.setString(1, receiver.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readMail(UUID receiver){
        List<String> mails = new ArrayList<>();
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT sender_uuid, content FROM mails WHERE receiver_uuid = ?")){
                ps.setString(1, receiver.toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String senderUUID = rs.getString("sender_uuid");
                    String content = rs.getString("content");
                    OfflinePlayer sender = Bukkit.getOfflinePlayer(UUID.fromString(senderUUID));
                    mails.add(sender.getName() + ": " + content);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mails;
    }
}
