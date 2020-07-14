package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.supliers.Reason;
import me.centralworks.modules.punishments.models.punishs.supliers.Service;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.AddressIP;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.Reasons;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdPunish extends Command {

    public CmdPunish() {
        super("punir", "", "punish", "punishment");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!(s instanceof ProxiedPlayer)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            if (!Permission.hasPermission(s, Permission.STAFF)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final ProxyServer proxy = Main.getInstance().getProxy();
            final General lib = General.getGeneralLib();
            final Configuration cfg = PunishmentPlugin.getMessages();
            final Reasons reasons = Reasons.getInstance();
            final ProxiedPlayer p = ((ProxiedPlayer) s);
            if (args.length == 1) {
                final String target = args[0];
                lib.sendPunishments(p, target);
            } else if (args.length > 1) {
                final String reasonText = String.join(" ", Lists.newArrayList(args).subList(1, args.length));
                if (!reasons.exists(reasonText)) {
                    new Message(cfg.getString("Messages.reason-not-found")).send(p);
                    return;
                }
                final Reason reason = reasons.getByReason(reasonText);
                final Service punishment = new Service(reason.getPunishmentType());
                final String target = lib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? "" : proxy.getPlayer(args[0]).getUniqueId().toString());
                if (reason.getWithIP()) {
                    final AddressIP adr = AddressIP.getInstance();
                    if (!adr.existsPlayer(target)) {
                        new Message(PunishmentPlugin.getMessages().getString("Messages.ip-not-registered")).send(s);
                        return;
                    } else punishment.setIp(adr.getByAccount(target).getHostName());
                }
                if (!p.hasPermission(reason.getPermission()) && !p.hasPermission(Permission.ADMIN.getPermission())) {
                    new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                    return;
                }
                if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                    new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                    return;
                }
                punishment.setSecondaryIdentifier(args[0]);
                punishment.setPunisher(p.getName());
                punishment.setTarget(target);
                punishment.setReason(reason);
                punishment.setPermanent(reason.isPermanent());
                punishment.applyOtherInformation(p);
            } else new Message(PunishmentPlugin.getUsages().getString("Usages.punish")).send(s);
        } catch (Exception ignored) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punish")).send(s);
        }
    }
}
