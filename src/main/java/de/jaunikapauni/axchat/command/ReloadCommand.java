package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    AxChat reference;
    public ReloadCommand(AxChat reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axchat.reload")){
            p.sendMessage("You don't have the permission! [axchat.reload]");
            return true;
        }
        reference.reloadConfig();
        p.sendMessage("config.yml was reloaded!");
        return true;
    }
}
