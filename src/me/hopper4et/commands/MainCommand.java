package me.hopper4et.commands;

import me.hopper4et.DeathData;
import me.hopper4et.DeathInventory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MainCommand implements Listener, CommandExecutor {

    Inventory inventory = null;
    public MainCommand(DeathInventory plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            if (!event.getWhoClicked().hasPermission("deathinventory.cantakeitems"))
                event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deathinv")){
            if (!(sender instanceof Player playerSender)) return true;


            String player = null;
            if (args.length == 0)
                player = playerSender.getName();
            else
                player = args[0];


            Location deathLocation = null;
            if (Bukkit.getPlayer(player) != null)
                deathLocation = Bukkit.getPlayer(player).getLastDeathLocation();


            if (deathLocation == null)
                playerSender.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + "DeathInv" + ChatColor.GRAY + "] Локация смерти не найдена или игрок не в сети");
            else {
                String coordinats = String.format("%d %d %d", deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ());
                TextComponent textComponent = new TextComponent("§7[§fDeathInv§7] Координаты смерти игрока " + player + ": ");
                TextComponent textComponent2 = new TextComponent("§a§n" + coordinats);
                textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/tp @s " + coordinats).create()));
                textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + coordinats));
                textComponent.addExtra(textComponent2);
                playerSender.spigot().sendMessage(textComponent);
            }


            if (playerSender.hasPermission("deathinventory.showinventory")) {
                if (DeathData.names.contains(player)) {
                    inventory = Bukkit.createInventory(null, 45, player);
                    inventory.setContents(DeathData.inventories.get(DeathData.names.indexOf(player)).getContents());
                    playerSender.openInventory(inventory);
                }
                else {
                    playerSender.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + "DeathInv" + ChatColor.GRAY + "] Инвентарь не найден");
                }
            }
        }
        return true;
    }
}
