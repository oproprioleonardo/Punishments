package me.centralworks.modules.reports.cmds;

import me.centralworks.Main;
import me.centralworks.modules.reports.enums.Permission;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
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
                //todo new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                //todo new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }

        } catch (Exception e) {
            //if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.report")).send(s);
            //else new Message(Main.getUsages().getString("Usages.report")).send(s);
        }
    }
}
