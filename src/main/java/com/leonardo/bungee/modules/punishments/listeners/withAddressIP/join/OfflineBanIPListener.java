package com.leonardo.bungee.modules.punishments.listeners.withAddressIP.join;

import com.leonardo.bungee.lib.LongMessage;
import com.leonardo.bungee.modules.punishments.models.OfflinePunishment;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.modules.punishments.models.supliers.CheckUp;
import com.leonardo.bungee.modules.punishments.models.supliers.Filter;
import com.leonardo.bungee.modules.punishments.models.supliers.PlaceHolder;
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
        final Request request = new Request(offlinePunishment);
        if (request.existsByAddress()) {
            final List<Punishment> instance = request.requireAllByAddress();
            final CheckUp checkUp = new CheckUp(instance);
            if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentBan()) return;
            final Punishment p = new Filter(instance).getAllBannedPActive().get(0);
            if (p.getPrimaryIdentifier().equals(offlinePunishment.getPrimaryIdentifier())) return;
            final LongMessage longMessage = new LongMessage("Runners.ban-kick");
            final List<String> collect = new PlaceHolder(longMessage.getStringList(), p).applyPlaceHolders();
            longMessage.setStringList(collect);
            final ComponentBuilder baseComponents = new ComponentBuilder("");
            longMessage.getColorfulList().forEach(baseComponents::append);
            e.setCancelled(true);
            e.setCancelReason(baseComponents.create());
        }
    }
}
