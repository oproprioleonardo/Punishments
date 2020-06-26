package me.centralworks.punishments.listeners;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.LongMessage;
import me.centralworks.punishments.punishs.supliers.cached.MutedPlayers;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class MuteChatListener implements Listener {

    @EventHandler
    public void talk(ChatEvent e){
        final Connection sender = e.getSender();
        if (e.isCommand()) return;
        if (e.isProxyCommand()) return;
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            final MutedPlayers instance = MutedPlayers.getInstance();
            final String identifier = Main.isOnlineMode() ? p.getUniqueId().toString() : p.getName();
            instance.update(identifier);
            if (instance.exists(identifier)){
                final MutedPlayers.MuteObject muteObject = instance.get(identifier);
                final LongMessage longMessage = new LongMessage("Runners.mute-status");
                longMessage.getColorfulList().stream().map(s -> s
                        .replace("{finishAt}", muteObject.getFinishAt() == 0L ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getFinishAt()))
                        .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(muteObject.getStartAt()))
                        .replace("{id}", muteObject.getId().toString())
                ).map(TextComponent::fromLegacyText).collect(Collectors.toList()).forEach(p::sendMessage);
                e.setCancelled(true);
            }
        }
    }
}
