package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.Duration;
import java.util.Objects;

public class Roubaix {

    Title roubaixTitle = Title.title(
            Component.text("Roubaix").color(TextColor.color(0xFF9200)),
            Component.text(""),
            Title.Times.times(
                    Duration.ofSeconds(3),
                    Duration.ofSeconds(10),
                    Duration.ofSeconds(3)));
    public void setRoubaix(Player p){
        PluginTest.getPlugin().getConfig().set("roubaix", p.getLocation());
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eCodingMC§6]§r §a §eRoubaix défini ! X"+p.getLocation().getX()+" Y"+p.getLocation().getY()+" Z"+p.getLocation().getZ());
    }

    public void roubaix(Player p){
        p.teleport((Location) Objects.requireNonNull(PluginTest.getPlugin().getConfig().get("roubaix")));
        p.showTitle(roubaixTitle);
    }
}
