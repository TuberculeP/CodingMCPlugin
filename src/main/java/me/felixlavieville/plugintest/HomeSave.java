package me.felixlavieville.plugintest;

import org.bukkit.entity.Player;

public class HomeSave {
    public void setHome(Player p, String name){
        p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as défini ton home " + name);
    }
}
