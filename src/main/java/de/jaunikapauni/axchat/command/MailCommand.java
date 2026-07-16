package de.jaunikapauni.axchat.command;

import de.jaunikapauni.axchat.AxChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MailCommand implements CommandExecutor {
    AxChat reference;
    public MailCommand(AxChat reference) {this.reference = reference;}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axchat.mail")){
            p.sendMessage("You don't have the permission! [axchat.mail]");
            return true;
        }
        if(args.length == 0){
            return false;
        }
        switch(args[0].toLowerCase()){
            case "send":
                UUID senderUUID = p.getUniqueId();
                String msg = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                UUID receiverUUID = receiver.getUniqueId();
                reference.getPlayerManager().sendMail(senderUUID, receiverUUID, msg);
                break;
            case "clear":
                reference.getPlayerManager().clearMail(p.getUniqueId());
                break;
            case "read":
                List<String> mails = reference.getPlayerManager().readMail(p.getUniqueId());
                if(mails.isEmpty()){
                    p.sendMessage("You have no mails!");
                    return true;
                }
                for(String mail : mails){
                    p.sendMessage(mail);
                }
                break;
            default:
                p.sendMessage(ChatColor.RED + "Invalid command!");
                break;
        }
        return true;
    }
}
