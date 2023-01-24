package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetTag {
    public void giveTag(Player p, String tag){
        //donner au joueur un nametag avec le nom donné en paramètre
        ItemStack nametag = new ItemStack(Material.NAME_TAG);
        //renommer le nametag
        nametag.editMeta(meta -> meta.displayName(Component.text(tag)));
        p.getInventory().addItem(nametag);
        p.sendMessage(Component.text("You have been given a nametag with the name " + tag));
    }
}
