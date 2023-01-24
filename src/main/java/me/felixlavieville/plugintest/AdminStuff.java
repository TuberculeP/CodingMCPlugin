package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminStuff implements Listener {

    TextColor color = TextColor.color(0xFF9913);

    //admin chat prefix
    public Component prefix = Component.text("[Admin] ", color);
    Server server = Bukkit.getServer();

    //LES ITEMS DU MENU ADMIN
    ItemStack time = new ItemStack(Material.CLOCK);
    ItemStack weather = new ItemStack(Material.WATER_BUCKET);
    ItemStack gamemode = new ItemStack(Material.GRASS_BLOCK);
    ItemStack lock = new ItemStack(Material.LEVER);
    ItemStack leave = new ItemStack(Material.OAK_DOOR);

    public void admin(Player p){

        Inventory adminInventory = server.createInventory(p, 9, Component.text("Admin Stuff").color(color));
        ItemMeta timeMeta = time.getItemMeta();
        ItemMeta weatherMeta = weather.getItemMeta();
        ItemMeta gamemodeMeta = gamemode.getItemMeta();
        ItemMeta lockMeta = lock.getItemMeta();
        ItemMeta leaveMeta = leave.getItemMeta();

        timeMeta.displayName(Component.text("Time"));
        weatherMeta.displayName(Component.text("Weather"));
        gamemodeMeta.displayName(Component.text("Gamemode"));
        lockMeta.displayName(Component.text("Lock"));
        leaveMeta.displayName(Component.text("Leave"));

        time.setItemMeta(timeMeta);
        weather.setItemMeta(weatherMeta);
        gamemode.setItemMeta(gamemodeMeta);
        lock.setItemMeta(lockMeta);
        leave.setItemMeta(leaveMeta);

        adminInventory.setItem(0, time);
        adminInventory.setItem(1, weather);
        adminInventory.setItem(2, gamemode);
        adminInventory.setItem(3, lock);
        adminInventory.setItem(8, leave);


        p.openInventory(adminInventory);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text("Admin Stuff").color(color))) {
            boolean timeLock = Boolean.FALSE.equals(server.getWorlds().get(0).getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE));
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType().isAir()) return;
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            if (clickedItem.getType().equals(Material.CLOCK)) {
                server.getWorlds().get(0).setTime(server.getWorlds().get(0).getTime() + 12000);
                p.sendMessage(prefix.append(Component.text("Time set to "+ server.getWorlds().get(0).getTime())));
            }
            if (clickedItem.getType().equals(Material.WATER_BUCKET)) {
                server.getWorlds().get(0).setStorm(!server.getWorlds().get(0).hasStorm());
                p.sendMessage(prefix.append(Component.text("Weather set to "+ server.getWorlds().get(0).hasStorm())));
            }
            if (clickedItem.getType().equals(Material.GRASS_BLOCK)) {
                if(p.getGameMode().equals(GameMode.SURVIVAL)){
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(prefix.append(Component.text("Gamemode set to Creative")));
                }else{
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(prefix.append(Component.text("Gamemode set to Survival")));
                }
            }
            if (clickedItem.getType().equals(Material.LEVER)) {
                if(timeLock){
                    server.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                    p.sendMessage(prefix.append(Component.text("Time lock disabled")));
                }else{
                    server.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                    p.sendMessage(prefix.append(Component.text("Time lock enabled")));
                }
            }
            if (clickedItem.getType().equals(Material.OAK_DOOR)) {
                event.getWhoClicked().closeInventory();
            }
        }
    }
}