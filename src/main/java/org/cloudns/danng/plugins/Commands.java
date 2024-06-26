package org.cloudns.danng.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CommandSender sender = commandSender;

        if (sender.isOp()){
            sender.sendMessage("Oranges and cucumbers");
        }
        return true;
    }
}
