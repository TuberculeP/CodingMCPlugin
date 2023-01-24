package me.felixlavieville.plugintest;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class RandomTP {
    public void randomTP(Player p){
        long cooldownDuration = 5 * 60;
        long cooldown;
        long now;
        long timeLeft = 1;
        boolean canTP = true;

        if(p.hasMetadata("randomtp")){
            cooldown = p.getMetadata("randomtp").get(0).asLong();
            now = System.currentTimeMillis();
            timeLeft = (cooldown + cooldownDuration *1000 - now)/1000;
            System.out.println(timeLeft);
            if(timeLeft > 0){
                canTP = false;
                int minutes = (int) timeLeft / 60;
                int seconds = (int) timeLeft % 60;
                p.sendMessage(ChatColor.RED + "Vous devez attendre " + minutes + " minutes et " + seconds + " secondes avant de pouvoir utiliser cette commande à nouveau.");

            }
        }

        if(canTP){

            //RTP

            Location current = p.getLocation();
            Location randomLocation;
            boolean isSafe = false;
            do {
                randomLocation = new Location(p.getWorld(), Math.random() * 10000 - 500, 0, Math.random() * 10000 - 500);
                randomLocation.setY(p.getWorld().getHighestBlockYAt(randomLocation));
                isSafe = randomLocation.getBlock().getType().isSolid();
                randomLocation.setY(randomLocation.getY() + 1);
            }while(!isSafe);

            p.teleport(randomLocation);
            PluginTest.getPlugin().getLogger().info("RTP at : "+randomLocation+" for player : "+p.getName());
            CommandBack back = new CommandBack();
            back.setBack(p, current);
            p.setMetadata("randomtp", new FixedMetadataValue(PluginTest.getPlugin(), System.currentTimeMillis()));
        }
    }
}
