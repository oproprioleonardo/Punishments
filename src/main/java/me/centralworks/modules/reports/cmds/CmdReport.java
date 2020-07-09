package me.centralworks.modules.reports.cmds;

import me.centralworks.Main;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.enums.Permission;
import me.centralworks.modules.reports.models.supliers.Context;
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
            final Context context = new Context();
            final String target = args[0];

        } catch (Exception e) {
            new Message(ReportPlugin.getUsages().getString("Usages.report")).send(s);
        }
    }
}
