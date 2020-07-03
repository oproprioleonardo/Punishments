package me.centralworks.punishments.listeners.withAddressIP.join;

import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.LongMessage;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class OfflineBanIPListener implements Listener {

    @EventHandler
    public void join(PreLoginEvent e) {
        final PendingConnection connection = e.getConnection();
        final OfflinePunishment offlinePunishment = new OfflinePunishment();
        offlinePunishment.setPrimaryIdentifier(connection.getName());
        offlinePunishment.setIp(connection.getAddress().getAddress().getHostAddress());
        if (offlinePunishment.existsByAddress()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = offlinePunishment.requireAllByAddress();
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentBan(instance)) return;
            final Punishment p = generalLib.getAllBannedPActive(instance).get(0);
            if (p.getPrimaryIdentifier().equals(offlinePunishment.getPrimaryIdentifier())) return;
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
