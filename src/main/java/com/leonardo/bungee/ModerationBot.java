package com.leonardo.bungee;

import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.PlaceHolder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.config.Configuration;

import java.awt.*;
import java.util.Objects;

public class ModerationBot extends ListenerAdapter {

    private static ModerationBot instance;
    private boolean working = false;
    private JDA jda;

    private ModerationBot() {
        if (Main.getDataConfiguration().getBoolean("Discord.toggle")) {
            final JDA jda = JDABuilder.createDefault(Main.getDataConfiguration().getString("Discord.token")).setActivity(Activity.watching("Punishments Plugin - Minecraft")).build();
            jda.addEventListener(this);
            this.working = true;
        }
    }

    public static ModerationBot get() {
        if (instance == null) instance = new ModerationBot();
        return instance;
    }

    public boolean isWorking() {
        return working;
    }

    public void announcement(Punishment p) {
        final Configuration cfg = Main.getDataConfiguration();
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.getColor(cfg.getString("Discord.embed-color")));
        builder.setTitle(cfg.getString("Discord.title"));
        builder.setAuthor(cfg.getString("Discord.author-name"));
        builder.setImage(cfg.getString("Discord.author-img"));
        builder.setDescription(cfg.getString("Discord.description"));
        builder.setFooter(cfg.getString("Discord.footer"), cfg.getString("Discord.author-img"));
        new PlaceHolder(cfg.getStringList("Discord.fields"), p).applyPlaceHolders().forEach(s -> {
            final String[] f = s.split("::");
            builder.addField(f[0], f[1], Boolean.parseBoolean(f[2]));
        });
        Objects.requireNonNull(jda.getGuildById(cfg.getString("Discord.guild-id"))).getTextChannels().stream()
                .filter(textChannel -> textChannel.getName().equalsIgnoreCase(cfg.getString("Discord.channel-announcement")))
                .findFirst().get().sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        this.jda = event.getJDA();
    }
}
