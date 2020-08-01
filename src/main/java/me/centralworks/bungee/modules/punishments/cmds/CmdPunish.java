package me.centralworks.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.lib.UUIDManager;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.supliers.Reason;
import me.centralworks.bungee.modules.punishments.models.supliers.SenderOptions;
import me.centralworks.bungee.modules.punishments.models.supliers.Service;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.AddressIP;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.Reasons;
import net.md_5.bungee.api.CommandSender;
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
            if (!Permission.hasPermission(s, Permission.STAFF)) return;
            final UUIDManager uuid = UUIDManager.get();
            final General lib = General.get();
            final Configuration cfg = PunishmentPlugin.getMessages();
            final Reasons reasons = Reasons.getInstance();
            final ProxiedPlayer p = ((ProxiedPlayer) s);
            if (args.length == 1) new SenderOptions(p, args[0]).sendPunishmentList();
            else if (args.length > 1) {
                final String reasonText = String.join(" ", Lists.newArrayList(args).subList(1, args.length));
                if (!reasons.exists(reasonText)) {
                    new Message(cfg.getString("Messages.reason-not-found")).send(p);
                    return;
                }
                if (uuid.getOriginalUUID(args[0]).equals("")) {
                    new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                    return;
                }
                final Reason reason = reasons.getByReason(reasonText);
                final Service punishment = new Service(reason.getPunishmentType());
                final String target = lib.identifierCompare(args[0], uuid.getOriginalUUID(args[0]));
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
