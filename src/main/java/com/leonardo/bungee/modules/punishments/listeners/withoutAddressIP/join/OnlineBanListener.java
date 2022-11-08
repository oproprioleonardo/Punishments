package com.leonardo.bungee.modules.punishments.listeners.withoutAddressIP.join;

import com.leonardo.bungee.lib.LongMessage;
import com.leonardo.bungee.modules.punishments.models.OnlinePunishment;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.modules.punishments.models.supliers.CheckUp;
import com.leonardo.bungee.modules.punishments.models.supliers.Filter;
import com.leonardo.bungee.modules.punishments.models.supliers.PlaceHolder;
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
        final Request request = new Request(onlinePunishment);
        if (request.existsPrimaryIdentifier()) {
            final List<Punishment> instance = request.requireAllByPrimaryIdentifier();
            final CheckUp checkUp = new CheckUp(instance);
            if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentBan()) return;
            final Punishment p = new Filter(instance).getAllBannedPActive().get(0);
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
