package org.itakinn.tknlogs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String message = "[JOIN] " + e.getPlayer().getName()+
        " X: "+e.getPlayer().getLocation().getBlockX()+
        " Y: "+e.getPlayer().getLocation().getBlockY()+
        " Z: "+e.getPlayer().getLocation().getBlockZ()+
        " World: "+e.getPlayer().getLocation().getWorld().getName();
        TKNLogs.getDiscord().sendMessage(message);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        String message = "[DEATH] " + e.getEntity().getName()+
        " X: "+e.getEntity().getLocation().getBlockX()+
        " Y: "+e.getEntity().getLocation().getBlockY()+
        " Z: "+e.getEntity().getLocation().getBlockZ()+
        " World: "+e.getEntity().getLocation().getWorld().getName();
        TKNLogs.getDiscord().sendMessage(message);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        String message = "[QUIT] " + e.getPlayer().getName()+
        " X: "+e.getPlayer().getLocation().getBlockX()+
        " Y: "+e.getPlayer().getLocation().getBlockY()+
        " Z: "+e.getPlayer().getLocation().getBlockZ()+
        " World: "+e.getPlayer().getLocation().getWorld().getName();
        TKNLogs.getDiscord().sendMessage(message);
    }
}