package com.koens.commandblox;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlox extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getCommand("cmdplace").setExecutor(new PlaceCommand());
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {

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
    }
}
