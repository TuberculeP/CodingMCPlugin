package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.awt.*;
import java.util.Objects;

public class TPA {

    String prefix = "§6[§eTéléportation§6]§r §a §e";
    Server server = Bukkit.getServer();
    public void tpRequest(Player p, Player target) throws InterruptedException {
        //si la demande n'existe pas
        if(PluginTest.getPlugin().getConfig().get("tpa." + target.getName()+"."+p.getName()) == null){
            p.sendMessage(prefix + "Tu as demandé une téléportation à " + target.getName());
            Component accept = Component.text(" [✔]").color(TextColor.color(0x00FF00));
            HoverEvent<Component> acceptHover = HoverEvent.showText(Component.text("Accepter la téléportation de " + p.getName()));
            accept = accept.hoverEvent(acceptHover);
            ClickEvent acceptClick = ClickEvent.runCommand("/tpaccept " + p.getName());
            accept = accept.clickEvent(acceptClick);
            Component deny = Component.text(" [✘]").color(TextColor.color(0xFF0000));
            HoverEvent<Component> denyHover = HoverEvent.showText(Component.text("Refuser la téléportation de " + p.getName()));
            deny = deny.hoverEvent(denyHover);
            ClickEvent denyClick = ClickEvent.runCommand("/tpdeny " + p.getName());
            deny = deny.clickEvent(denyClick);
            PluginTest.getPlugin().getConfig().set("tpa." + p.getName(), target.getName());
            PluginTest.getPlugin().saveConfig();
            target.sendMessage(Component.text(prefix + "Tu as reçu une demande de téléportation de " + p.getName()).append(accept).append(deny));

            Bukkit.getScheduler().scheduleSyncDelayedTask(PluginTest.getPlugin(), () -> {
                if(PluginTest.getPlugin().getConfig().get("tpa." + p.getName()) != null){
                    target.sendMessage(prefix+"La demande de " + p.getName() + " a expiré");
                    p.sendMessage(prefix+"La demande de téléportation vers "+target.getName()+" a expiré");
                    PluginTest.getPlugin().getConfig().set("tpa." + p.getName(), null);
                    PluginTest.getPlugin().saveConfig();
                }
            }, 20*30);
        }else{
            p.sendMessage("You already have a teleportation request to " + target.getName());
        }
    }

    public void tpAccept(Player requester, Player accepter){
        accepter.sendMessage(prefix + "Tu as accepté la demande de " + requester.getName());
        requester.sendMessage("Demande acceptée ! Téléportation vers " + accepter.getName());
        requester.teleport(accepter.getLocation());
        PluginTest.getPlugin().getConfig().set("tpa." + requester.getName(), null);
        PluginTest.getPlugin().saveConfig();
    }

    public void tpDeny(Player requester, Player denyer){
        denyer.sendMessage("Tu as refusé la téléportation vers " + requester.getName());
        requester.sendMessage("Demande refusée par " + denyer.getName());
        PluginTest.getPlugin().getConfig().set("tpa." + requester.getName(), null);
        PluginTest.getPlugin().saveConfig();
    }
}
