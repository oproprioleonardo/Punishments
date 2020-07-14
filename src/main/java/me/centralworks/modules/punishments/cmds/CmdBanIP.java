package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.supliers.Reason;
import me.centralworks.modules.punishments.models.punishs.supliers.Service;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.AddressIP;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.Reasons;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class CmdBanIP extends Command {

    public CmdBanIP() {
        super("banip", "", "banirip", "ipban");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            final ProxyServer proxy = Main.getInstance().getProxy();
            if (!Permission.hasPermission(s, Permission.BANIP)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            if (Main.isOnlineMode() && proxy.getPlayer(args[0]) == null) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.onlinemode-offline-player")).send(s);
                return;
            }
            final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
            final Service ban = new Service(PunishmentType.BAN);
            final General generalLib = General.getGeneralLib();
            final String target = generalLib.identifierCompare(args[0], proxy.getPlayer(args[0]) == null ? "" : proxy.getPlayer(args[0]).getUniqueId().toString());
            final AddressIP adr = AddressIP.getInstance();
            if (!adr.existsPlayer(target)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.ip-not-registered")).send(s);
                return;
            }
            ban.setIp(adr.getByAccount(target).getHostName());
            ban.setPunisher(punisher);
            ban.setPermanent(true);
            ban.setSecondaryIdentifier(args[0]);
            if (s instanceof ProxiedPlayer) {
                final ProxiedPlayer p = ((ProxiedPlayer) s);
                final List<String> reason = Arrays.asList(args).subList(1, args.length);
                final Reason reasonObj = Reasons.getInstance().getByReason(String.join(" ", reason));
                ban.setTarget(target);
                ban.setReason(reasonObj);
                ban.applyOtherInformation(p);
            } else {
                ban.setTarget(target);
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
            e.printStackTrace();
            if (s instanceof ProxiedPlayer)
                new Message(PunishmentPlugin.getUsages().getString("Usages.banIPPlayer")).send(s);
            else new Message(PunishmentPlugin.getUsages().getString("Usages.banIPConsole")).send(s);
        }
    }
}
