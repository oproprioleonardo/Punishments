package me.centralworks.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.Date;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;
import me.centralworks.punishments.punishs.supliers.cached.Reasons;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import me.centralworks.punishments.punishs.supliers.runners.Run;
import me.centralworks.punishments.punishs.supliers.runners.Task;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdTempBan extends Command {

    public CmdTempBan() {
        super("tempban", Permission.TEMPBAN.getPermission(), "tempbanir", "banirtemp");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.TEMPBAN)) {
                new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(Main.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Run ban = new Run(PunishmentType.TEMPBAN);
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
                new Message(Main.getMessages().getString("Messages.write-evidences")).send(p);
                Task.getInstance().add(p.getName(), ban);
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
            if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.tempBanPlayer")).send(s);
            else new Message(Main.getUsages().getString("Usages.tempBanConsole")).send(s);
        }
    }
}
