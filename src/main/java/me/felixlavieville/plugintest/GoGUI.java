package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class GoGUI implements Listener {
    //une fonction go qui ouvre un inventaire d'une ligne à un joueur
    // elle contient un bouton qui permet de retourner au hub (bloc d'herbe)

    public void go(Player p){
        Inventory inv = Bukkit.createInventory(null, 9*5, Component.text("Go"));

        ItemStack HUBitem = new ItemStack(Material.STONE_BRICKS);
        ItemMeta HUBmeta = HUBitem.getItemMeta();
        HUBmeta.displayName(Component.text("Hub"));
        HUBitem.setItemMeta(HUBmeta);
        HUBitem.lore(List.of(new Component[]{Component.text("Retour au hub"),Component.text("/hub")}));
        inv.setItem(9, HUBitem);

        ItemStack enderchest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta enderchestmeta = enderchest.getItemMeta();
        enderchestmeta.displayName(Component.text("Enderchest"));
        enderchest.setItemMeta(enderchestmeta);
        enderchest.lore(List.of(new Component[]{Component.text("Ouvre l'EnderChest"),Component.text("/ec")}));
        inv.setItem(27, enderchest);

        //générer des Grass Block correspondants à chaque Home existants du joueur
        HomeSystem homeSystem = new HomeSystem();
        for (int i = 0; i < homeSystem.getHomes(p).length; i++) {
            ItemStack home = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta homeMeta = home.getItemMeta();
            homeMeta.displayName(Component.text(homeSystem.getHomes(p)[i]).color(TextColor.color(0x14FF00)));
            home.setItemMeta(homeMeta);
            home.lore(List.of(new Component[]{Component.text("Téléportation à votre home "+i)}));
            inv.setItem(11+i, home);
        }

        //fait des lignes de panneaux
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Menu Go"));
            item.setItemMeta(meta);
            inv.setItem(i, item);
            inv.setItem(i+9*2, item);
            inv.setItem(i+9*4, item);
        }

        ItemStack leave = new ItemStack(Material.OAK_DOOR);
        ItemMeta leavemeta = leave.getItemMeta();
        leavemeta.displayName(Component.text("Quitter"));
        leave.setItemMeta(leavemeta);
        inv.setItem(44, leave);

        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getView().title().equals(Component.text("Go"))){
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if(Objects.requireNonNull(event.getCurrentItem()).getType().equals(Material.STONE_BRICKS)){
                Objects.requireNonNull(event.getClickedInventory()).close();
                p.performCommand("hub");
            }
            if(Objects.requireNonNull(event.getCurrentItem()).getType().equals(Material.OAK_DOOR)){
                Objects.requireNonNull(event.getClickedInventory()).close();
            }
            if(Objects.requireNonNull(event.getCurrentItem()).getType().equals(Material.ENDER_CHEST)){
                Objects.requireNonNull(event.getClickedInventory()).close();
                p.performCommand("enderchest");
            }
            if(Objects.requireNonNull(event.getCurrentItem()).getType().equals(Material.GRASS_BLOCK)){
                Objects.requireNonNull(event.getClickedInventory()).close();
                //récuperer l'index de l'item
                int index = event.getSlot();
                //récupérer le nom du home
                HomeSystem homeSystem = new HomeSystem();
                String homeName = homeSystem.getHomes(p)[index-11];
                //téléporter le joueur
                p.performCommand("home "+homeName);
            }
        }
    }
}
