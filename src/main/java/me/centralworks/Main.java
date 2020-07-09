package me.centralworks;

import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.reports.ReportPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main extends Plugin {

    protected static Main instance;
    protected static boolean onlineMode;
    protected static Configuration dataConfiguration;
    protected static Configuration immune;
    protected static List<String> usersImmune;

    public static List<String> getUsersImmune() {
        return usersImmune;
    }

    public static Configuration getImmune() {
        return immune;
    }

    public static Configuration getDataConfiguration() {
        return dataConfiguration;
    }

    public static boolean isOnlineMode() {
        return onlineMode;
    }

    public static Main getInstance() {
        return instance;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Configuration getConfiguration(String fileName, String folder, String path) {
        final File ctx = new File(Main.getInstance().getDataFolder(), folder);
        try {
            if (!Main.getInstance().getDataFolder().exists()) {
                Main.getInstance().getDataFolder().mkdir();
            }
            if (!ctx.exists()) {
                ctx.mkdir();
            }
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
            if (!Main.getInstance().getDataFolder().exists()) {
                Main.getInstance().getDataFolder().mkdir();
            }
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

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Override
    public void onEnable() {
        instance = this;
        onlineMode = Main.getInstance().getProxy().getConfig().isOnlineMode();
        dataConfiguration = Main.getConfiguration("data.yml", "/");
        immune = Main.getConfiguration("immune.yml", "/");
        usersImmune = getImmune().getStringList("Users");
        new PunishmentPlugin();
        new ReportPlugin();
    }

    @Override
    public void onDisable() {
        PunishmentPlugin.getDisable().accept(PunishmentPlugin.getInstance());
    }
}
