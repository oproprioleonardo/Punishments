package me.centralworks.punishments.listeners.join;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteListener implements Listener {

    @EventHandler
    public void js(PostLoginEvent e){
        final ProxiedPlayer connection = e.getPlayer();
        final Punishment punishment;
        if (Main.isOnlineMode()) {
            final OnlinePunishment onlinePunishment = new OnlinePunishment();
            onlinePunishment.setIdentifier(connection.getUniqueId().toString());
            punishment = onlinePunishment;
        } else {
            final OfflinePunishment offlinePunishment = new OfflinePunishment();
            offlinePunishment.setIdentifier(connection.getName());
            punishment = offlinePunishment;
        }
        if (punishment.exists()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = punishment.requireAll();
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentMute(instance)) return;
            final Punishment p = generalLib.getAllMuteP(instance).get(0);
            generalLib.getFunctionMuteIfOn().accept(p);
        }
    }

}