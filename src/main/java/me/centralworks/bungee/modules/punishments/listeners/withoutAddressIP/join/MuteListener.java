package me.centralworks.bungee.modules.punishments.listeners.withoutAddressIP.join;

import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.CheckUp;
import me.centralworks.bungee.modules.punishments.models.supliers.Filter;
import me.centralworks.bungee.modules.punishments.models.supliers.PFunctions;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteListener implements Listener {

    @EventHandler
    public void js(PostLoginEvent e) {
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final ProxiedPlayer connection = e.getPlayer();
                final Punishment punishment = General.get().easyInstance(connection.getName(), connection.getUniqueId().toString());
                final Request request = new Request(punishment);
                if (request.existsPrimaryIdentifier()) {
                    final List<Punishment> instance = request.requireAllByPrimaryIdentifier();
                    final CheckUp checkUp = new CheckUp(instance);
                    if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentMute()) return;
                    final Punishment p = new Filter(instance).getAllMutedPActive().get(0);
                    PFunctions.get().getFunctionMuteIfOn().accept(p);
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

}
