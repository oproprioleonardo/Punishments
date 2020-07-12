package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.Date;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.supliers.PunishmentReason;
import me.centralworks.modules.punishments.models.punishs.supliers.Service;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.Reasons;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdTempBan extends Command {

    public CmdTempBan() {
        super("tempban", "", "tempbanir", "banirtemp");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.TEMPBAN)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service ban = new Service(PunishmentType.TEMPBAN);
            final General generalLib = General.getGeneralLib();
            final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? "" : proxy.getPlayer(args[0]).getUniqueId().toString());
            final Long duration = Date.getInstance().convertPunishmentDuration(Lists.newArrayList(args[1].split(",")));
            ban.setTarget(target);
            ban.setPunisher(punisher);
            ban.setSecondaryIdentifier(args[0]);
            if (s instanceof ProxiedPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(2, args.length);
                final PunishmentReason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                reasonObj.setDuration(duration);
                ban.setPunishmentReason(reasonObj);
                ban.applyOtherInformation(p);
            } else {
                if (!(args.length == 3)) {
                    final List<String> reason = Arrays.asList(args).subList(2, args.length);
                    final PunishmentReason rs = Reasons.getInstance().getByReason(String.join(" ", reason));
                    ban.setPunishmentReason(rs);
                } else {
                    List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(2, 3).get(0).split(","));
                    final List<String> reason = Arrays.asList(args).subList(3, args.length);
                    final PunishmentReason rs = Reasons.getInstance().getByReason(String.join(" ", reason));
                    ban.setPunishmentReason(rs);
                    ban.setEvidences(evidences);
                }
                ban.getPunishmentReason().setDuration(duration);
                ban.run();
            }
        } catch (Exception e) {
            if (s instanceof ProxiedPlayer)
                new Message(PunishmentPlugin.getUsages().getString("Usages.tempBanPlayer")).send(s);
            else new Message(PunishmentPlugin.getUsages().getString("Usages.tempBanConsole")).send(s);
        }
    }
}
