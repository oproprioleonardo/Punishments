package me.centralworks.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.supliers.Reason;
import me.centralworks.bungee.modules.punishments.models.supliers.Service;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.Reasons;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
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
        final ProxyServer proxy = Main.getInstance().getProxy();
        final boolean isPlayer = s instanceof ProxiedPlayer;
        try {
            if (!Permission.hasPermission(s, Permission.MUTE)) return;
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service mute = new Service(PunishmentType.MUTE);
            final General generalLib = General.getGeneralLib();
            final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? "" : proxy.getPlayer(args[0]).getUniqueId().toString());
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
