package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MessageCommand implements CommandExecutor {
    AxChat reference;
    public MessageCommand(AxChat reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length < 2){
            return false;
        }
        String targetName = args[0];
        String message = "";
        for(int i = 1; i < args.length; i++){
            message = message + args[i] + " ";
        }
        message = message.trim();

        String redisData = sender.getName() + ";" + targetName + ";" + message;
        reference.getChatManager().publishMessage("private_messages", redisData);

        sender.sendMessage(sender.getName() + " - " + targetName + " : " + message);
        return true;
    }
}
