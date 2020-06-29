package me.centralworks.punishments.listeners;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import me.centralworks.punishments.punishs.supliers.runners.Run;
import me.centralworks.punishments.punishs.supliers.runners.Task;
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
            final Task task = Task.getInstance();
            final String message = e.getMessage();
            final Run run = task.get(p.getName());
            final General generalLib = General.getGeneralLib();
            if (!task.exists(p.getName())) return;
            e.setCancelled(true);
            if (message.equalsIgnoreCase("pronto")) {
                task.remove(p.getName());
                if (run.getPunishmentType() == PunishmentType.BAN || run.getPunishmentType() == PunishmentType.TEMPBAN) {
                    run.setFunctionIfOnline(generalLib.getFunctionBanIfOn());
                    run.setAnnouncer(generalLib.getFunctionAnnouncerBan());
                } else {
                    run.setFunctionIfOnline(generalLib.getFunctionMuteIfOn());
                    run.setAnnouncer(generalLib.getFunctionAnnouncerMute());
                }
                run.run();
                return;
            }
            if (message.equalsIgnoreCase("cancelar")) {
                task.remove(p.getName());
                new Message(Main.getMessages().getString("Messages.operation-cancel")).send(p);
                return;
            }
            if (!generalLib.isLink(message)) {
                new Message(Main.getMessages().getString("Messages.invalid-link")).send(p);
                return;
            }
            new Message(Main.getMessages().getString("Messages.next-link")).send(p);
            run.attachEvidence(message);
        }
    }
}
