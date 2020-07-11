package me.centralworks.modules.reports;

import com.google.gson.Gson;
import me.centralworks.Main;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class ReportPlugin {

    protected static ReportPlugin instance;
    protected static Configuration configuration;
    protected static Configuration usages;
    protected static Configuration messages;
    protected static List<String> reasons;
    protected static Gson gson;

    public ReportPlugin() {
        instance = this;
        configuration = Main.getConfiguration("config.yml", "reports", "/reports/");
        messages = Main.getConfiguration("messages.yml", "reports", "/reports/");
        usages = Main.getConfiguration("usages.yml", "reports", "/reports/");
        gson = new Gson();
        reasons = getConfiguration().getStringList("Reasons");
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
        return gson;
    }
}
