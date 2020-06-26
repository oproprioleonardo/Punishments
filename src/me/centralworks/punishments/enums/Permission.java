package me.centralworks.punishments.enums;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Permission {

    ADMIN("punishments.admin"),
    STAFF("punishments.punir"),
    BAN("punishments.ban"),
    BANIP("punishments.ban.ip"),
    MUTE("punishments.mute"),
    TEMPBAN("punishments.tempban"),
    TEMPMUTE("punishments.tempmute");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public static boolean hasPermission(ProxiedPlayer proxiedPlayer, Permission permission) {
        return proxiedPlayer.hasPermission(permission.getPermission()) || proxiedPlayer.hasPermission(Permission.ADMIN.getPermission());
    }

    public static boolean hasPermission(CommandSender cs, Permission permission) {
        return cs.hasPermission(permission.getPermission()) || cs.hasPermission(Permission.ADMIN.getPermission());
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer.hasPermission(getPermission());
    }

}
