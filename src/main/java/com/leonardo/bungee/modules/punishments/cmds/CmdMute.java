package com.leonardo.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.lib.UUIDManager;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.Reason;
import com.leonardo.bungee.modules.punishments.models.supliers.Service;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.Reasons;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdMute extends Command {

    public CmdMute() {
        super("mute", "", "silenciar", "mutar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final UUIDManager uuid = UUIDManager.get();
        final boolean isPlayer = s instanceof ProxiedPlayer;
        try {
            if (!Permission.hasPermission(s, Permission.MUTE)) return;
            if (uuid.getOriginalUUID(args[0]).equals("")) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service mute = new Service(PunishmentType.MUTE);
            final String target = Functionalities.identifierCompare(args[0], uuid.getOriginalUUID(args[0]));
            mute.setPunisher(punisher);
            mute.setPermanent(true);
            mute.setTarget(target);
            mute.setSecondaryIdentifier(args[0]);
            if (isPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(1, args.length);
                final Reason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                mute.setReason(reasonObj);
                mute.applyOtherInformation(p);
            } else {
                if (!(args.length == 2)) {
                    final List<String> reason = Arrays.asList(args).subList(1, args.length);
                    mute.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                } else {
                    List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(1, 2).get(0).split(","));
                    final List<String> reason = Arrays.asList(args).subList(2, args.length);
                    mute.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                    mute.setEvidences(evidences);
                }
                mute.run();
            }
        } catch (Exception e) {
            if (isPlayer) new Message(PunishmentPlugin.getUsages().getString("Usages.mutePlayer")).send(s);
            else new Message(PunishmentPlugin.getUsages().getString("Usages.muteConsole")).send(s);
        }
    }
}
