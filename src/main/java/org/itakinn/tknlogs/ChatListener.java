package org.itakinn.tknlogs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String message = "[CHAT] " + e.getPlayer().getName() + ": " + e.getMessage();
        TKNLogs.getDiscord().sendMessage(message);
    }
}