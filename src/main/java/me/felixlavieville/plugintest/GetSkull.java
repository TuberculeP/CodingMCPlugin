package me.felixlavieville.plugintest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GetSkull {
    public void getSkull(Player p, String name){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta(meta);
        p.getInventory().addItem(skull);
        p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as reçu la tête de " + name);
    }
}
