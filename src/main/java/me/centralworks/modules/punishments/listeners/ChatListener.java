package me.centralworks.modules.punishments.listeners;

import me.centralworks.lib.Context;
import me.centralworks.lib.Contexts;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.models.punishs.supliers.Service;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
            final General generalLib = General.getGeneralLib();
            if (!contexts.exists(p.getName())) return;
            final Context context = contexts.get(p.getName());
            if (context.getPlugin() == 1) {
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
                if (!generalLib.isLink(message)) {
                    new Message(PunishmentPlugin.getMessages().getString("Messages.invalid-link")).send(p);
                    return;
                }
                new Message(PunishmentPlugin.getMessages().getString("Messages.next-link")).send(p);
                service.attachEvidence(message);
            }
        }
    }
}
