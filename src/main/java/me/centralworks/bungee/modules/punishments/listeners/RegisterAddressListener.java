package me.centralworks.bungee.modules.punishments.listeners;

import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.AddressIP;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RegisterAddressListener implements Listener {

    @EventHandler
    public void registerIP(PostLoginEvent e) {
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final ProxiedPlayer p = e.getPlayer();
                final AddressIP adr = AddressIP.getInstance();
                final General generalLib = General.getGeneralLib();
                final String identifier = generalLib.easyInstance(p.getName(), p.getUniqueId().toString()).getPrimaryIdentifier();
                final String hostAddress = p.getAddress().getAddress().getHostAddress();
                if (adr.existsIPAndAccount(hostAddress, identifier)) {
                    final AddressIP.AddressIPObject var = adr.getByAddressAndAccount(hostAddress, identifier);
                    var.setLastUsage(System.currentTimeMillis());
                } else if (adr.existsIP(hostAddress) && !adr.existsPlayer(identifier)) {
                    final AddressIP.AddressIPObject var = adr.getByAddress(hostAddress);
                    var.update();
                    var.add(identifier);
                } else if (adr.existsIP(hostAddress) && adr.existsPlayer(identifier)) {
                    adr.getByAccount(identifier).remove(identifier);
                    final AddressIP.AddressIPObject var = adr.getByAddress(hostAddress);
                    var.update();
                    var.add(identifier);
                } else if (!adr.existsIP(hostAddress) && adr.existsPlayer(identifier)) {
                    adr.getByAccount(identifier).remove(identifier);
                    final AddressIP.AddressIPObject var = new AddressIP.AddressIPObject();
                    var.setHostName(hostAddress);
                    var.add(identifier);
                    adr.add(var);
                } else {
                    final AddressIP.AddressIPObject var = new AddressIP.AddressIPObject();
                    var.setHostName(hostAddress);
                    var.add(identifier);
                    adr.add(var);
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

    @EventHandler
    public void quit(PlayerDisconnectEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        AddressIP.getInstance().getByAddress(p.getAddress().getAddress().getHostAddress()).update();
    }

}
