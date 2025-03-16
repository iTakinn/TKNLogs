package org.itakinn.tknlogs;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;


public class DiscordNotifier {
    private static JDA api;
    private final static JavaPlugin plugin = TKNLogs.getPlugin();
    private final static FileConfiguration config = plugin.getConfig();
    private static TextChannel channel;
    private static final String token = config.getString("discord.token");
    public DiscordNotifier(){
        if(token == null){
            plugin.getLogger().warning("Invalid Token");
            return;
        }
        try {
            api = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();
            channel = api.getTextChannelById(Objects.requireNonNull(config.getString("discord.channel")));
            if(channel!=null){
                channel.sendMessage("Bot Started").queue();
            }else{
                plugin.getLogger().warning("Channel Not Found"+channel.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message){
        channel.sendMessage(message).queue();
    }
    public void sendShutdown(){
        api.shutdown();
    }
    public static JDA getJDA() {return api;}
}