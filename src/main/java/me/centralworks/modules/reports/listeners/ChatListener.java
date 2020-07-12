package me.centralworks.modules.reports.listeners;

import me.centralworks.lib.Contexts;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.models.supliers.Service;
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
            final Service service = (Service) contexts.get(p.getName());
            final General generalLib = General.getGeneralLib();
            if (!contexts.exists(p.getName())) return;
            e.setCancelled(true);
            if (message.equalsIgnoreCase("pronto")) {
                contexts.remove(p.getName());
                service.run();
                return;
            }
            if (message.equalsIgnoreCase("cancelar")) {
                contexts.remove(p.getName());
                new Message(ReportPlugin.getMessages().getString("Messages.operation-cancel")).send(p);
                return;
            }
            if (!generalLib.isLink(message)) {
                new Message(ReportPlugin.getMessages().getString("Messages.invalid-link")).send(p);
                return;
            }
            new Message(ReportPlugin.getMessages().getString("Messages.next-link")).send(p);
            service.attachEvidence(message);
        }
    }

}
