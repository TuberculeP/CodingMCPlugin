package me.felixlavieville.plugintest;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MoneySystem {
    Server server = Bukkit.getServer();
    public void getSalaire(Player p){
        //le cooldown est de 12h
        long lastSalaire = PluginTest.getPlugin().getConfig().getLong("lastSalaire."+p.getUniqueId());
        long now = System.currentTimeMillis();
        long timeLeft = (lastSalaire + 12 * 60 * 60 * 1000 - now)/1000;
        if(timeLeft > 0){
            int hoursLeft = (int) timeLeft / 3600;
            int minutesLeft = (int) timeLeft%3600/60;
            int secondsLeft = (int) timeLeft%3600%60;
            p.sendMessage("§6[§eJetBank§6]§r §a §cVous devez attendre "+hoursLeft+"h"+minutesLeft+"m"+secondsLeft+"s avant de pouvoir récupérer votre salaire.");
            return;
        }
        int money = PluginTest.getPlugin().getConfig().getInt("money." + p.getUniqueId());
        money += 300;
        PluginTest.getPlugin().getConfig().set("money." + p.getUniqueId(), money);
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eJetBank§6]§r §a §eTu as reçu ton salaire de 300 VSCoins");
        PluginTest.getPlugin().getConfig().set("lastSalaire."+p.getUniqueId(), System.currentTimeMillis());
        PluginTest.getPlugin().saveConfig();
        p.playSound(p.getLocation(), "minecraft:entity.player.levelup", 1, 1);
        PlayerHUD.updateHUD(p);
    }

    public void getMoney(Player p){
        int money = PluginTest.getPlugin().getConfig().getInt("money." + p.getUniqueId());
        p.sendMessage("§6[§eJetBank§6]§r §a §eTu as " + money + " VSCoins sur ton compte");
    }

    public void transferMoney(Player p, Player target, int amount){
        int money = PluginTest.getPlugin().getConfig().getInt("money." + p.getUniqueId());
        if(money < amount){
            p.sendMessage("§6[§eJetBank§6]§r §a §cTu n'as pas assez d'argent sur ton compte");
            return;
        }
        money -= amount;
        PluginTest.getPlugin().getConfig().set("money." + p.getUniqueId(), money);
        PluginTest.getPlugin().saveConfig();
        int targetMoney = PluginTest.getPlugin().getConfig().getInt("money." + target.getUniqueId());
        targetMoney += amount;
        PluginTest.getPlugin().getConfig().set("money." + target.getUniqueId(), targetMoney);
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eJetBank§6]§r §a §eTu as envoyé " + amount + " VSCoins à " + target.getName());
        target.sendMessage("§6[§eJetBank§6]§r §a §eTu as reçu " + amount + " VSCoins de la part de " + p.getName());
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        PlayerHUD.updateHUD(p);
        PlayerHUD.updateHUD(target);
    }

    public void addMoney(Player p, int amount){
        int money = PluginTest.getPlugin().getConfig().getInt("money." + p.getUniqueId());
        money += amount;
        PluginTest.getPlugin().getConfig().set("money." + p.getUniqueId(), money);
        PluginTest.getPlugin().saveConfig();
        p.sendMessage("§6[§eJetBank§6]§r §a §eTu as reçu " + amount + " VSCoins de la part du serveur");
        p.playSound(p.getLocation(), "minecraft:entity.player.levelup", 1, 1);
        PlayerHUD.updateHUD(p);
    }

}