package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    AxChat reference;
    public ReloadCommand(AxChat reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        reference.reloadConfig();
        sender.sendMessage("config.yml was reloaded!");
        return true;
    }
}
