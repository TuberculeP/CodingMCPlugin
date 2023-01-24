package me.felixlavieville.plugintest;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HomeSystem {
    public void setHome(Player p, String name){
        PluginTest.getPlugin().getConfig().set("homes."+p.getUniqueId()+"."+name, p.getLocation());
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as défini ton home " + name);
    }

    public void delHome(Player p, String name){
        PluginTest.getPlugin().getConfig().set("homes."+p.getUniqueId()+"."+name, null);
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as supprimé ton home " + name);
    }

    public void home(Player p, String name){
        CommandBack commandBack = new CommandBack();
        commandBack.setBack(p);
        p.teleport((Location) Objects.requireNonNull(PluginTest.getPlugin().getConfig().get("homes."+p.getUniqueId()+"."+name)));
        p.sendMessage("§6[§eCodingMC§6]§r §a §eTu as été téléporté à ton home " + name);
    }

    public void listHome(Player p){
        String[] homes = getHomes(p);
        if(homes.length == 0){
            p.sendMessage("§6[§eCodingMC§6]§r §a §eTu n'as pas encore de home. Utilise /sethome pour en créer un");
        }else{
            p.sendMessage("§6[§eCodingMC§6]§r §a §eVoici tes homes :");
            for(String home : homes){
                p.sendMessage("§6[§eCodingMC§6]§r §a §e- " + home);
            }
        }
    }

    //une fonction qui retourne la liste des noms de homes d'un joueur
    public String[] getHomes(Player p){
        //liste les home dans la configuration
        ConfigurationSection homes = PluginTest.getPlugin().getConfig().getConfigurationSection("homes." + p.getUniqueId());
        //si le joueur n'a pas de home, on retourne un tableau vide
        if(homes == null) return new String[0];
        //sinon on retourne la liste des noms de homes
        return homes.getKeys(false).toArray(new String[0]);
    }
}
