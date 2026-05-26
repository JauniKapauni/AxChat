package de.jaunikapauni.axchat.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ChatManager {
    Jedis publisher;
    public ChatManager(String host, int port){
        publisher = new Jedis(host, port);
    }
    public void publishMessage(String channel, String message){
        publisher.publish(channel, message);
    }

    public void subscribe(String channel){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try(Jedis subscriber = new Jedis("localhost", 6379)){
                    subscriber.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(message);
                            }
                        }
                    }, channel);
                }
            }
        }).start();
    }
}
