package com.leonardo.bungee.modules.punishments;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.leonardo.bungee.modules.punishments.cmds.*;
import com.leonardo.bungee.modules.punishments.dao.AddressIPDAO;
import com.leonardo.bungee.modules.punishments.dao.PunishmentDAO;
import com.leonardo.bungee.modules.punishments.dao.WarnDAO;
import com.leonardo.bungee.modules.punishments.models.supliers.Immune;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Contexts;
import com.leonardo.bungee.lib.Date;
import com.leonardo.bungee.modules.punishments.listeners.ChatListener;
import com.leonardo.bungee.modules.punishments.listeners.MuteQuitListener;
import com.leonardo.bungee.modules.punishments.listeners.RegisterAddressListener;
import com.leonardo.bungee.modules.punishments.listeners.withAddressIP.MuteIPChatListener;
import com.leonardo.bungee.modules.punishments.listeners.withAddressIP.join.MuteIPListener;
import com.leonardo.bungee.modules.punishments.listeners.withAddressIP.join.OfflineBanIPListener;
import com.leonardo.bungee.modules.punishments.listeners.withAddressIP.join.OnlineBanIPListener;
import com.leonardo.bungee.modules.punishments.listeners.withoutAddressIP.MuteChatListener;
import com.leonardo.bungee.modules.punishments.listeners.withoutAddressIP.join.MuteListener;
import com.leonardo.bungee.modules.punishments.listeners.withoutAddressIP.join.OfflineBanListener;
import com.leonardo.bungee.modules.punishments.listeners.withoutAddressIP.join.OnlineBanListener;
import com.leonardo.bungee.modules.punishments.models.supliers.Reason;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.Reasons;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import com.leonardo.bungee.modules.punishments.models.supliers.warns.WarnLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.function.Consumer;

public class PunishmentPlugin {

    private static PunishmentPlugin instance;
    private static Configuration configuration;
    private static Configuration messages;
    private static Configuration usages;
    private static Configuration immune;
    private static Consumer<PunishmentPlugin> disable;

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
        return Main.getGson();
    }

    private void registerReasons() {
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

    private void registerCommand(Command command) {
        Main.getInstance().getProxy().getPluginManager().registerCommand(Main.getInstance(), command);
    }

    private void registerListener(Listener listener) {
        Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), listener);
    }

    private void registerInstances() {
        instance = this;
        configuration = Main.getConfiguration("config.yml", "punishments", "/punishments/");
        messages = Main.getConfiguration("messages.yml", "punishments", "/punishments/");
        usages = Main.getConfiguration("usages.yml", "punishments", "/punishments/");
        immune = Main.getConfiguration("immune.yml", "punishments", "/punishments/");
    }

    private void registerCommands() {
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
        registerCommand(new CmdCheck());
    }

    private void registerListeners() {
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

    private void registerData() {
        PunishmentDAO.getInstance().createTable();
        final AddressIPDAO adr = AddressIPDAO.getInstance();
        adr.createTable();
        adr.loadAll();
        WarnDAO.getInstance().createTable();
    }

    public ProxyServer getProxy() {
        return Main.getInstance().getProxy();
    }
}
