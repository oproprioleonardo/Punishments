package com.leonardo.bungee;

import com.google.gson.Gson;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.reports.ReportPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends Plugin {

    private static Main instance;
    private static boolean onlineMode;
    private static Configuration dataConfiguration;
    private static Gson gson;

    public static Configuration getDataConfiguration() {
        return dataConfiguration;
    }

    public static boolean isOnlineMode() {
        return onlineMode;
    }

    public static Main getInstance() {
        return instance;
    }

    public static Gson getGson() {
        return gson;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Configuration getConfiguration(String fileName, String folder, String path) {
        final File ctx = new File(Main.getInstance().getDataFolder(), folder);
        try {
            if (!Main.getInstance().getDataFolder().exists()) Main.getInstance().getDataFolder().mkdir();
            if (!ctx.exists()) ctx.mkdir();
            final File file = new File(ctx, fileName);
            if (!file.exists()) {
                Files.copy(Main.getInstance().getClass().getResourceAsStream(path + fileName), file.toPath());
            }
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(ctx, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Configuration getConfiguration(String fileName, String path) {
        try {
            if (!Main.getInstance().getDataFolder().exists()) Main.getInstance().getDataFolder().mkdir();
            final File file = new File(Main.getInstance().getDataFolder(), fileName);
            if (!file.exists()) {
                Files.copy(Main.getInstance().getClass().getResourceAsStream(path + fileName), file.toPath());
            }
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();
        onlineMode = Main.getInstance().getProxy().getConfig().isOnlineMode();
        dataConfiguration = Main.getConfiguration("data.yml", "/");
        new PunishmentPlugin();
        new ReportPlugin();
        ModerationBot.get();
    }

    @Override
    public void onDisable() {
        PunishmentPlugin.getDisable().accept(PunishmentPlugin.getInstance());
        ReportPlugin.getDisable().accept(ReportPlugin.getInstance());
    }
}
