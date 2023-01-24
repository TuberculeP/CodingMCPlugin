package me.felixlavieville.plugintest;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;

public class HubSpawn implements Listener {

    public void setHub(Location l){
        PluginTest.getPlugin().getConfig().set("hub", l);
        PluginTest.getPlugin().saveConfig();
    }

    public void hub(Player p, boolean...disableBack){
        if(PluginTest.getPlugin().getConfig().contains("hub")) {
            Location current = p.getLocation();
            Location hub = (Location) PluginTest.getPlugin().getConfig().get("hub");
            if (hub != null) {
                p.teleport(hub);
                PluginTest.getPlugin().getLogger().info("back at : " + hub + " for player : " + p.getName());
                CommandBack back = new CommandBack();
                //si disableBack est vide ou si disableBack[0] est false
                if(disableBack.length == 0 || !disableBack[0]){
                    back.setBack(p, current);
                }
                p.playSound(p.getLocation(), "minecraft:entity.enderman.teleport", 1, 1);
            }
        }
    }

    public void setBeginPoint(Location l){
        PluginTest.getPlugin().getConfig().set("hubzone.begin", l);
        PluginTest.getPlugin().saveConfig();
    }

    public void setEndPoint(Location l){
        PluginTest.getPlugin().getConfig().set("hubzone.end", l);
        PluginTest.getPlugin().saveConfig();
    }

    @EventHandler
    //au respawn, on tp le joueur au hub
    public void onRespawn(PlayerRespawnEvent event){
        Player p = event.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(PluginTest.getPlugin(), new Runnable() {
            @Override
            public void run() {
                hub(p, true);
            }
        }, 1);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(PluginTest.getPlugin(), new Runnable() {
            @Override
            public void run() {
                hub(p, true);
            }
        }, 1);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(p.getLocation().getX() >= begin.getX() && p.getLocation().getX() <= end.getX() && p.getLocation().getZ() >= begin.getZ() && p.getLocation().getZ() <= end.getZ()){
                if(!p.hasMetadata("hubzone")) {
                    p.setMetadata("hubzone", new FixedMetadataValue(PluginTest.getPlugin(), true));
                    //écrire "bienvenue au hub" dans l'action bar
                    p.sendActionBar(Component.text("Bienvenue au hub !").color(TextColor.color(0xFF9913)));
                }
            }else{
                if(p.hasMetadata("hubzone")){
                    p.removeMetadata("hubzone", PluginTest.getPlugin());
                    //écrire "au revoir au hub" dans l'action bar
                    p.sendActionBar(Component.text("À bientôt !").color(TextColor.color(0xFF9913)));
                }
            }
        }
        if(p.getMetadata("hubzone").size() > 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 2));
        }else{
            p.removePotionEffect(PotionEffectType.SPEED);
            p.removePotionEffect(PotionEffectType.JUMP);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockBreakEvent e){
        Block b = e.getBlock();
        Player p = e.getPlayer();
        if(p.isOp() && p.getGameMode() == GameMode.CREATIVE){
            return;
        }
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(b.getLocation().getX() >= begin.getX() && b.getLocation().getX() <= end.getX() && b.getLocation().getZ() >= begin.getZ() && b.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    //empêcher les dégâts des mobs sur les joueurs dans la zone du hub
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;
        Player p = (Player) entity;
        if(!p.hasMetadata("hubzone")) return;
        e.setCancelled(true);
    }

    @EventHandler
    //au placement d'un bloc
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE)) return;
        Block b = e.getBlock();
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(b.getLocation().getX() >= begin.getX() && b.getLocation().getX() <= end.getX() && b.getLocation().getZ() >= begin.getZ() && b.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    //bloquer le feu dans le hub également
    @EventHandler
    public void onFireSpread(BlockSpreadEvent e){
        Block b = e.getBlock();
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(b.getLocation().getX() >= begin.getX() && b.getLocation().getX() <= end.getX() && b.getLocation().getZ() >= begin.getZ() && b.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    //empêcher les mobs de spawner dans la zone du hub
    public void onSpawn(CreatureSpawnEvent e){
        Entity entity = e.getEntity();
        if(entity instanceof Player) return;
        if(entity instanceof ArmorStand) return;
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(entity.getLocation().getX() >= begin.getX() && entity.getLocation().getX() <= end.getX() && entity.getLocation().getZ() >= begin.getZ() && entity.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    //hanging break event
    public void onHangingBreak(HangingBreakByEntityEvent e){
        Player p = (Player) e.getRemover();
        Entity entity = e.getEntity();
        assert p != null;
        if(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE)) return;
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(entity.getLocation().getX() >= begin.getX() && entity.getLocation().getX() <= end.getX() && entity.getLocation().getZ() >= begin.getZ() && entity.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    //hanging place event
    public void onHangingPlace(HangingPlaceEvent e){
        Player p = e.getPlayer();
        assert p != null;
        if(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE)) return;
        Entity entity = e.getEntity();
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(entity.getLocation().getX() >= begin.getX() && entity.getLocation().getX() <= end.getX() && entity.getLocation().getZ() >= begin.getZ() && entity.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    //empecher l'interaction avec les item frame
    public void onItemFrameInteract(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();
        if(!(entity instanceof ItemFrame)) return;
        if(p.isOp() && p.getGameMode().equals(GameMode.CREATIVE)) return;
        Location begin = (Location) PluginTest.getPlugin().getConfig().get("hubzone.begin");
        Location end = (Location) PluginTest.getPlugin().getConfig().get("hubzone.end");
        if(begin != null && end != null){
            if(entity.getLocation().getX() >= begin.getX() && entity.getLocation().getX() <= end.getX() && entity.getLocation().getZ() >= begin.getZ() && entity.getLocation().getZ() <= end.getZ()){
                e.setCancelled(true);
            }
        }
    }
}