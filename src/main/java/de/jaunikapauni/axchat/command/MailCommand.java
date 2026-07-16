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
                if(args.length < 3){
                    return false;
                }
                UUID senderUUID = p.getUniqueId();
                String msg = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                if(!receiver.hasPlayedBefore() && !receiver.isOnline()){
                    p.sendMessage("Unkown player.");
                    return true;
                }
                UUID receiverUUID = receiver.getUniqueId();
                Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
                    reference.getPlayerManager().sendMail(senderUUID, receiverUUID, msg);
                });
                p.sendMessage("Mail sent.");
                break;
            case "clear":
                Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
                    reference.getPlayerManager().clearMail(p.getUniqueId());
                });
                p.sendMessage("Your mailbox was cleared.");
                break;
            case "read":
                Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
                    List<String> mails = reference.getPlayerManager().readMail(p.getUniqueId());
                    Bukkit.getScheduler().runTask(reference, () -> {
                        if(mails.isEmpty()){
                            p.sendMessage("You have no mails!");
                            return;
                        }
                        for(String mail : mails){
                            p.sendMessage(mail);
                        }
                    });
                });
                break;
            default:
                p.sendMessage(ChatColor.RED + "Invalid command!");
                break;
        }
        return true;
    }
}
