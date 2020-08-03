package me.centralworks.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.bungee.lib.Functionalities;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.lib.UUIDManager;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.supliers.Reason;
import me.centralworks.bungee.modules.punishments.models.supliers.Service;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.Reasons;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdBan extends Command {

    public CmdBan() {
        super("ban", "", "banir");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final UUIDManager uuid = UUIDManager.get();
            if (!Permission.hasPermission(s, Permission.BAN)) return;
            if (uuid.getOriginalUUID(args[0]).equals("")) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service ban = new Service(PunishmentType.BAN);
            final Functionalities generalLib = Functionalities.get();
            final String target = generalLib.identifierCompare(args[0], uuid.getOriginalUUID(args[0]));
            ban.setPunisher(punisher);
            ban.setPermanent(true);
            ban.setTarget(target);
            ban.setSecondaryIdentifier(args[0]);
            if (s instanceof ProxiedPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(1, args.length);
                final Reason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                ban.setReason(reasonObj);
                ban.applyOtherInformation(p);
            } else {
                if (!(args.length == 2)) {
                    final List<String> reason = Arrays.asList(args).subList(1, args.length);
                    ban.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                } else {
                    List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(1, 2).get(0).split(","));
                    final List<String> reason = Arrays.asList(args).subList(2, args.length);
                    ban.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                    ban.setEvidences(evidences);
                }
                ban.run();
            }
        } catch (Exception e) {
            if (s instanceof ProxiedPlayer)
                new Message(PunishmentPlugin.getUsages().getString("Usages.banPlayer")).send(s);
            else new Message(PunishmentPlugin.getUsages().getString("Usages.banConsole")).send(s);
        }
    }
}
