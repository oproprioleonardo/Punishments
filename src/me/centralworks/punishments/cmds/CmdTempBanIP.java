package me.centralworks.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.Date;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;
import me.centralworks.punishments.punishs.supliers.cached.AddressIP;
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

public class CmdTempBanIP extends Command {

    public CmdTempBanIP() {
        super("tempbanip", Permission.TEMPBANIP.getPermission(), "tempsilenciarip", "silenciartempip");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.TEMPBANIP)) {
                new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Run ban = new Run(PunishmentType.TEMPBAN);
            final General generalLib = General.getGeneralLib();
            final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? generalLib.getPlayerUUID(args[0]).toString() : proxy.getPlayer(args[0]).getUniqueId().toString());
            final Long duration = Date.getInstance().convertPunishmentDuration(Lists.newArrayList(args[1].split(",")));
            final AddressIP adr = AddressIP.getInstance();
            if (!adr.existsPlayer(target)) {
                new Message(Main.getMessages().getString("Messages.ip-not-registered")).send(s);
                return;
            }
            ban.setIp(adr.getByAccount(target).getHostName());
            ban.setTarget(target);
            ban.setPunisher(punisher);
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
                ban.execute();
            }
        } catch (Exception e) {
            if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.tempBanIPPlayer")).send(s);
            else new Message(Main.getUsages().getString("Usages.tempBanIPConsole")).send(s);
        }
    }
}
