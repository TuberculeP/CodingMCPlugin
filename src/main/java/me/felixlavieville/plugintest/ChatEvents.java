package me.felixlavieville.plugintest;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;

public class ChatEvents implements Listener {

    TextColor basicPseudo = TextColor.color(0x5197FF);
    TextColor adminPseudo = TextColor.color(0xFF9913);
    Player playerThatSaidQuoi = null;

    Title feurTitle = Title.title(
            Component.text("Feur").color(TextColor.color(0xFF0000)),
            Component.text("quoi feur"),
            Title.Times.times(
                    Duration.ofSeconds(1),
                    Duration.ofSeconds(10),
                    Duration.ofSeconds(1)));


    public void ratio(Player p){
        p.getServer().broadcast(Component.text("+1").color(TextColor.color(0x00FF00)).append(Component.text(" from " + p.getName()).color(basicPseudo)));
    }

    public void flop(Player p){
        p.getServer().broadcast(Component.text("non").color(TextColor.color(0xFF0000)).append(Component.text(" from " + p.getName()).color(basicPseudo)));
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.setCancelled(true);
        TextColor pseudo = basicPseudo;
        if (event.getPlayer().isOp()) {
            pseudo = adminPseudo;
        }
        //EVENT LOREM
        if(event.message().equals(Component.text("lorem"))){
            event.message(Component.text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        }

        if(event.message().toString().contains("ratio")){
            Component check = Component.text(" [✔]").color(TextColor.color(0x00FF00));
            Component cross = Component.text(" [✘]").color(TextColor.color(0xFF0000));
            HoverEvent<Component> checkHover = HoverEvent.showText(Component.text("Click to ratio"));
            HoverEvent<Component> crossHover = HoverEvent.showText(Component.text("Click to flop"));
            check = check.hoverEvent(checkHover);
            cross = cross.hoverEvent(crossHover);

            ClickEvent checkClick = ClickEvent.runCommand("/ratio");
            ClickEvent crossClick = ClickEvent.runCommand("/flop");
            check = check.clickEvent(checkClick);
            cross = cross.clickEvent(crossClick);
            event.message(event.message().append(check).append(cross));
        }

        //ENVOI D'UN MESSAGE DANS LE CHAT --------------------------------------------
        Component message = Component.text("[")
                .append(Component.text(event.getPlayer().getName()).color(pseudo))
                .append(Component.text("] "))
                .append(event.message());
        event.getPlayer().getServer().broadcast(message);


        //EVENTS FEUR ---------------------------------------------------------------
        if(event.message().toString().contains("quoi")){
            playerThatSaidQuoi = event.getPlayer();
        }
        if(event.message().toString().contains("feur")){
            if(playerThatSaidQuoi != null){
                event.getPlayer().getServer().broadcast(Component.text("[Serveur] Félicitations à ")
                        .append(Component.text(event.getPlayer().getName()).color(pseudo))
                        .append(Component.text(" qui a feur "))
                        .append(Component.text(playerThatSaidQuoi.getName()).color(pseudo)));
                playerThatSaidQuoi.showTitle(feurTitle);
                playerThatSaidQuoi = null;
            }
        }
        if(!event.message().toString().contains("feur")
                && playerThatSaidQuoi != null
                && !event.message().toString().contains("quoi")){
            playerThatSaidQuoi = null;
        }
    }
}
