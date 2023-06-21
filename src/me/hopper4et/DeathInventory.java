package me.hopper4et;

import me.hopper4et.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathInventory extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("deathinv").setExecutor(new MainCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String name = event.getEntity().getName();
        Inventory inventory = Bukkit.createInventory(null, 45);
        inventory.setContents(event.getEntity().getInventory().getContents());
        Location location = event.getEntity().getLocation();
        String coordinates = String.format("%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (DeathData.names.contains(name)) {
            DeathData.inventories.set(DeathData.names.indexOf(name), inventory);
            DeathData.coordinates.set(DeathData.names.indexOf(name), coordinates);
        } else {
            DeathData.names.add(name);
            DeathData.inventories.add(inventory);
            DeathData.coordinates.add(coordinates);
        }
    }
}
