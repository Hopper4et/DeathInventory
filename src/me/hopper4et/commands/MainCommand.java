package me.hopper4et.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.hopper4et.DeathData;
import me.hopper4et.DeathInventory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.chat.TextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class MainCommand implements Listener, CommandExecutor {


    Plugin plugin;
    public MainCommand(DeathInventory plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Inventory inventory = null;



    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            if (!event.getWhoClicked().hasPermission("deathinventory.cantakeitems"))
                event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deathinv")){
            if (!(sender instanceof Player playerSender)) return true;
            String player = null;
            if (args.length == 0)
                player = playerSender.getName();
            else
                player = args[0];

            FileConfiguration config = plugin.getConfig();

            if (DeathData.names.contains(player)) {
                if (playerSender.hasPermission("deathinventory.showinventory")) {
                    inventory = Bukkit.createInventory(null, 45, player);
                    inventory.setContents(DeathData.inventories.get(DeathData.names.indexOf(player)).getContents());
                    playerSender.openInventory(inventory);
                }
                String outputText = config.getString("translate.death-coordinates");
                outputText = outputText.replace("%nickname%", player);
                outputText = outputText.replace("%coordinates%", DeathData.coordinates.get(DeathData.names.indexOf(player)));
                playerSender.spigot().sendMessage(ComponentSerializer.parse(outputText));
            }
            else {
                String outputText = config.getString("translate.death-location-not-found");
                outputText = outputText.replace("%nickname%", player);
                playerSender.spigot().sendMessage(ComponentSerializer.parse(outputText));
            }
        }
        return true;
    }
}
