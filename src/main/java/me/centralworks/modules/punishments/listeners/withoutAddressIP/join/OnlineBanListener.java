package me.centralworks.modules.punishments.listeners.withoutAddressIP.join;

import me.centralworks.lib.General;
import me.centralworks.lib.LongMessage;
import me.centralworks.modules.punishments.models.punishs.OnlinePunishment;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.CheckUp;
import me.centralworks.modules.punishments.models.punishs.supliers.Filter;
import me.centralworks.modules.punishments.models.punishs.supliers.Request;
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
            final List<String> collect = General.getGeneralLib().applyPlaceHolders(longMessage.getStringList(), p);
            longMessage.setStringList(collect);
            final ComponentBuilder baseComponents = new ComponentBuilder("");
            longMessage.getColorfulList().forEach(baseComponents::append);
            e.setCancelled(true);
            e.setCancelReason(baseComponents.create());
        }
    }
}
