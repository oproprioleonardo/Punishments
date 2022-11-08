package com.leonardo.bungee.modules.reports.cmds;

import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.reports.dao.ReportDAO;
import com.leonardo.bungee.modules.reports.enums.Permission;
import com.leonardo.bungee.modules.reports.inventories.ReportInfo;
import com.leonardo.bungee.modules.reports.inventories.ReportsMain;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.reports.ReportPlugin;
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
