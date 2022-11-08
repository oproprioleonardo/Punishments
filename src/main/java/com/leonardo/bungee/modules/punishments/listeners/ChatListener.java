package com.leonardo.bungee.modules.punishments.listeners;

import com.google.common.collect.Lists;
import com.leonardo.bungee.lib.Context;
import com.leonardo.bungee.lib.Contexts;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.Service;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class ChatListener implements Listener {

    @EventHandler
    public void chat(ChatEvent e) {
        final Connection sender = e.getSender();
        if (e.isCommand()) return;
        if (e.isProxyCommand()) return;
        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            final Contexts contexts = Contexts.getInstance();
            final String message = e.getMessage();
            if (!contexts.exists(p.getName())) return;
            final Context context = contexts.get(p.getName());
            if (context.getPlugin() == 0) {
                final Service service = (Service) context;
                e.setCancelled(true);
                if (message.equalsIgnoreCase("pronto")) {
                    contexts.remove(p.getName());
                    service.run();
                    return;
                }
                if (message.equalsIgnoreCase("cancelar")) {
                    contexts.remove(p.getName());
                    new Message(PunishmentPlugin.getMessages().getString("Messages.operation-cancel")).send(p);
                    return;
                }
                final List<String> evidences = Lists.newArrayList(message.split(" "));
                if (evidences.stream().anyMatch(s -> !Functionalities.isNotLink(s))) {
                    new Message(PunishmentPlugin.getMessages().getString("Messages.invalid-link")).send(p);
                    return;
                }
                new Message(PunishmentPlugin.getMessages().getString("Messages.next-link")).send(p);
                evidences.forEach(service::attachEvidence);
            }
        }
    }
}
