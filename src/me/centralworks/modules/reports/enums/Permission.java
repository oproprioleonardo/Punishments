package me.centralworks.modules.reports.enums;

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

    public static boolean hasPermission(ProxiedPlayer proxiedPlayer, Permission permission) {
        return permission.hasPermission(proxiedPlayer) || proxiedPlayer.hasPermission(Permission.ADMIN.getPermission());
    }

    public static boolean hasPermission(CommandSender cs, Permission permission) {
        return permission.hasPermission(cs) || cs.hasPermission(Permission.ADMIN.getPermission());
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
