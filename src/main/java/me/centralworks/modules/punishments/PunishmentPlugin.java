package me.centralworks.modules.punishments;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.centralworks.Main;
import me.centralworks.lib.Contexts;
import me.centralworks.lib.Date;
import me.centralworks.modules.punishments.cmds.*;
import me.centralworks.modules.punishments.dao.AddressIPDAO;
import me.centralworks.modules.punishments.dao.PunishmentDAO;
import me.centralworks.modules.punishments.dao.WarnDAO;
import me.centralworks.modules.punishments.listeners.ChatListener;
import me.centralworks.modules.punishments.listeners.MuteQuitListener;
import me.centralworks.modules.punishments.listeners.RegisterAddressListener;
import me.centralworks.modules.punishments.listeners.withAddressIP.MuteIPChatListener;
import me.centralworks.modules.punishments.listeners.withAddressIP.join.MuteIPListener;
import me.centralworks.modules.punishments.listeners.withAddressIP.join.OfflineBanIPListener;
import me.centralworks.modules.punishments.listeners.withAddressIP.join.OnlineBanIPListener;
import me.centralworks.modules.punishments.listeners.withoutAddressIP.MuteChatListener;
import me.centralworks.modules.punishments.listeners.withoutAddressIP.join.MuteListener;
import me.centralworks.modules.punishments.listeners.withoutAddressIP.join.OfflineBanListener;
import me.centralworks.modules.punishments.listeners.withoutAddressIP.join.OnlineBanListener;
import me.centralworks.modules.punishments.models.supliers.Immune;
import me.centralworks.modules.punishments.models.supliers.Reason;
import me.centralworks.modules.punishments.models.supliers.cached.Reasons;
import me.centralworks.modules.punishments.models.supliers.enums.PunishmentType;
import me.centralworks.modules.punishments.models.supliers.warns.WarnLoader;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.function.Consumer;

public class PunishmentPlugin {

    protected static PunishmentPlugin instance;
    protected static Configuration configuration;
    protected static Configuration messages;
    protected static Configuration usages;
    protected static Configuration immune;
    protected static Gson gson;
    protected static Consumer<PunishmentPlugin> disable;

    public PunishmentPlugin() {
        setDisable(punishmentPlugin -> AddressIPDAO.getInstance().saveAll());
        registerInstances();
        registerCommands();
        registerListeners();
        registerReasons();
        registerData();
        new WarnLoader().init(getConfiguration());
        new Immune(getImmune().getStringList("Users"));
    }

    public static Configuration getImmune() {
        return immune;
    }

    public static void setImmune(Configuration immune) {
        PunishmentPlugin.immune = immune;
    }

    public static Consumer<PunishmentPlugin> getDisable() {
        return disable;
    }

    public static void setDisable(Consumer<PunishmentPlugin> disable) {
        PunishmentPlugin.disable = disable;
    }

    public static PunishmentPlugin getInstance() {
        return instance;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Configuration getMessages() {
        return messages;
    }

    public static Configuration getUsages() {
        return usages;
    }

    public static Gson getGson() {
        return gson;
    }

    protected void registerReasons() {
        final List<Reason> list = Lists.newArrayList();
        getConfiguration().getSection("Reasons").getKeys().forEach(s -> {
            final String path = "Reasons." + s + ".";
            final Reason pr = new Reason();
            pr.setReason(getConfiguration().getString(path + "reason"));
            pr.setPermission(getConfiguration().getString(path + "permission"));
            pr.setWithIP(getConfiguration().getBoolean(path + "ip"));
            pr.setPunishmentType(PunishmentType.valueOf(getConfiguration().getString(path + "type")));
            if (pr.getPunishmentType().isTemp()) {
                pr.setDuration(Date.getInstance().convertPunishmentDuration(Lists.newArrayList(getConfiguration().getString(path + "duration").split(","))));
            } else pr.setPermanent(true);
            list.add(pr);
        });
        Reasons.getInstance().setReasons(list);
    }

    protected void registerCommand(Command command) {
        Main.getInstance().getProxy().getPluginManager().registerCommand(Main.getInstance(), command);
    }

    protected void registerListener(Listener listener) {
        Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), listener);
    }

    protected void registerInstances() {
        instance = this;
        configuration = Main.getConfiguration("config.yml", "punishments", "/punishments/");
        messages = Main.getConfiguration("messages.yml", "punishments", "/punishments/");
        usages = Main.getConfiguration("usages.yml", "punishments", "/punishments/");
        immune = Main.getConfiguration("immune.yml", "punishments", "/punishments/");
        gson = new Gson();
    }

    protected void registerCommands() {
        registerCommand(new CmdBan());
        registerCommand(new CmdTempBan());
        registerCommand(new CmdMute());
        registerCommand(new CmdTempMute());
        registerCommand(new CmdTempBanIP());
        registerCommand(new CmdBanIP());
        registerCommand(new CmdMuteIP());
        registerCommand(new CmdTempMuteIP());
        registerCommand(new CmdUnban());
        registerCommand(new CmdUnmute());
        registerCommand(new CmdUnpunish());
        registerCommand(new CmdPunish());
        registerCommand(new CmdPunishView());
        registerCommand(new CmdHistory());
        registerCommand(new CmdKick());
        registerCommand(new CmdTempWarn());
        registerCommand(new CmdWarn());
        registerCommand(new CmdUnwarn());
        registerCommand(new CmdWarns());
    }

    protected void registerListeners() {
        registerListener(new ChatListener());
        registerListener(new MuteListener());
        registerListener(new MuteIPListener());
        registerListener(new MuteChatListener());
        registerListener(new MuteIPChatListener());
        registerListener(new RegisterAddressListener());
        registerListener(new MuteQuitListener());
        registerListener(Contexts.getInstance());
        if (Main.isOnlineMode()) {
            registerListener(new OnlineBanListener());
            registerListener(new OnlineBanIPListener());
        } else {
            registerListener(new OfflineBanListener());
            registerListener(new OfflineBanIPListener());
        }
    }

    protected void registerData() {
        PunishmentDAO.getInstance().createTable();
        final AddressIPDAO adr = AddressIPDAO.getInstance();
        adr.createTable();
        adr.loadAll();
        WarnDAO.getInstance().createTable();
    }
}
