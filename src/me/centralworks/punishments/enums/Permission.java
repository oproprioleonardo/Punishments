package me.centralworks.punishments.enums;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Permission {

    ADMIN("punishments.admin"),
    STAFF("punishments.punir"),
    BAN("punishments.ban"),
    BANIP("punishments.ban.ip"),
    TEMPBANIP("punishments.tempban.ip"),
    TEMPBAN("punishments.tempban"),
    MUTE("punishments.mute"),
    MUTEIP("punishments.mute.ip"),
    TEMPMUTEIP("punishments.tempmute.ip"),
    TEMPMUTE("punishments.tempmute"),
    UNBAN("punishments.unban"),
    UNMUTE("punishments.unmute"),
    UNPUNISH("punishments.unpunish"),
    PUNISHVIEW("punishments.view"),
    PUNISHHISTORY("punishments.history"),
    KICK("punishments.kick"),
    WARN("punishments.warn"),
    TEMPWARN("punishments.tempwarn"),
    UNWARN("punishments.unwarn"),
    WARNS("punishments.warns");

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
