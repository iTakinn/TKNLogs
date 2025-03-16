package org.itakinn.tknlogs;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
            plugin.getLogger().warning("Token não encontrado");
            return;
        }
        api = JDABuilder.createDefault(token).build();

        /*        .addEventListeners(new SlashListener()).build();
        api.upsertCommand("genimg", "gera uma img")
                .addOption(OptionType.STRING, "resolução", "resolução da imagem", true, true)
                .addOption(OptionType.STRING, "prompt", "prompt pra gerar a imagem", true)
                .queue();*/

        channel = api.getTextChannelById(Objects.requireNonNull(config.getString("discord.channel-id")));
    }
    public void sendMessage(String message){
        channel.sendMessage(message).queue();


    }
    public void sendShutdown(){
        api.shutdown();
    }

    public static JDA getJDA() {return api;}
}