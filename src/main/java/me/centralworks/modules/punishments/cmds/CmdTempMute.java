package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.Date;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.supliers.Reason;
import me.centralworks.modules.punishments.models.punishs.supliers.Service;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.Reasons;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdTempMute extends Command {

    public CmdTempMute() {
        super("tempmute", "", "silenciartemp", "tempsilenciar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final ProxyServer proxy = Main.getInstance().getProxy();
        final boolean isPlayer = s instanceof ProxiedPlayer;
        try {
            if (!Permission.hasPermission(s, Permission.TEMPMUTE)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service mute = new Service(PunishmentType.TEMPMUTE);
            final General generalLib = General.getGeneralLib();
            final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? "" : proxy.getPlayer(args[0]).getUniqueId().toString());
            final Long duration = Date.getInstance().convertPunishmentDuration(Lists.newArrayList(args[1].split(",")));
            mute.setPunisher(punisher);
            mute.setTarget(target);
            mute.setSecondaryIdentifier(args[0]);
            if (isPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(2, args.length);
                final Reason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                reasonObj.setDuration(duration);
                mute.setReason(reasonObj);
                mute.applyOtherInformation(p);
            } else {
                if (!(args.length == 3)) {
                    final List<String> reason = Arrays.asList(args).subList(2, args.length);
                    mute.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                } else {
                    List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(2, 3).get(0).split(","));
                    final List<String> reason = Arrays.asList(args).subList(3, args.length);
                    mute.setReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                    mute.setEvidences(evidences);
                }
                mute.getReason().setDuration(duration);
                mute.run();
            }
        } catch (Exception e) {
            if (isPlayer) new Message(PunishmentPlugin.getUsages().getString("Usages.tempMutePlayer")).send(s);
            else new Message(PunishmentPlugin.getUsages().getString("Usages.tempMuteConsole")).send(s);
        }
    }
}
