package com.leonardo.bungee.modules.reports.cmds;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.reports.enums.Permission;
import com.leonardo.bungee.modules.reports.models.supliers.Delay;
import com.leonardo.bungee.modules.reports.models.supliers.Service;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Contexts;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.models.supliers.SenderOptions;
import com.leonardo.bungee.modules.reports.ReportPlugin;
import com.leonardo.bungee.modules.reports.dao.ReportDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdReport extends Command {

    public CmdReport() {
        super("report", "", "reportar", "denunciar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.REPORT)) return;
            if (!(s instanceof ProxiedPlayer)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(ReportPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            if (Delay.getInstance().exists(s.getName())) {
                new Message(ReportPlugin.getMessages().getString("Messages.delay")).send(s);
                return;
            }
            final ProxiedPlayer p = (ProxiedPlayer) s;
            final String target = args[0];
            if (args.length == 1) new SenderOptions(p, target).sendReportList();
            else {
                final Contexts contexts = Contexts.getInstance();
                if (contexts.exists(p.getName())) {
                    new Message("§cUma coisa de cada vez, amigo.").send(p);
                    return;
                }
                if (ReportDAO.getInstance().exists(target, p.getName())) {
                    new Message(ReportPlugin.getMessages().getString("Messages.again")).send(p);
                    return;
                }
                final String reason = String.join(" ", Lists.newArrayList(args).subList(1, args.length));
                final Service service = new Service();
                service.setTarget(target);
                service.setVictim(p.getName());
                service.setReason(reason);
                service.applyOtherInformation(p);
            }
        } catch (Exception e) {
            new Message(ReportPlugin.getUsages().getString("Usages.report")).send(s);
        }
    }
}
