package me.centralworks.bungee;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class ModerationBot extends ListenerAdapter {

    private static ModerationBot instance;

    private ModerationBot() {
        try {
            final JDA jda = JDABuilder.createDefault(Main.getDataConfiguration().getString("Discord.token")).setActivity(Activity.watching("Punishments Plugin - Minecraft")).build();
            jda.addEventListener(this);
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    public static ModerationBot get() {
        if (instance == null) instance = new ModerationBot();
        return instance;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().equalsIgnoreCase("anunciar")).findFirst().get().sendMessage("Olá").queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().forEach(guild -> {
            System.out.println(guild.getId());
            System.out.println(guild.getName());
            guild.getTextChannels().stream().filter(textChannel -> textChannel.getName().equalsIgnoreCase("anunciar")).findFirst().get().sendMessage("Olá").queue();
        });
    }
}
