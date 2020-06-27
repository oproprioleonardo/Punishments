package me.centralworks.punishments.listeners;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.punishs.supliers.cached.AddressIP;
import me.centralworks.punishments.punishs.supliers.cached.MutedPlayers;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MuteQuitListener implements Listener {

    @EventHandler
    public void quit(PlayerDisconnectEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        final String identifier = General.getGeneralLib().identifierCompare(p.getName(), p.getUniqueId().toString());
        final MutedPlayers mp = MutedPlayers.getInstance();
        final String adr = p.getAddress().getAddress().getHostAddress();
        if (mp.exists(identifier)) mp.remove(identifier);
        else if (mp.existsByAddress(adr)) {
            final MutedPlayers.MuteObject mo = mp.getByAddress(adr);
            if (AddressIP.getInstance().getByAddress(adr).getAccounts().stream().filter(s -> Main.getInstance().getProxy().getPlayer(s) != null).count() < 2)
                mp.remove(mo);
        }
    }

}
