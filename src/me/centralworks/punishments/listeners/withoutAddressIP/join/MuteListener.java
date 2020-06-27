package me.centralworks.punishments.listeners.withoutAddressIP.join;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteListener implements Listener {

    @EventHandler
    public void js(PostLoginEvent e) {
        BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final ProxiedPlayer connection = e.getPlayer();
                final Punishment punishment = General.getGeneralLib().easyInstance(connection.getName(), connection.getUniqueId().toString());
                if (punishment.exists()) {
                    final General generalLib = General.getGeneralLib();
                    final List<Punishment> instance = punishment.requireAll();
                    if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentMute(instance)) return;
                    final Punishment p = generalLib.getAllMuteP(instance).get(0);
                    generalLib.getFunctionMuteIfOn().accept(p);
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

}
