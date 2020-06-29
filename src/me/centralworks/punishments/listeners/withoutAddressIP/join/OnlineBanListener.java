package me.centralworks.punishments.listeners.withoutAddressIP.join;

import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.LongMessage;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class OnlineBanListener implements Listener {

    @EventHandler
    public void join(LoginEvent e) {
        final PendingConnection connection = e.getConnection();
        final OnlinePunishment onlinePunishment = new OnlinePunishment();
        onlinePunishment.setPrimaryIdentifier(connection.getUniqueId().toString());
        if (onlinePunishment.existsPrimaryIdentifier()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = onlinePunishment.requireAllByPrimaryIdentifier();
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
