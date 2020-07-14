package me.centralworks.modules.punishments.listeners.withoutAddressIP.join;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.CheckUp;
import me.centralworks.modules.punishments.models.punishs.supliers.Filter;
import me.centralworks.modules.punishments.models.punishs.supliers.Request;
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
                final Punishment punishment = General.getGeneralLib().easyInstance(connection.getName(), connection.getUniqueId().toString());
                final Request request = new Request(punishment);
                if (request.existsPrimaryIdentifier()) {
                    final General generalLib = General.getGeneralLib();
                    final List<Punishment> instance = request.requireAllByPrimaryIdentifier();
                    final CheckUp checkUp = new CheckUp(instance);
                    if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentMute()) return;
                    final Punishment p = new Filter(instance).getAllMutedPActive().get(0);
                    generalLib.getFunctionMuteIfOn().accept(p);
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

}
