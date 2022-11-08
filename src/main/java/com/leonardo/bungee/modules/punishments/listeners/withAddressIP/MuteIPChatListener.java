package com.leonardo.bungee.modules.punishments.listeners.withAddressIP;

import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.LongMessage;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.MutedPlayers;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class MuteIPChatListener implements Listener {

    @EventHandler
    public void talk(ChatEvent e) {
        if (e.isCommand()) return;
        if (e.isProxyCommand()) return;
        final Connection sender = e.getSender();
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            final MutedPlayers instance = MutedPlayers.getInstance();
            final String identifier = Main.isOnlineMode() ? p.getUniqueId().toString() : p.getName();
            final String addressIP = p.getAddress().getAddress().getHostAddress();
            instance.updateByAddress(addressIP);
            if (instance.existsByAddress(addressIP)) {
                final MutedPlayers.MuteObject muteObject = instance.getByAddress(addressIP);
                if (muteObject.getPlayerMuted().equalsIgnoreCase(identifier)) return;
                final ComponentBuilder componentBuilder = new ComponentBuilder("");
                new LongMessage("Runners.mute-status").getColorfulList().stream().map(s -> s
                        .replace("{finishAt}", muteObject.isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getFinishAt()))
                        .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getStartAt()))
                        .replace("{id}", muteObject.getId().toString())
                ).forEach(componentBuilder::append);
                p.sendMessage(componentBuilder.create());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cmd(ChatEvent e) {
        if (!e.isCommand()) return;
        if (e.isProxyCommand()) return;
        final Connection sender = e.getSender();
        if (sender instanceof ProxiedPlayer) {
            if (PunishmentPlugin.getConfiguration().getStringList("Commands-locked-when-silenced").stream().noneMatch(s -> s.equalsIgnoreCase(e.getMessage().split(" ")[0])))
                return;
            final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            final MutedPlayers instance = MutedPlayers.getInstance();
            final String identifier = Main.isOnlineMode() ? p.getUniqueId().toString() : p.getName();
            final String addressIP = p.getAddress().getAddress().getHostAddress();
            instance.updateByAddress(addressIP);
            if (instance.existsByAddress(addressIP)) {
                final MutedPlayers.MuteObject muteObject = instance.getByAddress(addressIP);
                if (muteObject.getPlayerMuted().equalsIgnoreCase(identifier)) return;
                final ComponentBuilder componentBuilder = new ComponentBuilder("");
                new LongMessage("Runners.mute-status").getColorfulList().stream().map(s -> s
                        .replace("{finishAt}", muteObject.isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getFinishAt()))
                        .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getStartAt()))
                        .replace("{id}", muteObject.getId().toString())
                ).forEach(componentBuilder::append);
                p.sendMessage(componentBuilder.create());
                e.setCancelled(true);
            }
        }
    }
}
