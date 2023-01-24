package me.felixlavieville.plugintest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class PlayerHUD implements Listener {

    public static void updateHUD(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("test", Criteria.DUMMY, Component.text(ChatColor.LIGHT_PURPLE + "CodingMC"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.GOLD + "En ligne :");
        score.setScore(Bukkit.getOnlinePlayers().size());
        //afficher la thune
        Score score2 = objective.getScore(ChatColor.GOLD + "VSCoins :");
        score2.setScore(PluginTest.getPlugin().getConfig().getInt("money." + p.getUniqueId()));
        p.setScoreboard(board);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getServer().getOnlinePlayers().forEach(PlayerHUD::updateHUD);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskLater(
                PluginTest.getPlugin(),
                () -> Bukkit.getServer().getOnlinePlayers().forEach(PlayerHUD::updateHUD),
                20);
    }
}
