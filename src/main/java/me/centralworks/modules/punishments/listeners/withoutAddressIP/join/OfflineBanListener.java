package me.centralworks.modules.punishments.listeners.withoutAddressIP.join;

import me.centralworks.lib.General;
import me.centralworks.lib.LongMessage;
import me.centralworks.modules.punishments.models.punishs.OfflinePunishment;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class OfflineBanListener implements Listener {

    @EventHandler
    public void join(PreLoginEvent e) {
        final PendingConnection connection = e.getConnection();
        final OfflinePunishment offlinePunishment = new OfflinePunishment();
        offlinePunishment.setPrimaryIdentifier(connection.getName());
        if (offlinePunishment.existsPrimaryIdentifier()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = offlinePunishment.requireAllByPrimaryIdentifier();
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentBan(instance)) return;
            final Punishment p = generalLib.getAllBannedPActive(instance).get(0);
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
