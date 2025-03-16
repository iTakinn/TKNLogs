package org.itakinn.tknlogs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String message = "[COMMAND] " + e.getPlayer().getName() + ": " + e.getMessage()+
        " X: "+e.getPlayer().getLocation().getBlockX()+
        " Y: "+e.getPlayer().getLocation().getBlockY()+
        " Z: "+e.getPlayer().getLocation().getBlockZ()+
        " World: "+e.getPlayer().getLocation().getWorld().getName();
        TKNLogs.getDiscord().sendMessage(message);
    }
    @EventHandler
    public void onConsoleCommand(ServerCommandEvent e) {
        String message = "[CONSOLE] Command: " + e.getCommand();
        TKNLogs.getDiscord().sendMessage(message);
    }
}
