package org.itakinn.tknlogs;

import org.bukkit.Bukkit;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class TKNLogs extends JavaPlugin {
    private static DiscordNotifier discord;
    private static JavaPlugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        reloadConfig();

        discord = new DiscordNotifier();
        Logger logger = Bukkit.getLogger();
        logger.addHandler(new ErrorCatcher());

    }
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    public static DiscordNotifier getDiscord(){
        return discord;
    }
    @Override
    public void onDisable() {
        discord.sendMessage("Desligando o plugin");
        discord.sendShutdown();
    }
}
