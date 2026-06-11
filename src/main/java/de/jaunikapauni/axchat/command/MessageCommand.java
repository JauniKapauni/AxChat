package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageCommand implements CommandExecutor {
    AxChat reference;
    public MessageCommand(AxChat reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axchat.msg")){
            p.sendMessage("You don't have the permission! [axchat.msg]");
            return true;
        }
        if(args.length < 2){
            return false;
        }
        String targetName = args[0];
        String message = "";
        for(int i = 1; i < args.length; i++){
            message = message + args[i] + " ";
        }
        message = message.trim();

        String redisData = p.getName() + ";" + targetName + ";" + message;
        reference.getChatManager().publishMessage("private_messages", redisData);

        p.sendMessage(p.getName() + " - " + targetName + " : " + message);
        return true;
    }
}
