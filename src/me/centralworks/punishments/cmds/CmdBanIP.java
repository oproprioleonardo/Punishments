package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import me.centralworks.punishments.punishs.supliers.runners.types.Ban;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdBanIP extends Command {

    public CmdBanIP() {
        super("banip", Permission.BANIP.getPermission(), "banirip", "ipban");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final ProxyServer proxy = Main.getInstance().getProxy();
        if (!Permission.hasPermission(s, Permission.BANIP)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
        final Ban ban = new Ban();
        final General generalLib = General.getGeneralLib();
        ban.setPunishmentType(PunishmentType.BAN);
        ban.setPunisher(punisher);
        if (args.length > 1) {
            if (s instanceof ProxiedPlayer) {
                if (generalLib.isAddressIP(args[0])) {
                }
            } else {

            }
        } else {
            if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.banPlayer")).send(s);
            else new Message(Main.getUsages().getString("Usages.banConsole")).send(s);
        }
    }
}
