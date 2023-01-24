package me.felixlavieville.plugintest;

import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Food implements Listener  {
    public void feed(Player p){
        //si le joueur ne l'a pas utilisé dans les 30 dernières minutes
        if(!p.hasMetadata("feed")){
            //insérer dans les metadatas du joueur la position actuelle
            p.setMetadata("feed", new FixedMetadataValue(PluginTest.getPlugin(), System.currentTimeMillis()));
            p.setFoodLevel(20);
            p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as été nourri !");
        }else{
            //récupérer la position de back
            long lastFeed = (long) p.getMetadata("feed").get(0).value();
            if(lastFeed + 1800000 < System.currentTimeMillis()){
                //insérer dans les metadatas du joueur la position actuelle
                p.setMetadata("feed", new FixedMetadataValue(PluginTest.getPlugin(), System.currentTimeMillis()));
                p.setFoodLevel(20);
                p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as été nourri !");
            }else{
                p.sendMessage("§6[§eCodingMC§6]§r §c §eTu dois attendre 30 minutes avant de pouvoir te nourrir à nouveau !");
            }
        }
    }

    //quand la faim du joueur baisse
    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if(player.hasMetadata("hubzone")){
            event.setCancelled(true);
        }
    }
}
