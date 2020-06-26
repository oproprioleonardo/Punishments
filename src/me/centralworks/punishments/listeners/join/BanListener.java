package me.centralworks.punishments.listeners.join;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.LongMessage;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BanListener implements Listener {

    @EventHandler
    public void join(LoginEvent e) {
        final PendingConnection connection = e.getConnection();
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
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentBan(instance)) return;
            final Punishment p = generalLib.getAllBannedP(instance).get(0);
            final LongMessage longMessage = new LongMessage("Runners.ban-kick");
            final List<String> collect = General.getGeneralLib().applyPlaceHolders(longMessage.getStringList(), p);
            longMessage.setStringList(collect);
            final ComponentBuilder baseComponents = new ComponentBuilder("");
            longMessage.getColorfulList().forEach(baseComponents::append);
            e.setCancelled(true);
            e.setCancelReason(baseComponents.create());
        }
    }

}
