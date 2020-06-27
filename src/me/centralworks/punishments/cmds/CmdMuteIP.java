package me.centralworks.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
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

public class CmdMuteIP extends Command {

    public CmdMuteIP() {
        super("muteip", Permission.MUTEIP.getPermission(), "silenciarip", "ipsilenciar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final ProxyServer proxy = Main.getInstance().getProxy();
        final boolean isPlayer = s instanceof ProxiedPlayer;
        try {
            if (!Permission.hasPermission(s, Permission.MUTEIP)) {
                new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Run mute = new Run(PunishmentType.MUTE);
            final General generalLib = General.getGeneralLib();
            final String target = Main.isOnlineMode() ? proxy.getPlayer(args[0]) == null ? generalLib.getPlayerUUID(args[0]).toString() : proxy.getPlayer(args[0]).getUniqueId().toString() : proxy.getPlayer(args[0]).getName();
            final AddressIP adr = AddressIP.getInstance();
            if (!adr.existsPlayer(target)) {
                new Message(Main.getMessages().getString("Messages.ip-not-registered")).send(s);
                return;
            }
            mute.setIp(adr.getByAccount(target).getHostName());
            mute.setPunisher(punisher);
            mute.setPermanent(true);
            mute.setTarget(target);
            if (isPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(1, args.length);
                final PunishmentReason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                mute.setPunishmentReason(reasonObj);
                new Message(Main.getMessages().getString("Messages.write-evidences")).send(p);
                Task.getInstance().add(p.getName(), mute);
            } else {
                if (!(args.length == 2)) {
                    final List<String> reason = Arrays.asList(args).subList(1, args.length);
                    mute.setPunishmentReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                } else {
                    List<String> evidences = Lists.newArrayList(Lists.newArrayList(args).subList(1, 2).get(0).split(","));
                    final List<String> reason = Arrays.asList(args).subList(2, args.length);
                    mute.setPunishmentReason(Reasons.getInstance().getByReason(String.join(" ", reason)));
                    mute.setEvidences(evidences);
                }
                mute.execute();
            }
        } catch (Exception e) {
            if (isPlayer) new Message(Main.getUsages().getString("Usages.muteIPPlayer")).send(s);
            else new Message(Main.getUsages().getString("Usages.muteIPConsole")).send(s);
        }
    }
}
