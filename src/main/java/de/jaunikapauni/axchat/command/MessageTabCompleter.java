package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageTabCompleter implements TabCompleter {
    AxChat reference;
    public MessageTabCompleter(AxChat reference){
        this.reference = reference;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            String input = args[0].toLowerCase();
            Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
                for(String name : reference.getPlayerManager().getOnlinePlayers()){
                    if(name.toLowerCase().startsWith(input)){
                        list.add(name);
                    }
                }
            });
        }
        return list;
    }
}
