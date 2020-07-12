package me.centralworks.modules.reports.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.Contexts;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.enums.Permission;
import me.centralworks.modules.reports.models.supliers.Service;
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
            final General lib = General.getGeneralLib();
            if (!Permission.hasPermission(s, Permission.REPORT)) {
                new Message(ReportPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (!(s instanceof ProxiedPlayer)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(ReportPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final ProxiedPlayer p = (ProxiedPlayer) s;
            final String target = args[0];
            if (args.length == 1) lib.sendReportList(p, target);
            else {
                final Contexts contexts = Contexts.getInstance();
                if (contexts.exists(p.getName())) {
                    new Message("§cUma denúncia de cada vez, amigo.").send(p);
                    return;
                }
                final String reason = String.join(" ", Lists.newArrayList(args).subList(2, args.length));
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
