package me.centralworks.punishments;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.centralworks.punishments.cmds.CmdBan;
import me.centralworks.punishments.cmds.CmdTempBan;
import me.centralworks.punishments.db.dao.PunishmentDAO;
import me.centralworks.punishments.lib.Date;
import me.centralworks.punishments.listeners.ChatListener;
import me.centralworks.punishments.listeners.join.BanListener;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;
import me.centralworks.punishments.punishs.supliers.cached.Reasons;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
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
    protected static Configuration configuration;
    protected static Configuration messages;
    protected static Configuration usages;
    protected static boolean onlineMode;
    protected static Gson gson;

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Configuration getMessages() {
        return messages;
    }

    public static Configuration getUsages() {
        return usages;
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
    protected Configuration getConfiguration(String fileName) {
        try {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            final File file = new File(this.getDataFolder(), fileName);
            if (!file.exists()) {
                Files.copy(this.getResourceAsStream(fileName), file.toPath());
            }
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void reasons() {
        final List<PunishmentReason> list = Lists.newArrayList();
        getConfiguration().getSection("Reasons").getKeys().forEach(s -> {
            final String path = "Reasons." + s + ".";
            final PunishmentReason pr = new PunishmentReason();
            pr.setReason(getConfiguration().getString(path + "reason"));
            pr.setPermission(getConfiguration().getString(path + "permission"));
            pr.setWithIP(getConfiguration().getBoolean(path + "ip"));
            pr.setPunishmentType(PunishmentType.valueOf(getConfiguration().getString(path + "type")));
            if (pr.getPunishmentType().isTemp()) {
                pr.setDuration(Date.getInstance().convertPunishmentDuration(Lists.newArrayList(getConfiguration().getString(path + "duration").split(","))));
            }
        });
        Reasons.getInstance().setReasons(list);
    }

    protected void registerCommand(Command command) {
        getProxy().getPluginManager().registerCommand(instance, command);
    }

    protected void registerListener(Listener listener) {
        getProxy().getPluginManager().registerListener(instance, listener);
    }

    @Override
    public void onEnable() {
        instance = this;
        onlineMode = getProxy().getConfig().isOnlineMode();
        configuration = getConfiguration("config.yml");
        messages = getConfiguration("messages.yml");
        usages = getConfiguration("usages.yml");
        gson = new Gson();
        reasons();
        registerCommand(new CmdBan());
        registerCommand(new CmdTempBan());
        registerListener(new ChatListener());
        registerListener(new BanListener());
        PunishmentDAO.getInstance().createTable();
    }

    @Override
    public void onDisable() {

    }
}
