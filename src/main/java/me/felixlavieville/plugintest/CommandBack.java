package me.felixlavieville.plugintest;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandBack implements Listener {

    public void setBack(Player p, Location...loc) {
        //insérer dans les metadatas du joueur la position actuelle
        Location l = loc.length == 0 ? p.getLocation() : loc[0];
        p.setMetadata("back", new FixedMetadataValue(PluginTest.getPlugin(), l));
        PluginTest.getPlugin().getLogger().info("back set at : "+p.getLocation());
    }

    public void back(Player p){
        //si le joueur a une position de back
        if(p.hasMetadata("back")){
            Location current = p.getLocation();
            //récupérer la position de back
            Location back = (Location) p.getMetadata("back").get(0).value();
            if(back != null){
                //se téléporter à la position de back
                p.teleport(back);
                setBack(p, current);
            }
        }else{
            HubSpawn hubSpawn = new HubSpawn();
            hubSpawn.hub(p);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        setBack(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        setBack(player);
    }
}
