package org.itakinn.tknlogs;

import org.bukkit.Bukkit;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class TKNLogs extends JavaPlugin {
    private static DiscordNotifier discord;
    private static JavaPlugin plugin;
    private LoggerReader logMonitor;
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        logMonitor = new LoggerReader();
        logMonitor.start();
        plugin = this;
        saveDefaultConfig();
        reloadConfig();
        discord = new DiscordNotifier();
        Logger logger = Bukkit.getLogger();
        logger.addHandler(new ErrorCatcher());
        ErrorCatcher.register();
        getLogger().info("PLUGIN STARTED");
    }
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    public static DiscordNotifier getDiscord(){
        return discord;
    }
    @Override
    public void onDisable() {
        discord.sendMessage("TURNING OFF PLUGIN");
        discord.sendShutdown();
    }
}
