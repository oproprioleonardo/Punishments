package me.centralworks.modules.reports.cmds;

import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.dao.ReportDAO;
import me.centralworks.modules.reports.enums.Permission;
import me.centralworks.modules.reports.inventories.ReportInfo;
import me.centralworks.modules.reports.inventories.ReportsMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdReports extends Command {

    public CmdReports() {
        super("reports", "", "reportes");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!Permission.hasPermission(s, Permission.REPORTS)) return;
            if (!(s instanceof ProxiedPlayer)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            final ProxiedPlayer p = (ProxiedPlayer) s;
            final ReportDAO dao = ReportDAO.getInstance();
            if (args.length == 1 && dao.exists(args[0])) new ReportInfo(p, dao.loadByUser(args[0])).open();
            else new ReportsMain(p).open();
        } catch (Exception e) {
            new Message(ReportPlugin.getUsages().getString("Usages.reports")).send(s);
        }
    }
}
