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

public class InfoCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        if (p.hasMetadata("commandBlockSelected")) {
            String[] locationvalues = p.getMetadata("commandBlockSelected").get(0).asString().split(",");
            Location loc = new Location(p.getWorld(), Double.parseDouble(locationvalues[0]), Double.parseDouble(locationvalues[1]), Double.parseDouble(locationvalues[2]));
            if (isCommandBlock(loc.getBlock().getType())) {
                CommandBlock cb = (CommandBlock) loc.getBlock().getState();
                String cmd = cb.getCommand();
                p.sendMessage(ChatColor.LIGHT_PURPLE + "The command in the selected command block is: " + cmd);
            }
            else {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "The block you selected is not a command block!");
            }
        }
        else {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Please first select a block!");
        }
        return true;
    }

    private boolean isCommandBlock(Material block) {
        return (block == Material.COMMAND || block == Material.COMMAND_CHAIN || block == Material.COMMAND_REPEATING);
    }
}
