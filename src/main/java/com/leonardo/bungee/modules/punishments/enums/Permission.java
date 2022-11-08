package com.leonardo.bungee.modules.punishments.enums;

import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
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
    WARNS("punishments.warns"),
    CHECK("punishments.check");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public static boolean hasPermission(ProxiedPlayer proxiedPlayer, Permission permission) {
        if (permission.hasPermission(proxiedPlayer) || proxiedPlayer.hasPermission(Permission.ADMIN.getPermission()))
            return true;
        new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(proxiedPlayer);
        return false;
    }

    public static boolean hasPermission(CommandSender cs, Permission permission) {
        if (permission.hasPermission(cs) || cs.hasPermission(Permission.ADMIN.getPermission())) return true;
        new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(cs);
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
