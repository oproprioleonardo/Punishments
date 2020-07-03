package me.centralworks.punishments.listeners.withAddressIP.join;

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

public class OnlineBanIPListener implements Listener {

    @EventHandler
    public void join(LoginEvent e) {
        final PendingConnection connection = e.getConnection();
        final OnlinePunishment onlinePunishment = new OnlinePunishment();
        onlinePunishment.setPrimaryIdentifier(connection.getUniqueId().toString());
        onlinePunishment.setIp(connection.getAddress().getAddress().getHostAddress());
        if (onlinePunishment.existsPrimaryIdentifier()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = onlinePunishment.requireAllByAddress();
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentBan(instance)) return;
            final Punishment p = generalLib.getAllBannedPActive(instance).get(0);
            if (p.getPrimaryIdentifier().equals(onlinePunishment.getPrimaryIdentifier())) return;
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
