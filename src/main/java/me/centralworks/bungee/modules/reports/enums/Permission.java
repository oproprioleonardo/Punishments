package me.centralworks.bungee.modules.reports.enums;

import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.reports.ReportPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Permission {

    ADMIN("punishments.admin"),
    REPORT("punishments.report"),
    REPORTS("punishments.reports");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }


    public static boolean hasPermission(CommandSender cs, Permission permission) {
        if (permission.hasPermission(cs) || cs.hasPermission(Permission.ADMIN.getPermission())) return true;
        new Message(ReportPlugin.getMessages().getString("Messages.permission-error")).send(cs);
        return false;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer.hasPermission(getPermission());
    }

    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission(getPermission());
    }

}
