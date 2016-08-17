package com.koens.commandblox;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CommandBlox extends JavaPlugin implements Listener {

    private static final Charset ascii = Charset.forName("ASCII");

    @Override
    public void onEnable() {
        getCommand("cmdplace").setExecutor(new PlaceCommand());
        getCommand("cmdinfo").setExecutor(new InfoCommand());
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    @EventHandler
    public void onSwing(BlockBreakEvent event) {
        Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        if (inv.getItemInMainHand().isSimilar(axe)) {
            event.setCancelled(true);
            Location blockLocation = event.getBlock().getLocation();
            String locationString = blockLocation.getBlockX() + "," + blockLocation.getBlockY() + "," + blockLocation.getBlockZ();
            p.setMetadata("commandBlockSelected", new FixedMetadataValue(this, locationString));
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Selected block " + locationString + " as location!");
        }
        else if (inv.getItemInMainHand().getType() == Material.BOOK_AND_QUILL && isCommandBlock(event.getBlock().getType())) {
            event.setCancelled(true);
            BookMeta meta = (BookMeta) inv.getItemInMainHand().getItemMeta();
            List<String> pages = meta.getPages();
            String cmd = "";
            List<String> option = getOptions(meta.getPage(1));
            if (option != null) {
                for (String page : pages) {
                    if (page.contains("%OPTIONS%")) {
                        cmd = page.substring(page.lastIndexOf("%") + 1);
                    } else {
                        cmd += page;
                    }
                }
                cmd = cmd.replaceAll("(\\r|\\n)", "");
                byte[] bytes = cmd.getBytes(ascii);
                cmd = new String(bytes, ascii);
                cmd = cmd.replace(cmd.substring(0, 4), "");
                if (option.contains("REPEATING"))
                    event.getBlock().setType(Material.COMMAND_REPEATING);
                else if (option.contains("CHAIN"))
                    event.getBlock().setType(Material.COMMAND_CHAIN);
                CommandBlock cb = (CommandBlock) event.getBlock().getState();
                cb.setCommand(cmd);
                cb.update();
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Set command in command block to: " + cmd);
            }
            else {
                for (String page : pages) {
                    if (page.contains("%OPTIONS%")) {
                        cmd = page.substring(page.lastIndexOf("%") + 1);
                    } else {
                        cmd += page;
                    }
                }
                cmd = cmd.replaceAll("(\\r|\\n)", "");
                byte[] bytes = cmd.getBytes(ascii);
                cmd = new String(bytes, ascii);
                cmd = cmd.replace(cmd.substring(0, 4), "");
                CommandBlock cb = (CommandBlock) event.getBlock().getState();
                cb.setCommand(cmd);
                cb.update();
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Set command in command block to: " + cmd);
            }
        }
    }

    private boolean isCommandBlock(Material block) {
        return (block == Material.COMMAND || block == Material.COMMAND_CHAIN || block == Material.COMMAND_REPEATING);
    }

    private List<String> getOptions(String options) {
        if (options.contains("%OPTIONS%") && options.contains("%/OPTIONS%")) {
            List<String> optionslist = new ArrayList<>();
            String opt = options.substring(options.indexOf("%OPTIONS%") + 1, options.indexOf("%/OPTIONS%"));
            if (opt.contains("REPEATING")) {
                optionslist.add("REPEATING");
            }
            else if (opt.contains("CHAIN")) {
                optionslist.add("CHAIN");
            }
            return optionslist;
        }
        else
            return null;
    }
}
