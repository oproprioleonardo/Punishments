package me.centralworks.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
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

public class CmdBan extends Command {

    public CmdBan() {
        super("ban", Permission.BAN.getPermission(), "banir");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.BAN)) {
                new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Run ban = new Run();
            final General generalLib = General.getGeneralLib();
            ban.setPunishmentType(PunishmentType.BAN);
            ban.setPunisher(punisher);
            if (args.length > 1) {
                if (s instanceof ProxiedPlayer) {
                    final ProxiedPlayer p = ((ProxiedPlayer) s);
                    final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? generalLib.getPlayerUUID(args[0]).toString() : proxy.getPlayer(args[0]).getUniqueId().toString());
                    final List<String> reason = Arrays.asList(args).subList(1, args.length);
                    final PunishmentReason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                    ban.setTarget(target);
                    ban.setPunishmentReason(reasonObj);
                    new Message(Main.getMessages().getString("Messages.write-evidences")).send(p);
                    Task.getInstance().add(p.getName(), ban);
                } else {
                    final String target = Main.isOnlineMode() ? proxy.getPlayer(args[0]) == null ? generalLib.getPlayerUUID(args[0]).toString() : proxy.getPlayer(args[0]).getUniqueId().toString() : proxy.getPlayer(args[0]).getName();
                    ban.setTarget(target);
                    if (!(args.length == 2)) {
                        final List<String> reason = Arrays.asList(args).subList(1, args.length);
                        ban.setPunishmentReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                    } else {
                        List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(1, 2).get(0).split(","));
                        final List<String> reason = Arrays.asList(args).subList(2, args.length);
                        ban.setPunishmentReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                        ban.setEvidences(evidences);
                    }
                    ban.setFunctionIfOnline(generalLib.getFunctionBanIfOn());
                    ban.setAnnouncer(generalLib.getFunctionAnnouncerBan());
                    ban.execute();
                }
            } else {
                if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.banPlayer")).send(s);
                else new Message(Main.getUsages().getString("Usages.banConsole")).send(s);
            }
        } catch (Exception e) {
            if (s instanceof ProxiedPlayer) new Message(Main.getUsages().getString("Usages.banPlayer")).send(s);
            else new Message(Main.getUsages().getString("Usages.banConsole")).send(s);
        }
    }
}
