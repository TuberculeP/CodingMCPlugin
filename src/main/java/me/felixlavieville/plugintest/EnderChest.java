package me.felixlavieville.plugintest;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EnderChest {
    public void enderChest(Player p){;
        p.openInventory(p.getEnderChest());
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
    }
}
