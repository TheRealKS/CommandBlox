package com.koens.commandblox;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        if (p.hasMetadata("commandBlockSelected")) {
            if (strings != null) {
                //Loop through args to get the desired command
                String commandToSet = "";
                for (String part : strings) {
                    if (commandToSet.isEmpty()) {
                        commandToSet = part;
                    }
                    else
                        commandToSet += " " + part;
                }
                String[] locationvalues = p.getMetadata("commandBlockSelected").get(0).asString().split(",");
                Location loc = new Location(p.getWorld(), Double.parseDouble(locationvalues[0]), Double.parseDouble(locationvalues[1]), Double.parseDouble(locationvalues[2]));
                Block blocktoset = loc.getBlock();
                blocktoset.setType(Material.COMMAND);
                CommandBlock cb = (CommandBlock) blocktoset.getState();
                cb.setCommand(commandToSet);
                cb.update();
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Command block placed!");
            }
            else {
                String[] locationvalues = p.getMetadata("commandBlockSelected").get(0).asString().split(",");
                Location loc = new Location(p.getWorld(), Double.parseDouble(locationvalues[0]), Double.parseDouble(locationvalues[1]), Double.parseDouble(locationvalues[2]));
                Block blocktoset = loc.getBlock();
                blocktoset.setType(Material.COMMAND);
            }
        }
        else {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Please first select a block!");
        }
        return true;
    }
}
