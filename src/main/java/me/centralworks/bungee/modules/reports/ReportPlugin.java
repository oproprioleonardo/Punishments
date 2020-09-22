package me.centralworks.bungee.modules.reports;

import com.google.gson.Gson;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.modules.reports.cmds.CmdReport;
import me.centralworks.bungee.modules.reports.cmds.CmdReports;
import me.centralworks.bungee.modules.reports.dao.ReportDAO;
import me.centralworks.bungee.modules.reports.listeners.ChatListener;
import me.centralworks.bungee.modules.reports.models.supliers.ToggleAnnouncement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.function.Consumer;

public class ReportPlugin {

    private static ReportPlugin instance;
    private static Configuration configuration;
    private static Configuration usages;
    private static Configuration messages;
    private static List<String> reasons;
    private static Consumer<ReportPlugin> disable;

    public ReportPlugin() {
        instance = this;
        configuration = Main.getConfiguration("config.yml", "reports", "/reports/");
        messages = Main.getConfiguration("messages.yml", "reports", "/reports/");
        usages = Main.getConfiguration("usages.yml", "reports", "/reports/");
        reasons = getConfiguration().getStringList("Reasons");
        registerCommand(new CmdReport());
        registerCommand(new CmdReports());
        registerListener(new ChatListener());
        registerListener(ToggleAnnouncement.getInstance());
        ReportDAO.getInstance().createTable();
        setDisable(reportPlugin -> ReportDAO.getInstance().clear());
    }

    public static Consumer<ReportPlugin> getDisable() {
        return disable;
    }

    public static void setDisable(Consumer<ReportPlugin> disable) {
        ReportPlugin.disable = disable;
    }

    public static List<String> getReasons() {
        return reasons;
    }

    public static ReportPlugin getInstance() {
        return instance;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Configuration getUsages() {
        return usages;
    }

    public static Configuration getMessages() {
        return messages;
    }

    public static Gson getGson() {
        return Main.getGson();
    }

    private void registerCommand(Command command) {
        Main.getInstance().getProxy().getPluginManager().registerCommand(Main.getInstance(), command);
    }

    private void registerListener(Listener listener) {
        Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), listener);
    }

    public ProxyServer getProxy() {
        return Main.getInstance().getProxy();
    }
}
