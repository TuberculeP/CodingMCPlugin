package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopGUI {
    public final ItemStack registerNewItem(Material material, int amount, int price){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.lore(List.of(Component.text("Prix :"), Component.text(price)));
        item.setItemMeta(itemMeta);
        return  item;
    }
    public ItemStack[] forSaleItems = {
            //un stack de bottle d'xp, une selle, 32 pain, un nametag, une skull de Steve, livre Mending, un debug stick, une carotte
            registerNewItem(Material.EXPERIENCE_BOTTLE, 64, 300),
            registerNewItem(Material.SADDLE, 1, 100),
            registerNewItem(Material.BREAD, 32, 50),
            registerNewItem(Material.NAME_TAG, 1, 100),
            registerNewItem(Material.PLAYER_HEAD, 1, 100),
            registerNewItem(Material.ENCHANTED_BOOK, 1, 2000),
            registerNewItem(Material.DEBUG_STICK, 1, 100),
            registerNewItem(Material.CARROT, 1, 1000)

    };

    public void openShop(int type, Player p){
        //générer un nouvel inventaire
        Inventory inv = p.getServer().createInventory(null, 9*5, Component.text("Shop"));
        if(type == 0){
            //alors on affiche les items de forSaleItems
            for(ItemStack item : forSaleItems){
                if(item.getType() == Material.ENCHANTED_BOOK){
                    //on ajoute l'ingrédient "Mending" au livre enchanté
                    ItemMeta itemMeta = item.getItemMeta();

                }
                inv.addItem(item);
            }
            p.openInventory(inv);
        }
    }

}
