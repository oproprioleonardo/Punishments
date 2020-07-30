package me.centralworks.bungee.modules.punishments.listeners.withAddressIP.join;

import me.centralworks.bungee.lib.LongMessage;
import me.centralworks.bungee.modules.punishments.models.OnlinePunishment;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.CheckUp;
import me.centralworks.bungee.modules.punishments.models.supliers.Filter;
import me.centralworks.bungee.modules.punishments.models.supliers.PlaceHolder;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
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
        final Request request = new Request(onlinePunishment);
        if (request.existsPrimaryIdentifier()) {
            final List<Punishment> instance = request.requireAllByAddress();
            final CheckUp checkUp = new CheckUp(instance);
            if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentBan()) return;
            final Punishment p = new Filter(instance).getAllBannedPActive().get(0);
            if (p.getPrimaryIdentifier().equals(onlinePunishment.getPrimaryIdentifier())) return;
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
